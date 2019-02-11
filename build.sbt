import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

lazy val akkaVersion = "2.5.20"
lazy val enumeratumReactiveMongoVersion = "1.5.13"
lazy val root = project.in(file(".")).enablePlugins(MultiJvmPlugin).settings(
  scalaVersion := "2.12.6",
  name := "akka-quickstart-scala",
  version := "1.0",
  organization := "net.tpa",
    libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion, 
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
    "com.typesafe.akka" %% "akka-slf4j" % "2.5.20",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2", 
    "org.reactivemongo" %% "reactivemongo" % "0.12.7",
    "com.beachape" %% "enumeratum-reactivemongo-bson" % enumeratumReactiveMongoVersion
  ),
  fork in run := true
).configs(MultiJvm)







