package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._
import ro.purecore.squarecomposer.InitialFigure._

import scala.scalajs.js.annotation.JSExport


@JSExport
object Transformation01 {

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    implicit val ctx = canvas
      .getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    def run() = {
      val (h, w) = (canvas.height, canvas.width)

      val allPaintedBlue = initialFigure
        .map { cube =>
          if (cube.color == magenta) Square(blue, cube.x, cube.y)
          else cube }
      draw(allPaintedBlue, 0, 0)
    }

    dom.setInterval(() => run(), 50)
  }
}