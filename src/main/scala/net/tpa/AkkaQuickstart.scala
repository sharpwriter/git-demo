//#full-example
package net.tpa

import akka.actor.{ActorRef, ActorSystem}
import net.tpa.actors.{Greeter, Printer}

//#main-class
object AkkaQuickstart extends App {
  import net.tpa.actors.Greeter._
  import net.tpa.models._


  // i would like to have function that calculate the factorial of the length of a string

  lazy val myTask = Task(1L, "TASK TITLE", "TASK DESCRIPTION", New)
  println(myTask)

  // Create the 'helloAkka' actor system
  val system: ActorSystem = ActorSystem("DemoSystem")

  //#create-actors
  // Create the printer actor
  val printer: ActorRef = system.actorOf(Printer.props, "printerActor")

  // Create the 'greeter' actors
  val howdyGreeter: ActorRef =
    system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")
  val helloGreeter: ActorRef =
    system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
  val goodDayGreeter: ActorRef =
    system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")
  //#create-actors

  //#main-send-messages

  while(true)
    {
      howdyGreeter ! WhoToGreet("Akka")
      howdyGreeter ! Greet

      howdyGreeter ! WhoToGreet("Lightbend")
      howdyGreeter ! Greet

      helloGreeter ! WhoToGreet("Scala")
      helloGreeter ! Greet

      goodDayGreeter ! WhoToGreet("Play")
      goodDayGreeter ! Greet
      Thread.sleep(5000)
    }


  //#main-send-messages
}
//#main-class
//#full-example
