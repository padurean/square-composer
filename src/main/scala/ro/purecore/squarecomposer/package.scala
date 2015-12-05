package ro.purecore

import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, html}

import scala.language.implicitConversions

package object squarecomposer {

  type Color = String

  case class Square(color: Color, x: Int, y: Int)

  class RichCanvas(val canvas: html.Canvas) extends AnyVal {
    def getContext2D: CanvasRenderingContext2D =
      canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D] }

  val defaultSize = 30

  implicit def richCanvas(canvas: html.Canvas): RichCanvas =
    new RichCanvas(canvas)
  implicit def richListOfSquares(squares: List[Square]): Transformations =
    new Transformations(squares)

  /**
    * Turn a string of format "FooBar" of "fooBar" into snake case "foo-bar"
    *
    * Note: snakify is not reversible
    *
    * @return the dash-separated string
    */
  def snakify(name : String) = name
    .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
    .replaceAll("([a-z\\d])([A-Z])", "$1-$2")
    .toLowerCase
}
