package uk.gov.bis.taxserviceMock.controllers.gateway

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import uk.gov.bis.taxserviceMock.actions.gateway.GatewayUserAction

class AccessCodeController @Inject()(UserAction: GatewayUserAction) extends Controller {

  def show(continue: String, origin: Option[String]) = Action { implicit request =>
    Ok(views.html.gateway.accesscode(continue, origin))
  }

  def handleAccessCode(continue: String, origin: Option[String]) = Action { implicit request =>
    val r = for {
      userId <- request.session.get(UserAction.validatedUserKey)
    } yield Redirect(continue)

    r.getOrElse(Unauthorized)

  }
}
