lazy val akkaVersion = "2.5.20"

lazy val root = project.in(file(".")).settings(
  scalaVersion := "2.12.6",
  name := "akka-quickstart-scala",
  version := "1.0",
  organization := "net.tpa",
    libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion, 
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
    "com.typesafe.akka" %% "akka-slf4j" % "2.5.20",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  ),
  fork in run := true
)







