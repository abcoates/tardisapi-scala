package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.Logger

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

  def create(name: String, password: String, email: String): Option[Long] = {
    val personid = Person.create(name, password, email)
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

  def select(id: Long): Patient = {
    DB.withConnection { implicit c =>
      SQL("select * from patient where id = {id}").on(
        'id -> id
      ).as(patient *)
    }.head
  }

  def delete(id: Long) {
    val patient = Patient.select(id)
    val personid = patient.person.id
    DB.withConnection { implicit c =>
      SQL("delete from patient where id = {id}").on(
        'id -> id
      ).executeUpdate()
      Person.delete(personid)
    }
  }

}