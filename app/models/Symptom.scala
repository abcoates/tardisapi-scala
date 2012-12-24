package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Symptom(id: Long, patientid: Long, whichsymptom: String, whensymptom: String)

object Symptom {

  val symptom = {
    get[Long]("id") ~
      get[Long]("patientid") ~
        get[String]("whichsymptom") ~
          get[String]("whensymptom") map {
            case id~patientid~whichsymptom~whensymptom => Symptom(id, patientid, whichsymptom, whensymptom)
          }
  }

  def all(patientid: Long): List[Symptom] = DB.withConnection { implicit c =>
    val allSymptoms = SQL("select * from symptom").as(symptom *)
    allSymptoms.filter((s: Symptom) => s.patientid.equals(patientid))
  }

  def create(patientid: Long, whichsymptom: String, whensymptom: String) {
    DB.withConnection { implicit c =>
      SQL("insert into symptom (patientid, whichsymptom, whensymptom) values ({patientid}, {whichsymptom}, {whensymptom})").on(
        'patientid -> patientid,
        'whichsymptom -> whichsymptom,
        'whensymptom -> whensymptom
      ).executeInsert()
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