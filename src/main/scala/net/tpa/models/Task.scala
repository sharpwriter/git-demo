package net.tpa.models

case class Task(id: Long, title: String, description: String, status: TaskStatus)


sealed trait TaskStatus
case object New extends TaskStatus
case object Pending extends TaskStatus
case object Completed extends TaskStatus
