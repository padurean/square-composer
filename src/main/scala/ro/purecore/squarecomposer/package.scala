package ro.purecore

import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, html}

import scala.language.implicitConversions

import scala.collection.mutable

package object squarecomposer {

  type Color = String

  val magenta: Color = "#ec7600"
  val yellow: Color = "#facd22"
  val blue: Color = "#678cb1"
  val brown: Color = "#804000"

  case class Square(color: Color, x: Int, y: Int)

  implicit def richCanvas(canvas: html.Canvas): RichCanvas =
    new RichCanvas(canvas)

  implicit def richListOfSquares(squares: List[Square]): Transformations =
    new Transformations(squares)

  class RichCanvas(val canvas: html.Canvas) extends AnyVal {
    def getContext2D: CanvasRenderingContext2D =
      canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D] }

  class Transformations(val squares: List[Square]) extends AnyVal {

    def compactDown: List[Square] = {
      val maxY = squares.maxBy(_.y).y
      squares
        .groupBy(_.x)
        .flatMap { case (x, havingSameX) =>
          val sortedByY = havingSameX.sortBy(_.y)
          val currMaxY = sortedByY.last.y
          for (square <- sortedByY)
            yield square.copy(y = square.y + (maxY - currMaxY)) }
        .toList }

    // removes the gaps on x axis, pushing cols to the left (decreasing x)
    def compactLeft(squares: List[Square]): List[Square] =
      squares
        .groupBy(_.x)
        .values.toList
        .sortBy(_.head.x)
        .foldLeft(List.empty[List[Square]]) { case (acc, havingSameX) =>
          if (acc.nonEmpty && havingSameX.head.x > acc.last.head.x + 1)
            acc :+ havingSameX.map(_.copy(x = acc.last.head.x + 1))
          else acc :+ havingSameX }
        .flatten

    def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
      squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))

    def stackAbove(colors: List[Color]): List[Square] = {
      assert(
        squares.forall(s => s.x >= 0 || s.y >= 0),
        "Stacking doesn't work with negative coords!")

      val minY = squares.minBy(_.y).y
      val minYRequired = colors.size
      val overlapY = if (minYRequired > minY) minYRequired - minY else 0
      val liftedSquares = if (overlapY > 0) moveVertically(overlapY) else squares
      val bottomSquares = liftedSquares.groupBy(_.x).map(_._2.minBy(_.y)).toList

      val stacked = colors.flatMap { c =>
        val deltaY = colors.indexOf(c) + 1
        bottomSquares.moveVertically(-deltaY, Some(c)) }
      stacked ++ liftedSquares }

    def stackBelow(colors: List[Color]): List[Square] = {
      assert(
        squares.forall(s => s.x >= 0 || s.y >= 0),
        "Stacking doesn't work with negative coords!")

      val topSquares = squares.groupBy(_.x).map(_._2.maxBy(_.y)).toList

      val stacked = colors.flatMap { c =>
        val deltaY = colors.indexOf(c) + 1
        topSquares.moveVertically(deltaY, Some(c)) }
      stacked ++ squares }

    def stackAbovePreviousIfSameColor(): List[Square] = {
      assert(
        squares.groupBy(_.y).size == 1,
        "Stacking same color only supports squares with same Y value")

      val stackedByColor = squares
        .sortBy(_.x)
        .foldLeft(List.empty[Square]) { case (acc, square) =>
          if (acc.nonEmpty && acc.last.color == square.color)
            acc :+ square.copy(x = acc.last.x, y = acc.last.y-1)
          else acc :+ square }

      compactLeft(stackedByColor) }
  }

  object Effects {
    def draw (
      squares: List[Square],
      dx: Int,
      dy: Int,
      s: Int = 30,
      strokeStyle: String = "3px #333333")(
      implicit ctx: dom.CanvasRenderingContext2D)
    : Unit = {

      ctx.strokeStyle = strokeStyle
      for(square <- squares.reverse) {
        ctx.strokeRect(square.x * s + dx, square.y * s + dy, s, s)

        ctx.fillStyle = square.color
        ctx.fillRect(square.x * s + dx, square.y * s + dy, s, s) } }

    def drawBg (
      squares: List[Square],
      dx: Int,
      dy: Int,
      s: Int = 30,
      color: Color = "#2a2a2a",
      strokeStyle: String = "3px #333333")(
      implicit ctx: dom.CanvasRenderingContext2D)
    : Unit = {
//      TODO OGG: remove this function
//      val maxX = squares.maxBy(_.x).x
//      val maxY = squares.maxBy(_.y).y
//
//      ctx.strokeStyle = strokeStyle
//      for (x <- 0 to maxX; y <- 0 to maxY) {
//        ctx.strokeRect(x * s + dx, y * s + dy, s, s)
//
//        ctx.fillStyle = color
//        ctx.fillRect(x * s + dx, y * s + dy, s, s) }
    }

    def fillText
      (text: String, x: Double, y: Double)
      (implicit ctx: dom.CanvasRenderingContext2D)
    : Unit = {
      ctx.fillStyle = "#494949"
      ctx.font = "16px sans-serif"
      ctx.fillText(text, x, y) }

    def clear(implicit ctx: dom.CanvasRenderingContext2D): Unit = {
      ctx.fillStyle = "black"
      ctx.fillRect(0, 0, 255, 255) }
  }

}
