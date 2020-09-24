package uk.gov.bis.taxserviceMock.mongo

import javax.inject.Inject

import play.modules.reactivemongo.ReactiveMongoApi
import uk.gov.bis.taxserviceMock.data.{Scope, ScopeOps}
import reactivemongo.api.bson._
import play.api.libs.json._
import reactivemongo.play.json.compat._

import scala.concurrent.ExecutionContext

class ScopeMongo @Inject()(val mongodb: ReactiveMongoApi) extends MongoCollection[Scope] with ScopeOps {
  implicit val scopeFormat = Json.format[Scope]

  override def collectionName: String = "sys_scopes"

  override def byName(scopeName: String)(implicit ec: ExecutionContext) = findOne("name" -> scopeName)
}
