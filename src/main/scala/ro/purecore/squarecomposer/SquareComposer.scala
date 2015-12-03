package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._

import scala.scalajs.js.annotation.JSExport


@JSExport
object SquareComposer {

  @JSExport
  def main(
    logoCanvas: html.Canvas,
    initialFigure01Canvas: html.Canvas,
    transformation01Canvas: html.Canvas,
    initialFigure02Canvas: html.Canvas,
    transformation02Canvas: html.Canvas,
    initialFigure03Canvas: html.Canvas,
    transformation03Canvas: html.Canvas,
    initialFigure04Canvas: html.Canvas,
    transformation04Canvas: html.Canvas,
    initialFigure11Canvas: html.Canvas,
    transformation11Canvas: html.Canvas,
    initialFigure12Canvas: html.Canvas,
    transformation12Canvas: html.Canvas,
    initialFigure13Canvas: html.Canvas,
    transformation13Canvas: html.Canvas,
    initialFigure14Canvas: html.Canvas,
    transformation14Canvas: html.Canvas,
    initialFigure21Canvas: html.Canvas,
    transformation21Canvas: html.Canvas): Unit = {

    val canvasAlpha = 1.00
    initialFigure01Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation01Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure02Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation02Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure03Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation03Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure04Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation04Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure11Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation11Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure12Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation12Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure13Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation13Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure14Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation14Canvas.getContext2D.globalAlpha = canvasAlpha
    initialFigure21Canvas.getContext2D.globalAlpha = canvasAlpha
    transformation21Canvas.getContext2D.globalAlpha = canvasAlpha

    def run() = {

      val logotype = List(
        Square(blue, 0, 0), Square(magenta, 1, 0),
        Square(magenta, 0, 1), Square(blue, 1, 1))

      val initialFigure0 = List(
        Square(blue,    0, 0),                                                                                       Square(blue   , 5, 0),
        Square(magenta, 0, 1), Square(blue,   1, 1),                                          Square(blue,    4, 1), Square(magenta, 5, 1),
        Square(magenta, 0, 2), Square(magenta, 1, 2), Square(blue, 2, 2), Square(blue, 3, 2), Square(magenta, 4, 2), Square(magenta, 5, 2))

      // Logo
      draw(logotype, 0, 0, logoCanvas.width/2, "0px")(logoCanvas.getContext2D)

      // T 01
      drawBg(initialFigure0, 0, 0)(initialFigure01Canvas.getContext2D)
      draw(initialFigure0, 0, 0)(initialFigure01Canvas.getContext2D)
      val t01 = initialFigure0.map(_.copy(color = blue))
      draw(t01, 0, 0)(transformation01Canvas.getContext2D)

      // T 02
      draw(initialFigure0, 0, 0)(initialFigure02Canvas.getContext2D)
      val t02 = initialFigure0.filter(_.color == blue).compactDown
      draw(t02, 0, 0)(transformation02Canvas.getContext2D)

      // T 03
      draw(initialFigure0, 0, 0)(initialFigure03Canvas.getContext2D)
      val t03 = initialFigure0
        .filter(_.color == blue)
        .compactDown
        .stackAbove(List(magenta))
      draw(t03, 0, 0)(transformation03Canvas.getContext2D)

      // T 04
      draw(initialFigure0, 0, 0)(initialFigure04Canvas.getContext2D)
      val t04 = initialFigure0
        .filter(_.color == blue)
        .compactDown
        .stackAbove(List(magenta, blue))
      draw(t04, 0, 0)(transformation04Canvas.getContext2D)

      val initialFigure1 = List(
        Square(magenta, 0, 1), Square(yellow, 1, 1), Square(yellow, 2, 1), Square(blue, 3, 1),
        Square(magenta, 0, 2), Square(magenta, 1, 2), Square(blue, 2, 2), Square(blue, 3, 2))

      // T 11
      draw(initialFigure1, 0, 0)(initialFigure11Canvas.getContext2D)
      val t11 = initialFigure1
        .map(square =>
          if(square.color == blue) square.copy(color = magenta)
          else square)
        .stackAbove(List(magenta))
      draw(t11, 0, 0)(transformation11Canvas.getContext2D)

      // T 12
      draw(initialFigure1, 0, 0)(initialFigure12Canvas.getContext2D)
      val t12 = initialFigure1
        .groupBy(_.x)
        .filter(_._2.exists(_.color == magenta))
        .flatMap(_._2)
        .map(square =>
          if(square.color == yellow) square.copy(color = magenta)
          else square)
        .toList
      draw(t12, 0, 0)(transformation12Canvas.getContext2D)

      val initialFigure2 = List(
        Square(yellow, 0, 0),                                               Square(yellow, 3, 0),
        Square(blue,   0, 1), Square(magenta, 1, 1), Square(magenta, 2, 1), Square(blue,   3, 1),
        Square(blue,   0, 2), Square(blue,    1, 2), Square(blue,    2, 2), Square(blue,   3, 2))

      // T 13
      draw(initialFigure2, 0, 0)(initialFigure13Canvas.getContext2D)
      val t13 = initialFigure2
        .filter(_.color == blue)
        .moveVertically(-1)
        .stackBelow(List(magenta))
      draw(t13, 0, 0)(transformation13Canvas.getContext2D)

      // T 14
      draw(initialFigure1, 0, 0)(initialFigure14Canvas.getContext2D)
      val t14 = initialFigure1.map(square => square.copy(color = magenta))
      draw(t14, 0, 0)(transformation14Canvas.getContext2D)

      val initialFigure3 = List(
        Square(brown, 0, 2), Square(magenta, 1, 2), Square(magenta, 2, 2),
        Square(yellow, 3, 2), Square(yellow, 4, 2), Square(yellow, 5, 2),
        Square(magenta, 6, 2), Square(magenta, 7, 2), Square(brown, 8, 2))

      // T 21
      draw(initialFigure3, 0, 0)(initialFigure21Canvas.getContext2D)
      val t21 = initialFigure3
        .stackAbovePreviousIfSameColor()
        .map(s => if (s.color == yellow) s.copy(color = brown) else s)
      draw(t21, 0, 0)(transformation21Canvas.getContext2D)
    }

    dom.setTimeout(() => run(), 50) }
}
