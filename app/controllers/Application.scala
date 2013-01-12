package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json._
import play.api.libs.json.JsObject
import models.{Person, Patient, Symptom, Event}

trait SessionController extends Controller {

  def redirectToLoginPage: Action[AnyContent];

  def isLoggedIn: Boolean

  def checkLoggedIn(action: Action[AnyContent]) = if (isLoggedIn) action else redirectToLoginPage

}

object Application extends SessionController {

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
      "password" -> nonEmptyText,
      "email" -> nonEmptyText
    )
  )

  val symptomForm = Form(
    tuple(
      "whichsymptom" -> nonEmptyText,
      "whensymptom" -> nonEmptyText
    )
  )

  val eventForm = Form(
    tuple(
      "eventname" -> nonEmptyText,
      "eventtime" -> nonEmptyText
    )
  )

  override def isLoggedIn = true // TODO: add appropriate session-reading code here

  def index = Action {
    Redirect(routes.Application.startHere)
  }

  def default(unknown: String) = index

  override def redirectToLoginPage = Action {
    Redirect(routes.Application.startHere)
  }

  def startHere = Action {
    Ok(views.html.starthere())
  }

  def infoSheet = Action {
    Ok(views.html.infosheet())
  }

  def consent = Action {
    // TODO: [ABC] store consent information from form
    // TODO: [ABC] go to registration form, not login form
    // Ok(views.html.register())
    Ok(views.html.login(loginForm))
  }

  def login = Action {
    Ok(views.html.login(loginForm))
  }

  def checkLogin = Action {
    // TODO: [ABC] add some actual validate code here
    Redirect(routes.Application.login)
  }

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
        Patient.create(username, password, email)
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
    if (symptom.patientid == patient.id) {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        val symptomDetails = Map(
          "id" -> toJson(symptom.id),
          "patientid" -> toJson(symptom.patientid),
          "whichsymptom" -> toJson(symptom.whichsymptom),
          "whensymptom" -> toJson(symptom.whensymptom)
        )
        Ok(toJson(symptomDetails))
      } else {
        Ok(views.html.symptom(symptom, symptomForm))
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
    if (symptom.patientid == patient.id) {
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
    if (event.patientid == patient.id) {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        val eventDetails = Map(
          "id" -> toJson(event.id),
          "userid" -> toJson(event.patientid),
          "eventname" -> toJson(event.eventname),
          "eventtime" -> toJson(event.eventtime)
        )
        Ok(toJson(eventDetails))
      } else {
        Ok(views.html.event(event, eventForm))
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
    if (event.patientid == patient.id) {
      Event.delete(id)
    }
    Redirect(routes.Application.selectPatient(personid))
  })

}