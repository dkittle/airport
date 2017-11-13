# Air Miles Airport Exercise

## Purpose
The exercise will allow us to evaluate your approach to building an event driven system along with how you deal with areas of a system that have constraints.

## Overview
We’d like you to build a simulation of an airport that has one runway. Focus on the major components of the model. The goal is to build a system that allows planes to land and to take off, orchestrating planes so they do not crash into one another.

You do not need to account for the raising and lowering of landing gear. You don’t need to include passengers, flight attendants or ground crew in your solution. You don’t need to get into details like turning on the seatbelt sign. The simulation should work at the level of planes, runways and other major components of an airport.

Your system will be tested by having planes take off and land at the airport. We should be able to feed it data from the scenarios detailed below and see how your code manages the inputs through looking at your log output.

## Basic Toolset
We’d like you to
- Implement in a programming language you are comfortable with
- Implement your solution with discreet services that interact in an event driven way - this need not be separate projects or modules, but there should be a clear delineation between services in your code
- Create unit tests as appropriate
- Create integration tests, if you believe there is a need
- Put your code under source version control, if you are invited for an in-person followup, we'll continue with some work on the solution
- Build using a tool (mvn, sbt, make, etc), not simply by building and running code in an IDE
- Have a README explaining how to build and run your solution. It should also explain how to feed sample data to your solution (JSON file, curl a REST endpoint with JSON data, type console input, etc)
- Optionally use a continuous integration tool

## What We Will Evaluate
- Your solution should work
- We should be able to see the flow of control from the logging statements generated
- Your solution should be event driven - you do not need to use an external messaging system (RabbitMQ, Resque, ActiveMQ, SQS, etc) but you can. You can also build services that communicate using pub/sub communication, Akka actors or any other solution that passes typed messages between loosely coupled services.
- We’d like to see test coverage, where it seems important
- We'd like some way of sending the sample simulation data to your solution and see (via logging output) that your solution works.

## A few things to keep in mind
We’d like to look at your code without you walking us through it. We want to see a clear separation of services, a well defined set of models and be able to understand the types of events that cause state transitions in your solution.

## Sample Data
The sample data consists of a list of aircraft along with their arrivals or departures.
Flight: the aircraft flight number (should be included in your log output)
Activity: whether the aircraft is arriving or departing the airport
Duration: the number of seconds the aircraft needs in order to complete the activity
Delay: the number of seconds into the simulation when the aircraft asks for permission to take off or land

Example, the simulation starts running at exactly 2:03:00pm. At 2:04:03pm (63 seconds from the start), flight IS123 will request a takeoff that will last 45 seconds. At 2:04:15pm (75 seconds from the simulation start), flight IS788 will request a takeoff which will last 32 seconds.
 

    [
      {
        "flight":"IS123",
        "activity":"Departure",
        "duration":45,
        "delay":63
      },
      {
        "flight":"IS788",
        "activity":"Departure",
        "duration":32,
        "delay":75
      },
      {
        "flight":"IS456",
        "activity":"Arrival",
        "duration":105,
        "delay":78
      },
      {
        "flight":"IS321",
        "activity":"Departure",
        "duration":45,
        "delay":147
      },
      {
        "flight":"IS993",
        "activity":"Departure",
        "duration":37,
        "delay":221
      },
      {
        "flight":"IS623",
        "activity":"Departure",
        "duration":37,
        "delay":244
      },
      {
        "flight":"IS898",
        "activity":"Arrival",
        "duration":55,
        "delay":302
      },
      {
        "flight":"IS488",
        "activity":"Departure",
        "duration":32,
        "delay":46
      }
    ]

