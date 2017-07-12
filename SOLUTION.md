# Don's Solution

# Initialization
There is a main object `AirportMain` that gets the application going. It creates an HTTP server with two REST endpoints,
 one to create airplanes and another to end the simulation. `AirportMain` also spins up an `AirportSupervisorActor` 
 that will create the `TowerActor`.

The supervisor becomes an _overseer_ of the `TowerActor` when it initializes the `TowerActor`. When an `AirplaneActor` 
relinquishes a runway back to the `TowerActor` and there are no other airplanes waiting to get access to the runway, the 
`TowerActor` sends it's overseer a message that the runway is empty. We could likely end the simulation at this point.

Airplanes perform activities at discreet points in time. The `AirportSupervisorActor` instantiates `AirplaneActors` with
the details of the activity to perform and the timing around those activities (the delay in seconds before the activity 
starts, timed from the beginning of the simulation and the duration of the activity in seconds).


# Simulation Runtime
Airplanes schedule their activity to start a specific number of seconds after the simulation starts. The number of 
seconds is the `delay` property of `ActivityDetails`. They will attempt to perform their activity by sending the 
`TowerActor` a message requesting permission. The `TowerActor` will either grant the `AirplaneActor` permission to 
 perform the activity or will ask the airplane to `Circle` (if it's in the sky) or `HoldOnTaxiway` if the airplane is 
 seeking to take off. The `TowerActor` will queue waiting aircraft in a FIFO queue and will advise a waiting airplane 
 when the runway is free. If the runway frees up and there are no more aircraft in the queue, the `TowerActor` will send
 it's overseer an `EmptyRunway` messaging, allowing the overseer to end the simulation.
 
