package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._
import ro.purecore.squarecomposer.InitialFigure._
import ro.purecore.squarecomposer.Transformations._

import scala.scalajs.js.annotation.JSExport


@JSExport
object Transformation02 {

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    implicit val ctx = canvas
      .getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    def run() = {
      val (h, w) = (canvas.height, canvas.width)

      val onlyBlues =
        applyGravity(initialFigure.filter(_.color == blue))
      draw(onlyBlues, 0, 0)
    }

    dom.setInterval(() => run(), 50)
  }
}