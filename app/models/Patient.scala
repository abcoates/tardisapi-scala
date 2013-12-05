package models

import scala.util.Random
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date

case class Patient(id: Long, person: Person, uniquestudyid: Long, age: Option[Double]) {
  def uniquestudyidAsString = "%12d".format(uniquestudyid)
  def ageAsString: String = if (age.isDefined) age.get.toString else "unknown"
}

object Patient {

  val patient = {
    get[Long]("id") ~
      get[Long]("personid") ~
        get[Long]("uniquestudyid") ~
          get[Option[Double]]("age") map {
            case id~personid~uniquestudyid~age => Patient(id, Person.select(personid).get, uniquestudyid, age)
          }
  }

  def all(): List[Patient] = DB.withConnection { implicit c =>
    SQL("select * from patient").as(patient *)
  }

  def create(name: String, email: String, password: String, consents: List[Boolean], age: Option[Double]): Option[Long] = {
    val MaxUniqueStudyId = 1000000000000L
    val personid = Person.create(name, email, password)
    if (personid.isDefined) {
      var id: Option[Long] = None
      DB.withConnection { implicit c =>
        val random = new Random
        var uniquestudyid = random.nextLong % MaxUniqueStudyId // unique study ID is twelve digits
        var notCheckedUnique = true
        while (notCheckedUnique) {
          val matches = SQL("select * from patient where uniquestudyid = {uniquestudyid}").on(
            'uniquestudyid -> uniquestudyid
          ).as(patient *)
          if (matches.size > 0) {
            uniquestudyid = random.nextLong % MaxUniqueStudyId // unique study ID is twelve digits
          } else {
            notCheckedUnique = false
          }
        }
        id = SQL("insert into patient (personid, uniquestudyid, age) values ({personid}, {uniquestudyid}, {age})").on(
          'personid -> personid.get,
          'uniquestudyid -> uniquestudyid,
          'age -> age
        ).executeInsert()
      }
      val timestamp = new Date
      if (consents.size >= 1) {
        DB.withConnection { implicit c =>
          SQL("update patient set consenttimestamp = {timestamp}, consent1 = {consent1} where personid = {personid}").on(
            'personid -> personid.get,
            'timestamp -> timestamp,
            'consent1 -> consents(0)
          ).executeUpdate()
        }
      }
      if (consents.size >= 2) {
        DB.withConnection { implicit c =>
          SQL("update patient set consent2 = {consent2} where personid = {personid}").on(
            'personid -> personid.get,
            'consent2 -> consents(1)
          ).executeUpdate()
        }
      }
      if (consents.size >= 3) {
        DB.withConnection { implicit c =>
          SQL("update patient set consent3 = {consent3} where personid = {personid}").on(
            'personid -> personid.get,
            'consent3 -> consents(2)
          ).executeUpdate()
        }
      }
      if (consents.size >= 4) {
        DB.withConnection { implicit c =>
          SQL("update patient set consent4 = {consent4} where personid = {personid}").on(
            'personid -> personid.get,
            'consent4 -> consents(3)
          ).executeUpdate()
        }
      }
      if (consents.size >= 5) {
        DB.withConnection { implicit c =>
          SQL("update patient set consent5 = {consent5} where personid = {personid}").on(
            'personid -> personid.get,
            'consent5 -> consents(4)
          ).executeUpdate()
        }
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

  def selectByPatientId(id: Long): Option[Patient] = {
    DB.withConnection { implicit c =>
      SQL("select * from patient where id = {id}").on(
        'id -> id
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