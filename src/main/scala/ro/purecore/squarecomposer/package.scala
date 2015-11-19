package ro.purecore

import org.scalajs.dom

package object squarecomposer {

  type Color = String

  val blue: Color = "#3399cc"
  val magenta: Color = "#cd00cd"

  case class Square(color: Color, x: Int, y: Int)

  object Transformations {
    def applyGravity(squares: List[Square]) = {
      val maxY = squares.maxBy(_.y).y
      squares
        .groupBy(_.x)
        .flatMap { case (x, havingSameX) =>
          val sortedByY = havingSameX.sortBy(_.y)
          val currMaxY = sortedByY.last.y
          for (i <- sortedByY.indices) yield {
            val square = sortedByY(i)
            Square(square.color, square.x, square.y + (maxY - currMaxY))
          }
        }
        .toList
    }
  }

  object Effects {
    def draw
      (squares: List[Square], dx: Int, dy: Int)
      (implicit ctx: dom.CanvasRenderingContext2D)
    : Unit = {

      val s = 30
      for(square <- squares.reverse) {
        ctx.strokeStyle = "3px #333333"
        ctx.strokeRect(square.x * s + dx, square.y * s + dy, s, s)

        ctx.fillStyle = square.color
        ctx.fillRect(square.x * s + dx, square.y * s + dy, s, s)
      }
    }

    def fillText
      (text: String, x: Double, y: Double)
      (implicit ctx: dom.CanvasRenderingContext2D)
    : Unit = {
      ctx.fillStyle = "#494949"
      ctx.font = "16px sans-serif"
      ctx.fillText(text, x, y)
    }

    def clear(implicit ctx: dom.CanvasRenderingContext2D): Unit = {
      ctx.fillStyle = "black"
      ctx.fillRect(0, 0, 255, 255)
    }
  }
}
