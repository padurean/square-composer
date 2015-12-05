package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.html
import ro.purecore.squarecomposer.Effects._

import scala.language.postfixOps
import scala.scalajs.js.annotation.JSExport


@JSExport
object SquareComposer {

  @JSExport
  def main(logoCanvas: html.Canvas, mainDiv: html.Div): Unit = {

    def run() = {

      draw(
        Transformations.logotype,
        0,
        0,
        logoCanvas.width/2,
        "0px")(
        logoCanvas.getContext2D)

      Transformations.definitions
        .foreach(renderFirstTransformationAndDraw(mainDiv, _))

      val commonCode =
      """type Color = String
        |
        |val magenta: Color = "#da0b56"
        |val yellow: Color = "#ccbd33"
        |val blue: Color = "#8d51fb"
        |val brown: Color = "#610527"
        |
        |case class Square(color: Color, x: Int, y: Int)
        |
        |implicit def richCanvas(canvas: html.Canvas): RichCanvas =
        |  new RichCanvas(canvas)
        |
        |implicit def richListOfSquares(squares: List[Square]): Transformations =
        |  new Transformations(squares)
        |
        |class RichCanvas(val canvas: html.Canvas) extends AnyVal {
        |  def getContext2D: CanvasRenderingContext2D =
        |    canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D] }
        |<a name="def-compact-down" class="nocode"></a>
        |def compactDown: List[Square] = {
        |val maxY = squares.maxBy(_.y).y
        |squares
        |  .groupBy(_.x)
        |  .flatMap { case (x, havingSameX) =>
        |    val sortedByY = havingSameX.sortBy(_.y)
        |    val currMaxY = sortedByY.last.y
        |    for (square <- sortedByY)
        |      yield square.copy(y = square.y + (maxY - currMaxY)) }
        |  .toList }
        |<a name="def-compact-left" class="nocode"></a>
        |def compactLeft(squares: List[Square]): List[Square] =
        |  squares
        |    .groupBy(_.x)
        |    .values.toList
        |    .sortBy(_.head.x)
        |    .foldLeft(List.empty[List[Square]]) { case (acc, havingSameX) =>
        |      if (acc.nonEmpty && havingSameX.head.x > acc.last.head.x + 1)
        |        acc :+ havingSameX.map(_.copy(x = acc.last.head.x + 1))
        |      else acc :+ havingSameX }
        |    .flatten
        |<a name="def-move-vertically" class="nocode"></a>
        |def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
        |  squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))
        |<a name="def-stack-above" class="nocode"></a>
        |def stackAbove(colors: List[Color]): List[Square] = {
        |  assert(
        |    squares.forall(s => s.x >= 0 || s.y >= 0),
        |    "Stacking doesn't work with negative coords!")
        |
        |  val minY = squares.minBy(_.y).y
        |  val minYRequired = colors.size
        |  val overlapY = if (minYRequired > minY) minYRequired - minY else 0
        |  val liftedSquares = if (overlapY > 0) moveVertically(overlapY) else squares
        |  val bottomSquares = liftedSquares.groupBy(_.x).map(_._2.minBy(_.y)).toList
        |
        |  val stacked = colors.flatMap { c =>
        |    val deltaY = colors.indexOf(c) + 1
        |    bottomSquares.moveVertically(-deltaY, Some(c)) }
        |
        |  stacked ++ liftedSquares }
        |<a name="def-stack-below" class="nocode"></a>
        |def stackBelow(colors: List[Color]): List[Square] = {
        |  assert(
        |    squares.forall(s => s.x >= 0 || s.y >= 0),
        |    "Stacking doesn't work with negative coords!")
        |
        |  val topSquares = squares.groupBy(_.x).map(_._2.maxBy(_.y)).toList
        |
        |  val stacked = colors.flatMap { c =>
        |    val deltaY = colors.indexOf(c) + 1
        |    topSquares.moveVertically(deltaY, Some(c)) }
        |
        |  stacked ++ squares }
        |<a name="def-stack-above-previous-if-same-color" class="nocode"></a>
        |def stackAbovePreviousIfSameColor(): List[Square] = {
        |  assert(
        |    squares.groupBy(_.y).size == 1,
        |    "Stacking same color only supports squares with same Y value")
        |
        |  val stackedByColor = squares
        |    .sortBy(_.x)
        |    .foldLeft(List.empty[Square]) { case (acc, square) =>
        |      if (acc.nonEmpty && acc.last.color == square.color)
        |        acc :+ square.copy(x = acc.last.x, y = acc.last.y-1)
        |      else acc :+ square }
        |
        |  compactLeft(stackedByColor) }"""
        .stripMargin

      mainDiv.appendChild(
        DomRenderer.renderCommonCode("Appendix:", "Common Code", commonCode))
    }

    dom.setTimeout(() => run(), 50) }
}
