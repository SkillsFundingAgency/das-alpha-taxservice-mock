package uk.gov.bis.taxserviceMock.mongo

import javax.inject.Inject

import play.api.Logger
import reactivemongo.api.bson._
import play.modules.reactivemongo.ReactiveMongoApi
import uk.gov.bis.taxserviceMock.data.{AuthCodeOps, AuthCodeRow}
import play.api.libs.json._
import reactivemongo.play.json.compat._
import json2bson._

import scala.concurrent.{ExecutionContext, Future}

class AuthCodeMongo @Inject()(val mongodb: ReactiveMongoApi) extends MongoCollection[AuthCodeRow] with AuthCodeOps {
  implicit val fmt = Json.format[AuthCodeRow]

  override val collectionName: String = "sys_auth_codes"

  override def find(code: String)(implicit ec: ExecutionContext) = findOne("authorizationCode" -> code)

  override def delete(code: String)(implicit ec: ExecutionContext): Future[Int] = remove("authorizationCode" -> code)

  override def create(code: String, gatewayUserId: String, redirectUri: String, clientId: String, scope: String)(implicit ec: ExecutionContext): Future[Int] = {
    Logger.debug("create auth code entry")
    val row = AuthCodeRow(code, gatewayUserId, redirectUri, System.currentTimeMillis(), Some("read:apprenticeship-levy"), Some(clientId), 3600)
    for {
      coll <- collectionF
      i <- coll.insert(ordered = false).one(row)
    } yield i.n
  }

  override def insert(authCode: AuthCodeRow)(implicit ec: ExecutionContext): Future[Int] = {
    for {
      coll <- collectionF
      i <- coll.insert(ordered = false).one(authCode)
    } yield i.n
  }
}
