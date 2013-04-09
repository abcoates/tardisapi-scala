package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.text.SimpleDateFormat
import java.util.Date
import org.joda.time._
import org.joda.time.format._

case class Symptom(id: Long, patient: Patient, whichsymptom: String, whensymptom: Date) {

  def whensymptomAsString = Symptom.dateFormatter.format(whensymptom)

}

object Symptom {

  val dateFormatGeneration: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

  implicit def rowToDateTime: Column[DateTime] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case ts: java.sql.Timestamp => Right(new DateTime(ts.getTime))
      case d: java.sql.Date => Right(new DateTime(d.getTime))
      case str: java.lang.String => Right(dateFormatGeneration.parseDateTime(str))
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass) )
    }
  }

  implicit val dateTimeToStatement = new ToStatement[DateTime] {
    def set(s: java.sql.PreparedStatement, index: Int, aValue: DateTime): Unit = {
      s.setTimestamp(index, new java.sql.Timestamp(aValue.withMillisOfSecond(0).getMillis()) )
    }
  }

  val symptom = {
    get[Long]("id") ~
      get[Long]("patientid") ~
        get[String]("whichsymptom") ~
          get[DateTime]("whensymptom") map {
            case id~patientid~whichsymptom~whensymptom => Symptom(id, Patient.selectByPatientId(patientid).get, whichsymptom, new Date(whensymptom getMillis))
          }
  }

  val dateFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a")

  def all(patientid: Long): List[Symptom] = DB.withConnection { implicit c =>
    val allSymptoms = SQL("select * from symptom").as(symptom *)
    allSymptoms.filter((s: Symptom) => s.patient.id.equals(patientid))
  }

  def create(patientid: Long, whichsymptom: String, whensymptom: Date) = {
    var id: Option[Long] = None
    DB.withConnection { implicit c =>
      println("DBG: Symptom.create: whensymptom = " + whensymptom)
      id = SQL("insert into symptom (patientid, whichsymptom, whensymptom) values ({patientid}, {whichsymptom}, {whensymptom})").on(
        'patientid -> patientid,
        'whichsymptom -> whichsymptom,
        'whensymptom -> new DateTime(whensymptom getTime)
      ).executeInsert()
    }
    id
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