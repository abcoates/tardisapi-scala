package models

import scala.util.Random
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import com.roundeights.hasher.Implicits._
import java.util.{Calendar, Date}
import java.text.SimpleDateFormat

case class Person(id: Long, name: Option[String], email: String, password: String, salt: String, dateofregistration: Option[Date]) {
  def checkPassword(aPassword: String): Boolean = aPassword.trim.salt(salt).sha512.toString == password
  def dateofregistrationAsString = if (dateofregistration.isDefined) Person.dateFormatter.format(dateofregistration.get) else "unknown"
  def dateofregistrationAsISOString = if (dateofregistration.isDefined) Person.isoDateFormatter.format(dateofregistration.get) else null
}

object Person {

  val person = {
    get[Long]("id") ~
      get[Option[String]]("name") ~
        get[String]("email") ~
          get[String]("password") ~
            get[String]("salt") ~
              get[Option[Date]]("dateofregistration") map {
                case id~name~email~password~salt~dateofregistration => Person(id, name, email, password, salt, dateofregistration)
              }
  }

  val dateFormatter = new SimpleDateFormat("dd MMM yyyy")
  val isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd")

  def all(): List[Person] = DB.withConnection { implicit c =>
    SQL("select * from person").as(person *)
  }

  def today: Date = {
    val cal = Calendar.getInstance
    cal.setTime(new Date())
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.getTime
  }

  def create(name: String, email: String, password: String, salt: Option[String] = None, dateofregistration: Option[Date] = Some(today)): Option[Long] = {
    var id: Option[Long] = None
    val appliedSalt: String = salt match {
      case Some(s) => s
      case None => // create a new password salt
        val SaltLength = 8
        val sb = new StringBuilder
        val random = new Random()
        val trimPassword = password.trim
        for (idx <- 1 to SaltLength) {
          val index = random.nextInt(trimPassword length)
          sb.append(trimPassword.charAt(index))
        }
        sb.toString
    }
    DB.withConnection { implicit c =>
      id = SQL("insert into person (name, email, password, salt, dateofregistration) values ({name}, {email}, {password}, {salt}, {dateofregistration})").on(
        'name -> (if ((name == null) || (name.trim.length < 1)) None else Some(name.trim)),
        'email -> email.trim,
        'password -> password.trim.salt(appliedSalt).sha512.toString,
        'salt -> appliedSalt,
        'dateofregistration -> dateofregistration
      ).executeInsert()
    }
    id
  }

  def select(id: Long): Option[Person] = {
    DB.withConnection { implicit c =>
      SQL("select * from person where id = {id}").on(
        'id -> id
      ).as(person *)
    }.headOption
  }

  def selectByEmail(email: String): Option[Person] = {
    DB.withConnection { implicit c =>
      SQL("select * from person where email = {email}").on(
        'email -> email.trim
      ).as(person *)
    }.headOption
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from person where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}