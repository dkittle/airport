package airport

import airport.AirplaneActor.{LandingClearance, TakeoffClearance}
import airport.TowerActor.{InitializeTower, RequestLanding, RequestTakeoff}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{
  BeforeAndAfterAll,
  FeatureSpecLike,
  GivenWhenThen,
  Matchers
}

import scala.concurrent.duration._

class TowerActorSpec
    extends TestKit(ActorSystem("test-system"))
    with ImplicitSender
    with FeatureSpecLike
    with GivenWhenThen
    with Matchers
    with BeforeAndAfterAll {

  private val timeout = 2.seconds

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  feature("Test the tower actor") {
    scenario("Expect to be assigned a runway when requesting a takeoff") {
      Given("a tower actor")
      val actorRef = TestActorRef(Props(classOf[TowerActor]))
      actorRef ! InitializeTower
      When("a RequestTakeoff message is sent")
      actorRef ! RequestTakeoff
      Then("the tower should send TakeoffClearance")
      expectMsg(timeout, TakeoffClearance)
      actorRef.stop()
    }
    scenario("Expect to be assigned a runway when requesting a landing") {
      Given("a tower actor")
      val actorRef = TestActorRef(Props(classOf[TowerActor]))
      actorRef ! InitializeTower
      When("a RequestLanding message is sent")
      actorRef ! RequestLanding
      Then("the tower should send LandingClearance")
      expectMsg(timeout, LandingClearance)
      actorRef.stop()
    }
    scenario(
      "Expect to be told to hold on taxi when requesting a takeoff and runway is occupied") {
      Given("a tower actor and another aircraft already on runway")
      val actorRef = TestActorRef(Props(classOf[TowerActor]))
      actorRef ! InitializeTower

      When("a RequestTakeoff message is sent")
      actorRef ! RequestTakeoff
      Then("the tower should send TakeoffClearance")
      expectMsg(timeout, TakeoffClearance)
      actorRef.stop()
    }
  }

}
