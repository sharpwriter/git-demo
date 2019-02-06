package net.tpa

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory
import net.tpa.actors.SimpleClusterListener
import net.tpa.actors.Consumer
import net.tpa.actors.Consumer._
import net.tpa.actors.PointToPointChannel
object ClusterQuickStart {

  def main(args: Array[String]): Unit = {

    if(args.isEmpty) {
      // handle forgetfullness
      launchAtPort(None)
    }
    else {
      launchAtPort(Some(args(0)))
    }

  }

  private def launchAtPort(port:Option[String]) = {


    val system = port match {
      case Some(port) =>
        val config = ConfigFactory.parseString(
          s"""
             |akka.remote.netty.tcp.port = $port
      """.stripMargin).withFallback(ConfigFactory.load())
        ActorSystem("Vishnu", config)
      case None => ActorSystem("Vishnu")
    }

    val listener = system.actorOf(SimpleClusterListener.props)

    def createSingleton(): ActorRef = {
      //#create-singleton-manager
      system.actorOf(
        ClusterSingletonManager.props(
          singletonProps = Props(classOf[Consumer], queue, testActor),
          terminationMessage = End,
          settings = ClusterSingletonManagerSettings(system).withRole("worker")),
        name = "consumer")
      //#create-singleton-manager
    }

    def createSingletonProxy(): ActorRef = {
      //#create-singleton-proxy
      val proxy = system.actorOf(
        ClusterSingletonProxy.props(
          singletonManagerPath = "/user/consumer",
          settings = ClusterSingletonProxySettings(system).withRole("worker")),
        name = "consumerProxy")
      //#create-singleton-proxy
      proxy
    }

  }

}
