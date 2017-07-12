package airport

import airport.AirportSupervisorActor.{
  CreateAircraft,
  InitializeAirport,
  Quit,
  RunwayEmpty
}
import airport.TowerActor.InitializeTower
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class AirportSupervisorActor extends Actor with ActorLogging {

  private val tower: ActorRef = createTower()
  private var aircraft: List[ActorRef] = List.empty

  private def createTower() = {
    log.debug("Creating tower")
    context.actorOf(TowerActor.props, "Tower")
  }

  private def createAircraft(activityDetails: ActivityDetails) = {
    aircraft :+ context.actorOf(AirplaneActor.props(tower, activityDetails),
                                "Flight-" + activityDetails.flight)
  }

  override def receive: Receive = {
    case InitializeAirport =>
      log.info("initializing airport system")
      tower ! InitializeTower
    case CreateAircraft(activity) =>
      createAircraft(activity)
    case RunwayEmpty =>
      log.info("Runway is empty")
    case Quit =>
      log.info("Simulation ending")
      context.system.terminate()
  }
}

object AirportSupervisorActor {
  case object InitializeAirport
  case object RunwayEmpty
  case class CreateAircraft(activity: ActivityDetails)
  case object Quit
  val props: Props = Props[AirportSupervisorActor]
}
