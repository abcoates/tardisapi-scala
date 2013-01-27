package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Patient(id: Long, person: Person)

object Patient {

  val patient = {
    get[Long]("id") ~
      get[Long]("personid") map {
        case id~personid => Patient(id, Person.select(personid))
      }
  }

  def all(): List[Patient] = DB.withConnection { implicit c =>
    SQL("select * from patient").as(patient *)
  }

  def create(name: String, email: String, password: String): Option[Long] = {
    val personid = Person.create(name, email, password)
    if (personid.isDefined) {
      var id: Option[Long] = None
      DB.withConnection { implicit c =>
        id = SQL("insert into patient (personid) values ({personid})").on(
          'personid -> personid.get
        ).executeInsert()
      }
      id
    } else {
      None
    }
  }

  def select(personid: Long): Option[Patient] = {
    DB.withConnection { implicit c =>
      SQL("select * from patient where personid = {personid}").on(
        'personid -> personid
      ).as(patient *)
    }.headOption
  }

  def delete(personid: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from patient where personid = {personid}").on(
        'personid -> personid
      ).executeUpdate()
      Person.delete(personid)
    }
  }

}