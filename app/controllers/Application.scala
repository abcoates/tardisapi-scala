package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json._
import play.api.libs.json.JsObject
import models.{Person, Patient, Doctor, Administrator, Symptom, Event}

trait SessionController extends Controller {

  def redirectToLoginPage: Action[AnyContent];

  def isLoggedIn(session: Session): Boolean

  def checkLoggedIn(action: Action[AnyContent]) = action // TODO: was: if (isLoggedIn) action else redirectToLoginPage

}

object Application extends SessionController {

  val RESULT_OK = "OK"
  val RESULT_FAIL = "FAIL"

  val JsonMimeType = "application/json"

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    )
  )

  val personForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  val consentForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "password2" -> nonEmptyText,
      "consent1" -> boolean,
      "consent2" -> boolean,
      "consent3" -> boolean,
      "consent4" -> boolean,
      "consent5" -> boolean
    )
  )

  val symptomForm = Form(
    tuple(
      "whichsymptom" -> nonEmptyText,
      "whensymptom" -> date // TODO: make this a date/time somehow, to nearest quarter hour
    )
  )

  val eventForm = Form(
    tuple(
      "eventname" -> nonEmptyText,
      "eventtime" -> date
    )
  )

  override def isLoggedIn(session: Session) = session.get("personid").map { personid =>
    Person.select(personid.toLong).isDefined
  }.headOption.getOrElse(false)

  def index = Action {
    Redirect(routes.Application.startHere)
  }

  def default(unknown: String) = index

  override def redirectToLoginPage = Action {
    Redirect(routes.Application.startHere).withNewSession
  }

  def startHere = Action {
    Ok(views.html.starthere())
  }

  def infoSheet = Action {
    Ok(views.html.infosheet(consentForm, "")).withNewSession
  }

  def consent = Action { implicit request =>
    consentForm.bindFromRequest.fold(
      errors => BadRequest(views.html.infosheet(errors, "")),
      _ => {
        val (username, email, password, password2, consent1, consent2, consent3, consent4, consent5) = consentForm.bindFromRequest.get
        if (!(consent1 && consent2 && consent3 && consent4 && consent5)) {
          BadRequest(views.html.infosheet(consentForm.bindFromRequest, "You must consent to all 5 conditions in order to register."))
        } else if (!(email.contains("@") && (email.trim.replaceAll("\\s+", "") == email.trim))) {
          BadRequest(views.html.infosheet(consentForm.bindFromRequest, "Please use a valid e-mail address."))
        } else if (password != password2) {
          BadRequest(views.html.infosheet(consentForm.bindFromRequest, "Your 2 passwords did not match, please try again."))
        } else {
          val patientid = Patient.create(username, email, password, List(consent1, consent2, consent3, consent4, consent5))
          if (patientid.isDefined) {
            Redirect(routes.Application.selectPatient(patientid.get)).withSession("personid" -> patientid.get.toString)
          } else {
            Redirect(routes.Application.login)
          }
        }
      }
    )
  }

  def login = Action { implicit request =>
    session.get("personid").map { personid =>
      Redirect(routes.Application.selectPerson(personid.toLong))
    }.getOrElse {
      Ok(views.html.login(loginForm, "")).withNewSession
    }
  }

  def checkLogin = Action { implicit request =>
    val isJSON = request.headers.get("accept").getOrElse("").equals(JsonMimeType)
    loginForm.bindFromRequest.fold(
      errors =>
        if (isJSON) {
          BadRequest(toJson(Map(
            "status" -> RESULT_FAIL,
            "errorCode" -> "checkLogin:fold:fail",
            "errorDetails" -> "There was a problem at the server in interpreting the login details."
          )))
        } else {
          BadRequest(views.html.login(errors, ""))
        },
    _ => {
        val (email, password) = loginForm.bindFromRequest.get
        val person = Person.selectByEmail(email)
        if (person isDefined) {
          if (person.get.checkPassword(password)) {
            if (isJSON) {
              Ok(toJson(Map(
                "status" -> toJson(RESULT_OK),
                "personid" -> toJson(person.get.id),
                "isPatient" -> toJson(Patient.select(person.get.id).isDefined),
                "isDoctor" -> toJson(Doctor.select(person.get.id).isDefined),
                "isAdmin" -> toJson(Administrator.select(person.get.id).isDefined)
              )))
            } else {
              Redirect(routes.Application.selectPerson(person.get.id)).withSession("personid" -> person.get.id.toString)
            }
          } else {
            if (isJSON) {
              BadRequest(toJson(Map(
                "status" -> RESULT_FAIL,
                "errorCode" -> "checkLogin:password:nomatch",
                "errorDetails" -> "The login e-mail address and password did not match.",
                "email" -> email,
                "password" -> password
              )))
            } else {
              BadRequest(views.html.login(loginForm.bindFromRequest, "Your e-mail and password did not match, please try again."))
            }
          }
        } else {
          if (isJSON) {
            BadRequest(toJson(Map(
              "status" -> RESULT_FAIL,
              "errorCode" -> "checkLogin:email:nosuchuser",
              "errorDetails" -> "The login e-mail address does not match a registered user.",
              "email" -> email,
              "password" -> password
            )))
          } else {
            BadRequest(views.html.login(loginForm, ""))
          }
        }
      }
    )
  }

  def logout = Action { implicit request =>
    val isJSON = request.headers.get("accept").getOrElse("").equals(JsonMimeType)
    if (isJSON) {
      Ok(toJson(Map(
        "status" -> RESULT_OK
      )))
    } else {
      Redirect(routes.Application.startHere).withNewSession
    }
  }

  def persons = checkLoggedIn(Action { request =>
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val personDetails = Person.all().map {
        person => Map(
          "id" -> toJson(person.id),
          "username" -> toJson(person.name),
          "email" -> toJson(person.email)
        )
      }
      Ok(toJson(personDetails))
    } else {
      Ok(views.html.persons(Person.all(), personForm))
    }
  })

  def newPerson = checkLoggedIn(Action { implicit request =>
    personForm.bindFromRequest.fold(
      errors => BadRequest(views.html.persons(Person.all(), errors)),
      _ => {
        val (username, email, password) = personForm.bindFromRequest.get
        Person.create(username, email, password)
        Redirect(routes.Application.persons)
      }
    )
  })

  def selectPerson(id: Long) = checkLoggedIn(Action { request =>
    val person = Person.select(id).get // TODO: add code to deal with non-existent person ID
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val personDetails = Map(
        "id" -> toJson(person.id),
        "username" -> toJson(person.name),
        "email" -> toJson(person.email)
      )
      Ok(toJson(personDetails))
    } else {
      Ok(views.html.person(person))
    }
  })

  def deletePerson(id: Long) = checkLoggedIn(Action {
    Person.delete(id)
    Patient.delete(id)
    Doctor.delete(id)
    Administrator.delete(id)
    Redirect(routes.Application.persons)
  })

  def patients = checkLoggedIn(Action { request =>
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val patientDetails = Patient.all().map {
        patient => Map(
          "id" -> toJson(patient.person.id),
          "username" -> toJson(patient.person.name),
          "email" -> toJson(patient.person.email),
          "password" -> toJson(patient.person.password),
          "symptoms" -> toJson(Symptom.all(patient.id).map{symptom => symptom.id}),
          "events" -> toJson(Event.all(patient.id).map{event => event.id})
        )
      }
      Ok(toJson(patientDetails))
    } else {
      Ok(views.html.patients(Patient.all(), personForm))
    }
  })

  def newPatient = checkLoggedIn(Action { implicit request =>
    personForm.bindFromRequest.fold(
      errors => BadRequest(views.html.patients(Patient.all(), errors)),
      _ => {
        val (username, password, email) = personForm.bindFromRequest.get
        Patient.create(username, email, password, List[Boolean]()) // TODO: should this be allowed, no consents?
        Redirect(routes.Application.patients)
      }
    )
  })

  def selectPatient(id: Long) = checkLoggedIn(Action { request =>
    val patient = Patient.select(id).get
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val patientDetails = Map(
        "id" -> toJson(patient.person.id),
        "username" -> toJson(patient.person.name),
        "password" -> toJson(patient.person.password),
        "email" -> toJson(patient.person.email),
        "symptoms" -> toJson(Symptom.all(patient.id).map{symptom => symptom.id}),
        "events" -> toJson(Event.all(patient.id).map{event => event.id})
      )
      Ok(toJson(patientDetails))
    } else {
      Ok(views.html.patient(patient, Symptom.all(patient.id), Event.all(patient.id), symptomForm, eventForm))
    }
  })

  def deletePatient(id: Long) = checkLoggedIn(Action {
    Patient.delete(id)
    Redirect(routes.Application.patients)
  })

  def newSymptom(personid: Long) = checkLoggedIn(Action { implicit request =>
    val patient = Patient.select(personid).get
    symptomForm.bindFromRequest.fold(
      errors => BadRequest(views.html.patient(patient, Symptom.all(patient.id), Event.all(patient.id), errors, eventForm)),
      _ => {
        val (whichsymptom, whensymptom) = symptomForm.bindFromRequest.get
        Symptom.create(patient.id, whichsymptom, whensymptom)
        Redirect(routes.Application.selectPatient(personid))
      }
    )
  })

  def selectSymptom(personid: Long, id: Long) = checkLoggedIn(Action { request =>
    val symptom = Symptom.select(id)
    val patient = Patient.select(personid).get
    if (symptom.patient.id == patient.id) {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        val symptomDetails = Map(
          "id" -> toJson(symptom.id),
          "personid" -> toJson(symptom.patient.person.id),
          "whichsymptom" -> toJson(symptom.whichsymptom),
          "whensymptom" -> toJson(symptom.whensymptom.toString)
        )
        Ok(toJson(symptomDetails))
      } else {
        Ok(views.html.symptom(symptom))
      }
    } else {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        Ok(toJson(Map[String,JsObject]()))
      } else {
        Ok(views.html.patient(patient, Symptom.all(patient.id), Event.all(patient.id), symptomForm, eventForm))
      }
    }
  })

  def deleteSymptom(personid: Long, id: Long) = checkLoggedIn(Action {
    val symptom = Symptom.select(id)
    val patient = Patient.select(personid).get
    if (symptom.patient.id == patient.id) {
      Symptom.delete(id)
    }
    Redirect(routes.Application.selectPatient(personid))
  })

  def newEvent(personid: Long) = checkLoggedIn(Action { implicit request =>
    val patient = Patient.select(personid).get
    eventForm.bindFromRequest.fold(
      errors => BadRequest(views.html.patient(patient, Symptom.all(patient.id), Event.all(patient.id), symptomForm, errors)),
      _ => {
        val (eventname, eventtime) = eventForm.bindFromRequest.get
        Event.create(patient.id, eventname, eventtime)
        Redirect(routes.Application.selectPatient(personid))
      }
    )
  })

  def selectEvent(personid: Long, id: Long) = checkLoggedIn(Action { request =>
    val event = Event.select(id)
    val patient = Patient.select(personid).get
    if (event.patient.id == patient.id) {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        val eventDetails = Map(
          "id" -> toJson(event.id),
          "personid" -> toJson(event.patient.person.id),
          "eventname" -> toJson(event.eventname),
          "eventtime" -> toJson(event.eventtime.toString)
        )
        Ok(toJson(eventDetails))
      } else {
        Ok(views.html.event(event))
      }
    } else {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        Ok(toJson(Map[String,JsObject]()))
      } else {
        Ok(views.html.patient(patient, Symptom.all(patient.id), Event.all(patient.id), symptomForm, eventForm))
      }
    }
  })

  def deleteEvent(personid: Long, id: Long) = checkLoggedIn(Action {
    val event = Event.select(id)
    val patient = Patient.select(personid).get
    if (event.patient.id == patient.id) {
      Event.delete(id)
    }
    Redirect(routes.Application.selectPatient(personid))
  })

}