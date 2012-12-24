package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Person(id: Long, name: String, password: String, email: String)

object Person {

  val person = {
    get[Long]("id") ~
      get[String]("name") ~
        get[String]("password") ~
          get[String]("email") map {
            case id~name~password~email => Person(id, name, password, email)
          }
  }

  def all(): List[Person] = DB.withConnection { implicit c =>
    SQL("select * from person").as(person *)
  }

  def create(name: String, password: String, email: String): Option[Long] = {
    var id: Option[Long] = None
    DB.withConnection { implicit c =>
      id = SQL("insert into person (name, password, email) values ({name}, {password}, {email})").on(
        'name -> name,
        'password -> password,
        'email -> email
      ).executeInsert()
    }
    id
  }

  def select(id: Long): Person = {
    DB.withConnection { implicit c =>
      SQL("select * from person where id = {id}").on(
        'id -> id
      ).as(person *)
    }.head
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from person where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}