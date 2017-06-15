package airport

import akka.actor.ActorSystem

object AirportMain extends App {
  val system = ActorSystem("ActorStudio")
  val supervisorActor =
    system.actorOf(AirportSupervisorActor.props, "supervisorActor")
  supervisorActor ! AirportSupervisorActor.InitializeAirport
}
