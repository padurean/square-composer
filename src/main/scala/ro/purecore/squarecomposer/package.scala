package ro.purecore

import org.scalajs.dom

package object squarecomposer {

  type Color = String

  val blue: Color = "#3399cc"
  val magenta: Color = "#cd00cd"

  case class Square(color: Color, x: Int, y: Int)

  // Think upside down, as 0, 0 means top-left corner of the screen,
  // so X grows to right, as expected, but Y grows down, as not-expected :)
  // hence your perceived "up", is actually "down" in computations :P
  object Transformations {

    def applyGravity(squares: List[Square]): List[Square] = {
      val maxY = squares.maxBy(_.y).y
      squares
        .groupBy(_.x)
        .flatMap { case (x, havingSameX) =>
          val sortedByY = havingSameX.sortBy(_.y)
          val currMaxY = sortedByY.last.y
          for (i <- sortedByY.indices) yield {
            val square = sortedByY(i)
            square.copy(y = square.y + (maxY - currMaxY)) } }
        .toList }

    def stack(color: Color, squares: List[Square]): List[Square] = {
      val minY = squares.minBy(_.y).y
      val squs =
        // if lowest square is on or below y=0, shift all up so that lowest is at y=1
        // so that there is room to "stack" a row below (remember y grows downwards)
        if (minY <= 0) {
          val deltaY = -minY + 1
          squares.map(s => s.copy(y = s.y + deltaY)) }
        else squares
      val stacked = squs.map(s => s.copy(color = color, y = s.y - 1))
      stacked ++ squares }
  }

  object Effects {
    def draw
      (squares: List[Square], dx: Int, dy: Int, s: Int = 30, strokeStyle: String = "3px #333333")
      (implicit ctx: dom.CanvasRenderingContext2D)
    : Unit = {

      for(square <- squares.reverse) {
        ctx.strokeStyle = strokeStyle
        ctx.strokeRect(square.x * s + dx, square.y * s + dy, s, s)

        ctx.fillStyle = square.color
        ctx.fillRect(square.x * s + dx, square.y * s + dy, s, s) } }

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
