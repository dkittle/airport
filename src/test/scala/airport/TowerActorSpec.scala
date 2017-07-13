package airport

import airport.ActivityDetails._
import airport.AirplaneActor.{LandingClearance, TakeoffClearance}
import airport.TowerActor.{InitializeTower, RequestLanding, RequestTakeoff}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, FeatureSpecLike, GivenWhenThen, Matchers}

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
    scenario("Expect to be told to hold on taxi when requesting a takeoff and runway is occupied") {
      Given("a tower actor and another aircraft already on runway")
      val actorRef = TestActorRef(new TowerActor)
      actorRef ! InitializeTower
      val airplane1 = TestActorRef(new AirplaneActor(actorRef, ActivityDetails("123", Takeoff, 20, 0)), "123")
      When("a second airplane wants to take off")
      val airplane2 = TestActorRef(new AirplaneActor(actorRef, ActivityDetails("456", Takeoff, 20, 0)), "456")
      Then("the tower should hold the second flight in a queue")
      val queue = actorRef.underlyingActor.waitingAircraft
      assert(queue.nonEmpty)
      assert(queue.dequeue._1.path.name.endsWith("456"))
      airplane1.stop()
      airplane2.stop()
      actorRef.stop()
    }
    scenario("Expect to be told to circle when requesting a landing and runway is occupied") {
      Given("a tower actor and another aircraft already on runway")
      val actorRef = TestActorRef(new TowerActor)
      actorRef ! InitializeTower
      val airplane1 = TestActorRef(new AirplaneActor(actorRef, ActivityDetails("123", Land, 20, 0)), "123")
      When("a second airplane wants to land")
      val airplane2 = TestActorRef(new AirplaneActor(actorRef, ActivityDetails("456", Land, 20, 0)), "456")
      Then("the tower should hold the second flight in a queue")
      val queue = actorRef.underlyingActor.waitingAircraft
      assert(queue.nonEmpty)
      assert(queue.dequeue._1.path.name.endsWith("456"))
      airplane1.stop()
      airplane2.stop()
      actorRef.stop()
    }
  }

}
