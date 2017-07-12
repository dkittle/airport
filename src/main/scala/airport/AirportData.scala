package airport

import scala.language.implicitConversions
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait AircraftActivity
case object Land extends AircraftActivity
case object Takeoff extends AircraftActivity
case object Noop extends AircraftActivity

case class ActivityDetails(flight: String,
                           activity: String,
                           duration: Int,
                           delay: Int)

object ActivityDetails {
  implicit def string2aircraftActivity(s: String): AircraftActivity = s match {
    case "Land" => Land
    case "Takeoff" => Takeoff
    case _ => Noop
  }
  implicit def aircraftActivity2String(a: AircraftActivity): String = a match {
    case Land => Land.toString
    case Takeoff => Takeoff.toString
    case Noop => Noop.toString
  }
}
trait JsonProtocol extends DefaultJsonProtocol {
  implicit val activityDetailsFormat: RootJsonFormat[ActivityDetails] =
    jsonFormat4(ActivityDetails.apply)
}

// Takeoff and landing times by aircraft model
//  737      45 95
//  757-200  32 105
//  757-300  37 105
//  767      55 110
