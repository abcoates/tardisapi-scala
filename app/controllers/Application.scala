package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.{User, Symptom, Event}

object Application extends Controller {

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
    Redirect(routes.Application.users)
  }

  def users = Action {
    Ok(views.html.index(User.all(), userForm))
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

  def selectUser(id: Long) = Action {
    Ok(views.html.user(User.select(id), Symptom.all(id), Event.all(id), symptomForm, eventForm))
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

  def selectSymptom(userid: Long, id: Long) = Action {
    val symptom = Symptom.select(id)
    if (symptom.userid == userid) {
      Ok(views.html.symptom(symptom, symptomForm))
    } else {
      Ok(views.html.user(User.select(userid), Symptom.all(userid), Event.all(userid), symptomForm, eventForm))
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

  def selectEvent(userid: Long, id: Long) = Action {
    val event = Event.select(id)
    if (event.userid == userid) {
      Ok(views.html.event(event, eventForm))
    } else {
      Ok(views.html.user(User.select(userid), Symptom.all(userid), Event.all(userid), symptomForm, eventForm))
    }
  }

  def deleteEvent(userid: Long, id: Long) = Action {
    Event.delete(id)
    Redirect(routes.Application.selectUser(userid))
  }

}