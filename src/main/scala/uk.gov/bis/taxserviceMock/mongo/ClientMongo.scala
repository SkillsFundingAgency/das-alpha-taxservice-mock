package uk.gov.bis.taxserviceMock.mongo

import javax.inject.Inject

import play.modules.reactivemongo.ReactiveMongoApi
import uk.gov.bis.taxserviceMock.data.{AuthCodeRow, ClientOps}
import reactivemongo.api.Cursor
import reactivemongo.api.bson._
import play.api.libs.json._
import reactivemongo.play.json.compat._
import json2bson._

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.bson.collection.BSONCollection

class ClientMongo @Inject()(val mongodb: ReactiveMongoApi) extends ClientOps {
  
  def collectionF(implicit ec: ExecutionContext): Future[BSONCollection] = mongodb.database.map(_.collection[BSONCollection]("applications"))

  override def validate(id: String, secret: Option[String], grantType: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    for {
      coll <- collectionF
      o <- coll.find(Json.obj("clientID" -> id, "clientSecret" -> secret),projection=Option.empty[JsObject]).cursor[JsObject]().collect[List](1, Cursor.FailOnError[List[JsObject]]()).map(_.nonEmpty)
    } yield o
  }
}
