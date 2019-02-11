package net.tpa.db.bson

import java.time.LocalDateTime

import net.tpa.models.Project
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}
import BSONHandlers._

import scala.util.{Failure, Success}

object BSONProject extends BSONDocumentReader[Project] with BSONDocumentWriter[Project] {

  override def write(t: Project): BSONDocument = {
    BSONDocument(
      "_id" -> BSONObjectID.parse(t.id).get,
      "projectName" -> t.name,
      "projectStartDate" -> t.startDate,
      "projectDueDate" -> t.dueDate,
      "isCompleted" -> t.completed,
      "expectedBudget" -> t.budget
    )
  }


  override def read(bson: BSONDocument): Project = {

    val t = for {
      id <- bson.getAsTry[BSONObjectID]("_id").map(_.stringify)
      name <- bson.getAsTry[String]("projectName")
      startDate <- bson.getAsTry[LocalDateTime]("projectStartDate")
      dueDate <- bson.getAsTry[LocalDateTime]("projectDueDate")
      completed <- bson.getAsTry[Boolean]("isCompleted")
      budget <- bson.getAsTry[Int]("expectedBudget")
    } yield Project(id = id, name = name, startDate = startDate, dueDate = dueDate, completed = completed, budget = budget)

    t match {
      case Success(project) => project
      case Failure(exc) => throw new Exception(s"error while reading from Mongo : ${exc.getMessage}")
    }
  }

}

