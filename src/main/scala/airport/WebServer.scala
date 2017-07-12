package airport

import airport.AirportSupervisorActor.{CreateAircraft, Quit}
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

class WebServer(system: ActorSystem) extends JsonProtocol {

  private val airport = system.actorSelection("/user/airportSupervisorActor")
  val routes: Route =
    path("airplane") {
      (post & entity(as[ActivityDetails])) { activityDetails =>
        airport ! CreateAircraft(activityDetails)
        complete(201, s"${activityDetails.flight} created.")
      }
    } ~
      path("quit") {
        post {
          airport ! Quit
          complete(200, s"airport closing for business\n")
        }
      }
}
