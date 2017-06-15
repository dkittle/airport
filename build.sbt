name := "airport"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.lihaoyi" %% "upickle" % "0.4.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
