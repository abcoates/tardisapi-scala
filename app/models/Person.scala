package models

import scala.util.Random
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import com.roundeights.hasher.Implicits._

case class Person(id: Long, name: String, email: String, password: String, salt: String) {
  def checkPassword(aPassword: String): Boolean = aPassword.trim.salt(salt).sha512.toString == password
}

object Person {

  val person = {
    get[Long]("id") ~
      get[String]("name") ~
        get[String]("email") ~
          get[String]("password") ~
            get[String]("salt") map {
            case id~name~email~password~salt => Person(id, name, email, password, salt)
          }
  }

  def all(): List[Person] = DB.withConnection { implicit c =>
    SQL("select * from person").as(person *)
  }

  def create(name: String, email: String, password: String): Option[Long] = {
    val SaltLength = 8
    val salt = new StringBuilder
    val random = new Random()
    val trimPassword = password.trim
    for (idx <- 1 to SaltLength) {
      val index = random.nextInt(trimPassword length)
      salt.append(trimPassword.charAt(index))
    }
    create(name, email, password, salt toString)
  }

  def create(name: String, email: String, password: String, salt: String): Option[Long] = {
    var id: Option[Long] = None
    DB.withConnection { implicit c =>
      id = SQL("insert into person (name, email, password, salt) values ({name}, {email}, {password}, {salt})").on(
        'name -> name.trim,
        'email -> email.trim,
        'password -> password.trim.salt(salt).sha512.toString,
        'salt -> salt
      ).executeInsert()
    }
    id
  }

  // TODO: what happens if you select a non-existent ID?
  def select(id: Long): Person = {
    DB.withConnection { implicit c =>
      SQL("select * from person where id = {id}").on(
        'id -> id
      ).as(person *)
    }.head
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