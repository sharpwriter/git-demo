package net.tpa.utils

import java.time.{LocalDateTime, ZoneOffset}

import scala.util.Random

object RND {

  private val _RND = scala.util.Random

  def randomInt(max:Int):Int = _RND.nextInt(max)

  def randomProduct(base:Int, multiplierMax:Int):Int = base * _RND.nextInt(multiplierMax)

  def randomFloat(max:Int): Float = max * _RND.nextFloat

  def randomString(length:Int):String = _RND.nextString(length)

  def randomDouble(max:Int): Double = max * _RND.nextDouble
}
