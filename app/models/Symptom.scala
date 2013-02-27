package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date
import java.text.SimpleDateFormat

case class Symptom(id: Long, patient: Patient, whichsymptom: String, whensymptom: Date) {

  def whensymptomAsString = Symptom.dateFormatter.format(whensymptom)

}

object Symptom {

  val symptom = {
    get[Long]("id") ~
      get[Long]("patientid") ~
        get[String]("whichsymptom") ~
          get[Date]("whensymptom") map {
            case id~patientid~whichsymptom~whensymptom => Symptom(id, Patient.selectByPatientId(patientid).get, whichsymptom, whensymptom)
          }
  }

  val dateFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a")

  def all(patientid: Long): List[Symptom] = DB.withConnection { implicit c =>
    val allSymptoms = SQL("select * from symptom").as(symptom *)
    allSymptoms.filter((s: Symptom) => s.patient.id.equals(patientid))
  }

  def create(patientid: Long, whichsymptom: String, whensymptom: Date) {
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