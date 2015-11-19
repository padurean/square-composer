package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._

import scala.scalajs.js.annotation.JSExport


@JSExport
object InitialFigure {

  val initialFigure = List(
    Square(magenta, 0, 2), Square(magenta, 1, 2), Square(blue, 2, 2), Square(blue, 3, 2), Square(magenta, 4, 2), Square(magenta, 5, 2),
    Square(magenta, 0, 1), Square(blue,    1, 1),                                         Square(blue,    4, 1), Square(magenta, 5, 1),
    Square(blue,    0, 0),                                                                                       Square(blue   , 5, 0))

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    implicit val ctx = canvas
      .getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    def run() = {
      val (h, w) = (canvas.height, canvas.width)
      draw(initialFigure, 0, 0)
    }

    dom.setInterval(() => run(), 50)
  }
}
