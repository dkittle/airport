package airport

import airport.AirplaneActor.{
  Circle,
  HoldOnTaxiway,
  LandingClearance,
  TakeoffClearance
}
import airport.TowerActor.{RelinquishRunway, RequestLanding, RequestTakeoff}
import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class AirplaneActor(tower: ActorRef, activity: ActivityDetails)
    extends Actor
    with ActorLogging {

  var scheduledTask: Cancellable =
    context.system.scheduler
      .scheduleOnce(activity.delay.seconds, self, activity.activity)
  log.debug(
    s"${activity.flight} will want to ${activity.activity} in ${activity.delay} seconds.")

  override def receive: Receive = {
    case Land =>
      log.debug(s"Flight ${activity.flight} requesting a runway to land")
      tower ! RequestLanding
    case Takeoff =>
      log.debug(s"Flight ${activity.flight} requesting a runway for takeoff")
      tower ! RequestTakeoff
    case Noop =>
      log.debug(s"${activity.flight} doing nothing")
    case Circle =>
      log.debug(s"${activity.flight} is circling until a runway is available")
    case HoldOnTaxiway =>
      log.debug(
        s"${activity.flight} is holding on a taxiway until a runway is available")
    case LandingClearance =>
      log.debug(s"Flight ${activity.flight} is landing")
      scheduledTask = relinquishRunWhenDone()
    case TakeoffClearance =>
      log.debug(s"Flight ${activity.flight} is taking off")
      scheduledTask = relinquishRunWhenDone()
  }

  def relinquishRunWhenDone(): Cancellable =
    context.system.scheduler
      .scheduleOnce(activity.duration.seconds, tower, RelinquishRunway)
}

object AirplaneActor {
  case object LandingClearance
  case object TakeoffClearance
  case object Circle
  case object HoldOnTaxiway

  def props(tower: ActorRef, activity: ActivityDetails): Props = {
    Props(classOf[AirplaneActor], tower, activity)
  }
}
