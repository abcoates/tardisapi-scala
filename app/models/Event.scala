package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Event(id: Long, userid: Long, eventname: String, eventtime: String)

object Event {

  val event = {
    get[Long]("id") ~
      get[Long]("userid") ~
        get[String]("eventname") ~
          get[String]("eventtime") map {
            case id~userid~eventname~eventtime => Event(id, userid, eventname, eventtime)
          }
  }

  def all(userid: Long): List[Event] = DB.withConnection { implicit c =>
    val allEvents = SQL("select * from event").as(event *)
    allEvents.filter((e: Event) => e.userid.equals(userid))
  }

  def create(userid: Long, eventname: String, eventtime: String) {
    DB.withConnection { implicit c =>
      SQL("insert into event (userid, eventname, eventtime) values ({userid}, {eventname}, {eventtime})").on(
        'userid -> userid,
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