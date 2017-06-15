package airport

import upickle.default._

trait AircraftActivity
case object Land extends AircraftActivity
case object Takeoff extends AircraftActivity
case object Noop extends AircraftActivity

case class ActivityDetails(flight: String,
                           activity: AircraftActivity,
                           duration: Int,
                           delay: Int)

// Takeoff and landing times by aircraft model
//  737      45 95
//  757-200  32 105
//  757-300  37 105
//  767      55 110
