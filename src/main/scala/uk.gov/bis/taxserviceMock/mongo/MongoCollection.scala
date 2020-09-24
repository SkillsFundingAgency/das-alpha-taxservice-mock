package uk.gov.bis.taxserviceMock.mongo

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.api.bson._
import reactivemongo.play.json.compat._
import json2bson._

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.bson.collection.BSONCollection

trait MongoCollection[T] {
  def mongodb: ReactiveMongoApi

  def collectionName: String

  def collectionF(implicit ec: ExecutionContext): Future[BSONCollection] = mongodb.database.map(_.collection[BSONCollection](collectionName))

  def findOne(params: (String, JsValueWrapper)*)(implicit ec: ExecutionContext, Reads: Format[T]): Future[Option[T]] = {
    println("Finding One")
    val selector = Json.obj(params: _*)
    println(selector)
    val of = for {
      collection <- collectionF
      o <- collection.find(selector).cursor[JsObject]().collect[List](1, Cursor.FailOnError[List[JsObject]]()).map(_.headOption)
    } yield o

    of.map {
      case Some(o) => o.validate[T] match {
        case JsSuccess(resp, _) => {
          println(resp)
          Some(resp)
        }
        case JsError(errs) => { 
          println(errs)
          None
        }
      }
      case _ => None
    }
  }

  def remove(params: (String, JsValueWrapper)*)(implicit ec: ExecutionContext): Future[Int] = {
    for {
      coll <- collectionF
      i <- coll.delete().one(Json.obj(params: _*))
    } yield i.n
  }
}
