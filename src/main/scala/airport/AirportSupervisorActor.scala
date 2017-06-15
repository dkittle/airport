package airport

import airport.AirportSupervisorActor.{InitializeAirport, RunwayEmpty}
import airport.TowerActor.InitializeTower
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class AirportSupervisorActor extends Actor with ActorLogging {

  private def createTower() = {
    log.debug("Creating tower")
    context.actorOf(TowerActor.props, "Tower")
  }

  private def createAircraft(tower: ActorRef) = {
    val airplane123: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS123", Takeoff, 45, 63)),
      "airplane-IS123")
    val airplane788: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS788", Takeoff, 32, 75)),
      "airplane-IS788")
    val airplane456: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS456", Land, 105, 78)),
      "airplane-IS456")
    val airplane321: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS321", Takeoff, 45, 147)),
      "airplane-IS321")
    val airplane993: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS993", Takeoff, 37, 221)),
      "airplane-IS993")
    val airplane623: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS623", Takeoff, 37, 244)),
      "airplane-IS623")
    val airplane898: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS898", Land, 55, 302)),
      "airplane-IS898")
    val airplane488: ActorRef = context.actorOf(
      AirplaneActor.props(tower, ActivityDetails("IS488", Takeoff, 32, 46)),
      "airplane-IS488")
  }

  override def receive: Receive = {
    case InitializeAirport =>
      log.info("initializing airport system")
      val tower = createTower()
      createAircraft(tower)
      tower ! InitializeTower
    case RunwayEmpty =>
      System.exit(0)
  }
}

object AirportSupervisorActor {
  case object InitializeAirport
  case object RunwayEmpty
  val props: Props = Props[AirportSupervisorActor]
}
