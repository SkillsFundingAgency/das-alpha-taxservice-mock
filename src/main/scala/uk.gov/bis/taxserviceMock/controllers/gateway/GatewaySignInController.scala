package uk.gov.bis.taxserviceMock.controllers.gateway

import javax.inject.{Inject, Singleton}

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import uk.gov.bis.taxserviceMock.actions.gateway.GatewayUserAction
import uk.gov.bis.taxserviceMock.data.GatewayUserOps

import scala.concurrent.{ExecutionContext, Future}

case class UserData(userId: String, password: String)

@Singleton
class GatewaySignInController @Inject()(gatewayUsers: GatewayUserOps, UserAction: GatewayUserAction)(implicit exec: ExecutionContext) extends Controller {

  val userForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )

  def showSignIn = Action {
    Ok(views.html.gateway.signIn(userForm))
  }

  def handleSignIn = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.gateway.signIn(formWithErrors))),
      userData => {
        gatewayUsers.validate(userData.userId, userData.password).map {
          case Some(user) => Redirect(routes.AccessCodeController.show()).addingToSession((UserAction.validatedUserKey, user.gatewayID))
          case None => Ok(views.html.gateway.signIn(userForm.withError("username", "Bad user name or password")))
        }
      }
    )
  }

  def signOut = Action {
    Redirect(routes.GatewaySignInController.showSignIn()).withNewSession
  }
}
