package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class User(id: Long, username: String, password: String, email: String)

object User {

  val user = {
    get[Long]("id") ~
      get[String]("username") ~
        get[String]("password") ~
          get[String]("email") map {
            case id~username~password~email => User(id, username, password, email)
          }
  }

  def all(): List[User] = DB.withConnection { implicit c =>
    SQL("select * from user").as(user *)
  }

  def create(username: String, password: String, email: String) {
    DB.withConnection { implicit c =>
      SQL("insert into user (username, password, email) values ({username}, {password}, {email})").on(
        'username -> username,
        'password -> password,
        'email -> email
      ).executeUpdate()
    }
  }

  def select(id: Long): User = {
    DB.withConnection { implicit c =>
      SQL("select * from user where id = {id}").on(
        'id -> id
      ).as(user *)
    }.head
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from user where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}