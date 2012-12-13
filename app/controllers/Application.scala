package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json._
import play.api.libs.json.JsObject
import models.{Patient, Symptom, Event}

object Application extends Controller {

  val JsonMimeType = "application/json"

  val patientForm = Form(
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

  def index = Action {
    Redirect(routes.Application.login)
  }

  def login = Action {
    Ok(views.html.login())
  }

  def patients = Action { request =>
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val patientDetails = Patient.all().map {
        patient => Map(
          "id" -> toJson(patient.id),
          "username" -> toJson(patient.patientname),
          "password" -> toJson(patient.password),
          "email" -> toJson(patient.email),
          "symptoms" -> toJson(Symptom.all(patient.id).map{symptom => symptom.id}),
          "events" -> toJson(Event.all(patient.id).map{event => event.id})
        )
      }
      Ok(toJson(patientDetails))
    } else {
      Ok(views.html.patients(Patient.all(), patientForm))
    }
  }

  def newPatient = Action { implicit request =>
    patientForm.bindFromRequest.fold(
      errors => BadRequest(views.html.patients(Patient.all(), errors)),
      _ => {
        val (username, password, email) = patientForm.bindFromRequest.get
        Patient.create(username, password, email)
        Redirect(routes.Application.patients)
      }
    )
  }

  def selectPatient(id: Long) = Action { request =>
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val patient = Patient.select(id)
      val patientDetails = Map(
        "id" -> toJson(patient.id),
        "username" -> toJson(patient.patientname),
        "password" -> toJson(patient.password),
        "email" -> toJson(patient.email),
        "symptoms" -> toJson(Symptom.all(patient.id).map{symptom => symptom.id}),
        "events" -> toJson(Event.all(patient.id).map{event => event.id})
      )
      Ok(toJson(patientDetails))
    } else {
      Ok(views.html.patient(Patient.select(id), Symptom.all(id), Event.all(id), symptomForm, eventForm))
    }
  }

  def deletePatient(id: Long) = Action {
    Patient.delete(id)
    Redirect(routes.Application.patients)
  }

  def newSymptom(patientid: Long) = Action { implicit request =>
    symptomForm.bindFromRequest.fold(
      errors => BadRequest(views.html.patient(Patient.select(patientid), Symptom.all(patientid), Event.all(patientid), errors, eventForm)),
      _ => {
        val (whichsymptom, whensymptom) = symptomForm.bindFromRequest.get
        Symptom.create(patientid, whichsymptom, whensymptom)
        Redirect(routes.Application.selectPatient(patientid))
      }
    )
  }

  def selectSymptom(patientid: Long, id: Long) = Action { request =>
    val symptom = Symptom.select(id)
    if (symptom.patientid == patientid) {
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
        Ok(views.html.patient(Patient.select(patientid), Symptom.all(patientid), Event.all(patientid), symptomForm, eventForm))
      }
    }
  }

  def deleteSymptom(patientid: Long, id: Long) = Action {
    Symptom.delete(id)
    Redirect(routes.Application.selectPatient(patientid))
  }

  def newEvent(patientid: Long) = Action { implicit request =>
    eventForm.bindFromRequest.fold(
      errors => BadRequest(views.html.patient(Patient.select(patientid), Symptom.all(patientid), Event.all(patientid), symptomForm, errors)),
      _ => {
        val (eventname, eventtime) = eventForm.bindFromRequest.get
        Event.create(patientid, eventname, eventtime)
        Redirect(routes.Application.selectPatient(patientid))
      }
    )
  }

  def selectEvent(patientid: Long, id: Long) = Action { request =>
    val event = Event.select(id)
    if (event.patientid == patientid) {
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
        Ok(views.html.patient(Patient.select(patientid), Symptom.all(patientid), Event.all(patientid), symptomForm, eventForm))
      }
    }
  }

  def deleteEvent(patientid: Long, id: Long) = Action {
    Event.delete(id)
    Redirect(routes.Application.selectPatient(patientid))
  }

}