package net.tpa.models

import java.time.LocalDateTime

import reactivemongo.bson.BSONObjectID

case class Project(id: String = BSONObjectID.generate.stringify, name:String, startDate: LocalDateTime, dueDate:LocalDateTime, completed: Boolean, budget: Int)



