name := "airport"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= {
  val akkaV = "2.4.16"
  val akkaHttpV = "10.0.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % Test,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test"
  )
}
