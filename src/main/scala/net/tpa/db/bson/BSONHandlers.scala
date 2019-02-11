package net.tpa.db.bson

import java.time.{Instant, LocalDateTime, ZoneOffset}

import reactivemongo.bson.{BSONDateTime, BSONHandler}

object BSONHandlers {

  implicit object BSONLocalDateTimeHandler extends BSONHandler[BSONDateTime, LocalDateTime] {
    private val zoneOffset = ZoneOffset.UTC

    override def read(time: BSONDateTime):LocalDateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(time.value), zoneOffset)

    override def write(time: LocalDateTime) = BSONDateTime(time.toInstant(zoneOffset).toEpochMilli)
  }

}
