package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._

import scala.scalajs.js.annotation.JSExport


@JSExport
object Logo {

  val logotype = List(
    Square(magenta, 0, 0), Square(blue, 1, 0),
    Square(blue, 0, 1), Square(magenta, 1, 1))

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    implicit val ctx = canvas
      .getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    def run() = {
      val (h, w) = (canvas.height, canvas.width)
      draw(logotype, 0, 0, canvas.width/2, "0px")
    }

    dom.setInterval(() => run(), 50)
  }
}
