import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import net.tpa.actors.FactorialActor

import scala.concurrent.duration._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object FactorialsQuickstart extends App {

  import net.tpa.actors.FactorialActor._

  val system = ActorSystem("FactorialSystem")

  val fa = system.actorOf(FactorialActor.props)

  implicit val timeout: Timeout = 500.millis
  val myFuture : Future[FactorialResponse] = (fa ? FactorialRequest(4)).mapTo[FactorialResponse]

  implicit val ec: ExecutionContext = system.dispatcher
  myFuture.onComplete {
    case Success(value) =>
      println(value)
      system.terminate()
    case Failure(exception) =>
      println(exception.getMessage())
      system.terminate()
  }




}
