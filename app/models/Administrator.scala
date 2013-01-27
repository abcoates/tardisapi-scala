package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Administrator(id: Long, person: Person)

object Administrator {

  val admin = {
    get[Long]("id") ~
      get[Long]("personid") map {
      case id~personid => Administrator(id, Person.select(personid))
    }
  }

  def all(): List[Administrator] = DB.withConnection { implicit c =>
    SQL("select * from admin").as(admin *)
  }

  def create(name: String, password: String, email: String): Option[Long] = {
    val personid = Person.create(name, email, password)
    if (personid.isDefined) {
      var id: Option[Long] = None
      DB.withConnection { implicit c =>
        id = SQL("insert into admin (personid) values ({personid})").on(
          'personid -> personid.get
        ).executeInsert()
      }
      id
    } else {
      None
    }
  }

  def select(personid: Long): Option[Administrator] = {
    DB.withConnection { implicit c =>
      SQL("select * from admin where personid = {personid}").on(
        'personid -> personid
      ).as(admin *)
    }.headOption
  }

  def delete(personid: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from admin where personid = {personid}").on(
        'personid -> personid
      ).executeUpdate()
      Person.delete(personid)
    }
  }

}