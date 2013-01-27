package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Doctor(id: Long, person: Person)

object Doctor {

  val doctor = {
    get[Long]("id") ~
      get[Long]("personid") map {
      case id~personid => Doctor(id, Person.select(personid))
    }
  }

  def all(): List[Doctor] = DB.withConnection { implicit c =>
    SQL("select * from doctor").as(doctor *)
  }

  def create(name: String, password: String, email: String): Option[Long] = {
    val personid = Person.create(name, email, password)
    if (personid.isDefined) {
      var id: Option[Long] = None
      DB.withConnection { implicit c =>
        id = SQL("insert into doctor (personid) values ({personid})").on(
          'personid -> personid.get
        ).executeInsert()
      }
      id
    } else {
      None
    }
  }

  def select(personid: Long): Option[Doctor] = {
    DB.withConnection { implicit c =>
      SQL("select * from doctor where personid = {personid}").on(
        'personid -> personid
      ).as(doctor *)
    }.headOption
  }

  def delete(personid: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from doctor where personid = {personid}").on(
        'personid -> personid
      ).executeUpdate()
      Person.delete(personid)
    }
  }

}