package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date
import java.text.SimpleDateFormat

case class Event(id: Long, patient: Patient, eventname: String, eventtime: Date) {

  def eventtimeAsString = Event.dateFormatter.format(eventtime)

}

object Event {

  val event = {
    get[Long]("id") ~
      get[Long]("patientid") ~
        get[String]("eventname") ~
          get[Date]("eventtime") map {
            case id~patientid~eventname~eventtime => Event(id, Patient.selectByPatientId(patientid).get, eventname, eventtime)
          }
  }

  val dateFormatter = new SimpleDateFormat("dd MMM yyyy")

  def all(patientid: Long): List[Event] = DB.withConnection { implicit c =>
    val allEvents = SQL("select * from event").as(event *)
    allEvents.filter((e: Event) => e.patient.id.equals(patientid))
  }

  def create(patientid: Long, eventname: String, eventtime: Date) = {
    var id: Option[Long] = None
    DB.withConnection { implicit c =>
      id = SQL("insert into event (patientid, eventname, eventtime) values ({patientid}, {eventname}, {eventtime})").on(
        'patientid -> patientid,
        'eventname -> eventname,
        'eventtime -> eventtime
      ).executeInsert()
    }
    id
  }

  def select(id: Long): Event = {
    DB.withConnection { implicit c =>
      SQL("select * from event where id = {id}").on(
        'id -> id
      ).as(event *)
    }.head
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from event where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}