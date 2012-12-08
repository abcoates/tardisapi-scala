package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json._
import play.api.libs.json.JsObject
import models.{User, Symptom, Event}

object Application extends Controller {

  val JsonMimeType = "application/json"

  val userForm = Form(
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

  def users = Action { request =>
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val userDetails = User.all().map {
        user => Map(
          "id" -> toJson(user.id),
          "username" -> toJson(user.username),
          "password" -> toJson(user.password),
          "email" -> toJson(user.email),
          "symptoms" -> toJson(Symptom.all(user.id).map{symptom => symptom.id}),
          "events" -> toJson(Event.all(user.id).map{event => event.id})
        )
      }
      Ok(toJson(userDetails))
    } else {
      Ok(views.html.users(User.all(), userForm))
    }
  }

  def newUser = Action { implicit request =>
    userForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(User.all(), errors)),
      _ => {
        val (username, password, email) = userForm.bindFromRequest.get
        User.create(username, password, email)
        Redirect(routes.Application.users)
      }
    )
  }

  def selectUser(id: Long) = Action { request =>
    if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
      val user = User.select(id)
      val userDetails = Map(
        "id" -> toJson(user.id),
        "username" -> toJson(user.username),
        "password" -> toJson(user.password),
        "email" -> toJson(user.email),
        "symptoms" -> toJson(Symptom.all(user.id).map{symptom => symptom.id}),
        "events" -> toJson(Event.all(user.id).map{event => event.id})
      )
      Ok(toJson(userDetails))
    } else {
      Ok(views.html.user(User.select(id), Symptom.all(id), Event.all(id), symptomForm, eventForm))
    }
  }

  def deleteUser(id: Long) = Action {
    User.delete(id)
    Redirect(routes.Application.users)
  }

  def newSymptom(userid: Long) = Action { implicit request =>
    symptomForm.bindFromRequest.fold(
      errors => BadRequest(views.html.user(User.select(userid), Symptom.all(userid), Event.all(userid), errors, eventForm)),
      _ => {
        val (whichsymptom, whensymptom) = symptomForm.bindFromRequest.get
        Symptom.create(userid, whichsymptom, whensymptom)
        Redirect(routes.Application.selectUser(userid))
      }
    )
  }

  def selectSymptom(userid: Long, id: Long) = Action { request =>
    val symptom = Symptom.select(id)
    if (symptom.userid == userid) {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        val symptomDetails = Map(
          "id" -> toJson(symptom.id),
          "userid" -> toJson(symptom.userid),
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
        Ok(views.html.user(User.select(userid), Symptom.all(userid), Event.all(userid), symptomForm, eventForm))
      }
    }
  }

  def deleteSymptom(userid: Long, id: Long) = Action {
    Symptom.delete(id)
    Redirect(routes.Application.selectUser(userid))
  }

  def newEvent(userid: Long) = Action { implicit request =>
    eventForm.bindFromRequest.fold(
      errors => BadRequest(views.html.user(User.select(userid), Symptom.all(userid), Event.all(userid), symptomForm, errors)),
      _ => {
        val (eventname, eventtime) = eventForm.bindFromRequest.get
        Event.create(userid, eventname, eventtime)
        Redirect(routes.Application.selectUser(userid))
      }
    )
  }

  def selectEvent(userid: Long, id: Long) = Action { request =>
    val event = Event.select(id)
    if (event.userid == userid) {
      if (request.headers.get("accept").getOrElse("").equals(JsonMimeType)) {
        val eventDetails = Map(
          "id" -> toJson(event.id),
          "userid" -> toJson(event.userid),
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
        Ok(views.html.user(User.select(userid), Symptom.all(userid), Event.all(userid), symptomForm, eventForm))
      }
    }
  }

  def deleteEvent(userid: Long, id: Long) = Action {
    Event.delete(id)
    Redirect(routes.Application.selectUser(userid))
  }

}