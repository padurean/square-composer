package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom._

object Effects {
  def renderFirstTransformationAndDraw(
    parentDiv: html.Div,
    t: Transformation)
  : Unit = {
    val (itemDiv, inCanvasId, outCanvasId) =
      DomRenderer.render(t)(t.transformations.head)
    parentDiv.appendChild(itemDiv)

    val (inCanvas, outCanvas) =
      fetchCanvases(parentDiv.lastElementChild, inCanvasId, outCanvasId)

    draw(t)(inCanvas.getContext2D, outCanvas.getContext2D) }

  def draw(
    t: Transformation)(
    in2DContext: CanvasRenderingContext2D,
    out2DContext: CanvasRenderingContext2D)
  : Unit = {
    drawBg(t.input, 0, 0)(in2DContext)
    draw(t.input, 0, 0)(in2DContext)
    drawBg(t.input, 0, 0)(out2DContext)
    draw(t.map(t.transformations.head), 0, 0)(out2DContext) }

  def draw (
    squares: List[Square],
    dx: Int,
    dy: Int,
    s: Int = defaultSize,
    strokeStyle: String = "3px #333333")(
    implicit ctx: dom.CanvasRenderingContext2D)
  : Unit = {

    ctx.strokeStyle = strokeStyle
    for(square <- squares.reverse) {
      ctx.strokeRect(square.x * s + dx, square.y * s + dy, s, s)

      ctx.fillStyle = square.color
      ctx.fillRect(square.x * s + dx, square.y * s + dy, s, s) } }

  def drawBg (
    squares: List[Square],
    dx: Int,
    dy: Int,
    s: Int = defaultSize,
    color: Color = "#333",
    strokeStyle: String = "3px #222")(
    implicit ctx: dom.CanvasRenderingContext2D)
  : Unit = {
    val maxX = squares.maxBy(_.x).x
    val maxY = squares.maxBy(_.y).y

    ctx.strokeStyle = strokeStyle
    for (x <- 0 to maxX; y <- 0 to maxY) {
      ctx.strokeRect(x * s + dx, y * s + dy, s, s)

      ctx.fillStyle = color
      ctx.fillRect(x * s + dx, y * s + dy, s, s) } }

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

  private def fetchCanvases(
    item: dom.Element,
    inCanvasId: String,
    outCanvasId: String)
  : (html.Canvas, html.Canvas) = {
    val canvasElems = item
      .getElementsByTagName("canvas")
    val canvases = List(
      canvasElems.item(0).asInstanceOf[html.Canvas],
      canvasElems.item(1).asInstanceOf[html.Canvas])
    (canvases.find(_.id == inCanvasId).get,
      canvases.find(_.id == outCanvasId).get) }
}
