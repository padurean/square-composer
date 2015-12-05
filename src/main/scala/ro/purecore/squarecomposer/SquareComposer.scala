package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._

import scala.language.postfixOps
import scala.scalajs.js.annotation.JSExport


@JSExport
object SquareComposer {

  val orange: Color = "#b35900"
  val yellow: Color = "#facd22"
  val blue: Color = "#4e7398"
  val brown: Color = "#663300"

  @JSExport
  def main(logoCanvas: html.Canvas, mainDiv: html.Div): Unit = {

    def run() = {

      val logotype = List(
        Square(blue,   0, 0), Square(orange, 1, 0),
        Square(orange, 0, 1), Square(blue,   1, 1))
      draw(logotype, 0, 0, logoCanvas.width/2, "0px")(logoCanvas.getContext2D)

      val initialFigure0 = List(
        Square(blue,   0, 0),                                                                                     Square(blue,   5, 0),
        Square(orange, 0, 1), Square(blue,   1, 1),                                         Square(blue,   4, 1), Square(orange, 5, 1),
        Square(orange, 0, 2), Square(orange, 1, 2), Square(blue, 2, 2), Square(blue, 3, 2), Square(orange, 4, 2), Square(orange, 5, 2))
      val initialFigure1 = List(
        Square(orange, 0, 1), Square(blue,   1, 1), Square(blue,  2, 1), Square(brown, 3, 1),
        Square(orange, 0, 2), Square(orange, 1, 2), Square(brown, 2, 2), Square(brown, 3, 2))
      val initialFigure2 = List(
        Square(orange,  0, 0),                                           Square(orange, 3, 0),
        Square(blue,    0, 1), Square(brown, 1, 1), Square(brown, 2, 1), Square(blue,   3, 1),
        Square(blue,    0, 2), Square(blue,  1, 2), Square(blue,  2, 2), Square(blue,   3, 2))
      val initialFigure3 = List(
        Square(brown,  0, 2), Square(orange, 1, 2), Square(orange, 2, 2),
        Square(blue,   3, 2), Square(blue,   4, 2), Square(blue,   5, 2),
        Square(orange, 6, 2), Square(orange, 7, 2), Square(brown,  8, 2))

      Seq(

        Transformation(
          uid = "0.1",
          name = "Transformation",
          input = initialFigure0,
          transformations = List((ss: List[Square]) => ss.map(_.copy(color = blue))),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "0.2",
          name = "Rejection",
          input = initialFigure0,
          transformations = List((ss: List[Square]) => ss.filter(_.color == blue).compactDown),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "0.3",
          name = "Composition",
          input = initialFigure0,
          transformations = List(
            (ss: List[Square]) =>
              ss.filter(_.color == blue).compactDown.stackAbove(List(orange))),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "0.4",
          name = "Spanish flag",
          input = initialFigure0,
          transformations = List(
            (ss: List[Square]) =>
              ss.filter(_.color == blue).compactDown.stackAbove(List(orange, blue))),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "1.1",
          name = "Mercury",
          input = initialFigure1,
          transformations = List(
            (ss: List[Square]) => ss
              .map(square =>
                if(square.color == brown) square.copy(color = orange)
                else square)
              .stackAbove(List(orange))),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "1.2",
          name = "Venus",
          input = initialFigure1,
          transformations = List(
            (ss: List[Square]) => ss
              .groupBy(_.x)
              .filter(_._2.exists(_.color == orange))
              .flatMap(_._2)
              .map(square =>
                if(square.color == blue) square.copy(color = orange)
                else square)
              .toList),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "1.3",
          name = "Earth",
          input = initialFigure2,
          transformations = List(
            (ss: List[Square]) => ss
              .filter(_.color == blue)
              .moveVertically(-1)
              .stackBelow(List(brown))),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "1.4",
          name = "Mars",
          input = initialFigure1,
          transformations = List(
            (ss: List[Square]) => ss.map(square => square.copy(color = orange))),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown")),

        Transformation(
          uid = "2.1",
          name = "Bricklayer",
          input = initialFigure3,
          transformations = List(
            (ss: List[Square]) => ss
              .stackAbovePreviousIfSameColor()
              .map(s => if (s.color == blue) s.copy(color = brown) else s)),
          sourceCodes = List(
            """def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
              |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))"""
              .stripMargin),
          functions = List("compactLeft", "compactDown"))
      ).foreach(renderFirstTransformationAndDraw(mainDiv, _))
    }

    dom.setTimeout(() => run(), 50) }
}