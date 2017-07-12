package airport

import airport.ActivityDetails._
import airport.TowerActor.{RequestLanding, RequestTakeoff}
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{
  BeforeAndAfterAll,
  FeatureSpecLike,
  GivenWhenThen,
  Matchers
}

import scala.concurrent.duration._

class AirplaneActorSpec
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

  feature("Test the airplane actor") {
    scenario(
      "Expect to receive a landing request when it's time for an airplane to land") {
      Given("an aircraft actor")
      val airplane = TestActorRef(
        AirplaneActor.props(self, ActivityDetails("123", Noop, 0, 0)))
      When("a Land message is sent")
      airplane ! Land
      Then("the tower should receive a landing request")
      expectMsg(timeout, RequestLanding)
      airplane.stop()
    }
    scenario(
      "Expect to receive a takeoff request when it's time for an airplane to takeoff") {
      Given("an aircraft actor")
      val airplane = TestActorRef(
        AirplaneActor.props(self, ActivityDetails("123", Noop, 0, 0)))
      When("a Takeoff message is sent")
      airplane ! Takeoff
      Then("the tower should receive a takeoff request")
      expectMsg(timeout, RequestTakeoff)
      airplane.stop()
    }
  }

}
