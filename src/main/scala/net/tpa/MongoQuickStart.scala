package net.tpa


import java.time.{LocalDateTime, ZoneOffset}

import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import net.tpa.models.Project
import net.tpa.db.bson.BSONImplicits._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{Cursor, DefaultDB, MongoConnection}

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
import net.tpa.utils.RND
import reactivemongo.api.commands.MultiBulkWriteResult
import reactivemongo.bson.BSONDocument


object MongoQuickStart {

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val startId:Int = args(0).toInt
    val numberOfProjectsToGenerate:Int = args(1).toInt

    def randomDateInFuture(from:LocalDateTime, to: LocalDateTime): LocalDateTime = {
      val diff = java.time.temporal.ChronoUnit.DAYS.between(from, to)
      LocalDateTime.ofInstant(from.plusDays(Random.nextInt(diff.toInt)).toInstant(ZoneOffset.UTC),ZoneOffset.UTC)
    }

    def randomBudget = RND.randomInt(10) * 50000

    def generateProjects(input:(Int,Int)) : List[Project] = {
      (startId._1 to total._2).toList.map(n => {
        val dueDate = LocalDateTime.now.plusDays(Random.nextInt(365))
        Project(name = s"Project #$n",
        startDate = randomDateInFuture(LocalDateTime.now(ZoneOffset.UTC), dueDate ),
        dueDate = dueDate,
        completed = false,
        budget = randomBudget
      )})
    }


    def storeSampleProjects(db:DefaultDB, collection: String) = {
      val generatedProjects: List[Project] = generateProjects((startId, startId + numberOfProjectsToGenerate - 1))
      val coll: BSONCollection = db.collection(collection)
      coll.insert(ordered = false).many(generatedProjects)
    }



    // set up a connection to the MongoDb
    val mongoDriver = new reactivemongo.api.MongoDriver
    // create a connection to the db using a URI string (only create one single connection in your application !)
    val config = ConfigFactory.load()
    val dbConfig = config.getConfig("mongo")
    val dbConnectionUri = dbConfig.getString("db.uri")
    //val uri:String  = "mongodb://localhost:27017/SampleCollections"
//    val dbAndresult : Future[(DefaultDB, MultiBulkWriteResult)]  = for {
//      uri <- Future.fromTry(MongoConnection.parseURI(dbConnectionUri))
//      con = mongoDriver.connection(uri)
//      dn <- Future(uri.db.get)
//      db <- con.database(dn)
//      result <- storeSampleProjects(db, "projects")
//    } yield (db, result)


    val dbAndResult : Future[(DefaultDB, MultiBulkWriteResult)] = Future.fromTry(MongoConnection.parseURI(dbConnectionUri)).flatMap(parsedUri =>
      mongoDriver.connection(parsedUri).database(parsedUri.db.get)
    ).flatMap(db => storeSampleProjects(db, "projects").map(result => (db, result)))

    dbAndResult.onComplete {
      case Success((db, result)) =>
        println(s"""
                   |Success : ${result.ok}\n
                   |Affected Records : ${result.totalN}\n
                   |Inserted : ${result.upserted.foreach(u => u._id + "\n" )}
       """.stripMargin)
       printCollection(db, "projects")
      case Failure(exception) => println(exception.getMessage)
    }

    def printCollection(db:DefaultDB, collection:String) = {
      val coll :BSONCollection = db.collection(collection)
      val myProjects : Future[List[Project]] = coll.find(BSONDocument.empty).cursor[Project]().collect[List](-1, Cursor.FailOnError[List[Project]]())
      myProjects.onComplete {
        case Success(list) =>
          list.map(item => println(item))
          shutdownDBConnection(db)
        case Failure(exc) =>
          println(exc.getMessage)
          shutdownDBConnection(db)
      }
    }


    def shutdownDBConnection(db:DefaultDB) = {
      try {
        implicit val timeout: FiniteDuration = 5.second
        db.connection.askClose().onComplete {
          case Success(_) => println("Connection to DB closed."); terminateMongoDriverActorSystem()
          case Failure(exc) => println(exc.getMessage); terminateMongoDriverActorSystem()
        }
      } catch {
        case e:Throwable => println(e.getMessage)
      }
    }

    def terminateMongoDriverActorSystem(): Unit = {
      mongoDriver.close()
    }

  }

}
