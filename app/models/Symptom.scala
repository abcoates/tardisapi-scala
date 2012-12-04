package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Symptom(id: Long, userid: Long, whichsymptom: String, whensymptom: String)

object Symptom {

  val symptom = {
    get[Long]("id") ~
      get[Long]("userid") ~
        get[String]("whichsymptom") ~
          get[String]("whensymptom") map {
            case id~userid~whichsymptom~whensymptom => Symptom(id, userid, whichsymptom, whensymptom)
          }
  }

  def all(userid: Long): List[Symptom] = DB.withConnection { implicit c =>
    val allSymptoms = SQL("select * from symptom").as(symptom *)
    allSymptoms.filter((s: Symptom) => s.userid.equals(userid))
  }

  def create(userid: Long, whichsymptom: String, whensymptom: String) {
    DB.withConnection { implicit c =>
      SQL("insert into symptom (userid, whichsymptom, whensymptom) values ({userid}, {whichsymptom}, {whensymptom})").on(
        'userid -> userid,
        'whichsymptom -> whichsymptom,
        'whensymptom -> whensymptom
      ).executeUpdate()
    }
  }

  def select(id: Long): Symptom = {
    DB.withConnection { implicit c =>
      SQL("select * from symptom where id = {id}").on(
        'id -> id
      ).as(symptom *)
    }.head
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from symptom where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}