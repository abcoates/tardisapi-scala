package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Patient(id: Long, patientname: String, password: String, email: String)

object Patient {

  val patient = {
    get[Long]("id") ~
      get[String]("patientname") ~
        get[String]("password") ~
          get[String]("email") map {
            case id~patientname~password~email => Patient(id, patientname, password, email)
          }
  }

  def all(): List[Patient] = DB.withConnection { implicit c =>
    SQL("select * from patient").as(patient *)
  }

  def create(patientname: String, password: String, email: String) {
    DB.withConnection { implicit c =>
      SQL("insert into patient (patientname, password, email) values ({patientname}, {password}, {email})").on(
        'patientname -> patientname,
        'password -> password,
        'email -> email
      ).executeUpdate()
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
    DB.withConnection { implicit c =>
      SQL("delete from patient where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}