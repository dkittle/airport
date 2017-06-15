package airport

import airport.AirplaneActor.{
  Circle,
  HoldOnTaxiway,
  LandingClearance,
  TakeoffClearance
}
import airport.AirportSupervisorActor.RunwayEmpty
import airport.TowerActor.{
  InitializeTower,
  RelinquishRunway,
  RequestLanding,
  RequestTakeoff
}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.collection.mutable

class TowerActor extends Actor with ActorLogging {
  var runwayBusy = false
  var overseer: ActorRef = _
  var aircraftOnRunway: Option[ActorRef] = None
  val waitingAircraft: mutable.Queue[(ActorRef, AircraftActivity)] =
    new mutable.Queue()

  override def receive: Receive = {
    case InitializeTower =>
      log.debug("Tower initializing")
      overseer = sender()
    case RequestLanding if aircraftOnRunway.isEmpty =>
      log.debug(s"Assigning runway to ${sender().path} for landing")
      aircraftOnRunway = Some(sender())
      sender() ! LandingClearance
    case RequestLanding if aircraftOnRunway.isDefined =>
      log.debug(s"Queuing ${sender().path} as runway is occupied")
      waitingAircraft.enqueue((sender(), Land))
      sender() ! Circle
    case RequestTakeoff if aircraftOnRunway.isEmpty =>
      log.debug(s"Assigning runway to ${sender().path} for takeoff")
      aircraftOnRunway = Some(sender())
      sender() ! TakeoffClearance
    case RequestTakeoff if aircraftOnRunway.isDefined =>
      log.debug(s"Queuing ${sender().path} as runway is occupied")
      waitingAircraft.enqueue((sender(), Takeoff))
      sender() ! HoldOnTaxiway
    case RelinquishRunway =>
      log.debug(s"${sender().path} has relinquished runway")
      if (waitingAircraft.isEmpty) {
        log.debug("No aircraft waiting for a runway")
        overseer ! RunwayEmpty
      }
      waitingAircraft.dequeue() match {
        case (airplaneRef, activity) =>
          activity match {
            case Land =>
              log.debug(
                s"${airplaneRef.path} is being assigned to runway for landing")
              airplaneRef ! LandingClearance
            case Takeoff =>
              log.debug(
                s"${airplaneRef.path} is being assigned to runway for takeoff")
              airplaneRef ! TakeoffClearance
          }
      }
  }
}

object TowerActor {
  case object RequestLanding
  case object RequestTakeoff
  case object RelinquishRunway
  case object InitializeTower
  val props: Props = Props[TowerActor]
}
