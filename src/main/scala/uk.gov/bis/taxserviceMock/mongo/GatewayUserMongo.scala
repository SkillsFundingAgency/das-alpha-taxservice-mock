package uk.gov.bis.taxserviceMock.mongo

import javax.inject._

import play.modules.reactivemongo._
import reactivemongo.api.bson._
import uk.gov.bis.taxserviceMock.data.{GatewayUser, GatewayUserOps}
import play.api.libs.json._
import reactivemongo.play.json.compat._

import scala.concurrent.ExecutionContext

class GatewayUserMongo @Inject()(val mongodb: ReactiveMongoApi) extends MongoCollection[GatewayUser] with GatewayUserOps {

  implicit val gatewayUserFormat = Json.format[GatewayUser]

  override val collectionName = "gateway_users"

  override def forGatewayID(gatewayId: String)(implicit ec: ExecutionContext) = findOne("gatewayID" -> gatewayId)

  override def forEmpref(empref: String)(implicit ec: ExecutionContext) = findOne("empref" -> empref)

  override def validate(gatewayID: String, password: String)(implicit ec: ExecutionContext) = findOne("gatewayID" -> gatewayID, "password" -> password)
}
