package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Event(id: Long, patientid: Long, eventname: String, eventtime: String)

object Event {

  val event = {
    get[Long]("id") ~
      get[Long]("patientid") ~
        get[String]("eventname") ~
          get[String]("eventtime") map {
            case id~patientid~eventname~eventtime => Event(id, patientid, eventname, eventtime)
          }
  }

  def all(patientid: Long): List[Event] = DB.withConnection { implicit c =>
    val allEvents = SQL("select * from event").as(event *)
    allEvents.filter((e: Event) => e.patientid.equals(patientid))
  }

  def create(patientid: Long, eventname: String, eventtime: String) {
    DB.withConnection { implicit c =>
      SQL("insert into event (patientid, eventname, eventtime) values ({patientid}, {eventname}, {eventtime})").on(
        'patientid -> patientid,
        'eventname -> eventname,
        'eventtime -> eventtime
      ).executeUpdate()
    }
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