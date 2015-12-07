package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom._

import scala.scalajs.js.annotation.JSExport

@JSExport
object Effects {

  @JSExport
  def drawForUid(uid: String, parentDiv: html.Div, drawOutputFigure: Boolean = true): Unit = {
    for (t <- Transformations.definitions.find(_.uid == uid))
      renderFirstTransformationAndDraw(
        parentDiv,
        t,
        replace = true,
        drawOutputFigure) }

  @JSExport
  def renderFirstTransformationAndDraw(
    parentDiv: html.Div,
    t: Transformation,
    replace: Boolean = false,
    drawOutputFigure: Boolean = false)
  : Unit = {
    val itemDiv =
      DomRenderer.render(t)(t.transformations.head) // TODO OGG: mind the head - that's why it's called renderFIRSTTransformationAndDraw
    val lastChild = parentDiv.lastChild // TODO OGG: this is the common code; maybe you do it by just replacing item's content
    // to avoid re-appending also the common code
    if (replace) {
      parentDiv.removeChild(parentDiv.firstChild)
      parentDiv.removeChild(parentDiv.firstChild) }
    parentDiv.appendChild(itemDiv)
    if (replace)
      parentDiv.appendChild(lastChild)

    val (inCanvas, outCanvas) =
      fetchCanvases(
        parentDiv.firstElementChild,
        DomRenderer.inputCanvasId,
        DomRenderer.outputCanvasId)

    draw(
      t, drawOutputFigure)(
      inCanvas.getContext2D, outCanvas.getContext2D) }

  def draw(
    t: Transformation,
    drawOutputFigure: Boolean)(
    in2DContext: CanvasRenderingContext2D,
    out2DContext: CanvasRenderingContext2D)
  : Unit = {
    drawBg(t.input, 0, 0)(in2DContext)
    draw(t.input, 0, 0)(in2DContext)
    drawBg(t.input, 0, 0)(out2DContext)
    if (drawOutputFigure)
      draw(t.map(t.transformations.head), 0, 0)(out2DContext) }

  def draw (
    squares: List[Square],
    dx: Int,
    dy: Int,
    s: Int = defaultSize,
    strokeStyle: String = "#222")(
    implicit ctx: dom.CanvasRenderingContext2D)
  : Unit = {
    ctx.strokeStyle = strokeStyle
    for(square <- squares.reverse)
      drawSquare(square.x, square.y, dx, dy, s, square.color) }

  def drawBg (
    squares: List[Square],
    dx: Int,
    dy: Int,
    s: Int = defaultSize,
    color: Color = "#1c1c1c",
    strokeStyle: String = "#333")(
    implicit ctx: dom.CanvasRenderingContext2D)
  : Unit = {
    val maxX = squares.maxBy(_.x).x
    val maxY = squares.maxBy(_.y).y

    ctx.strokeStyle = strokeStyle
    for (x <- 0 to maxX; y <- 0 to maxY)
      drawSquare(x, y, dx, dy, s, color) }

  def drawSquare(
    x: Int,
    y: Int,
    dx: Int,
    dy: Int,
    s: Int,
    color: Color)(
    implicit ctx: CanvasRenderingContext2D)
  : Unit = {
    ctx.strokeRect(x * s + dx, y * s + dy, s, s)

    ctx.fillStyle = color
    ctx.fillRect(x * s + dx, y * s + dy, s, s) }

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
    val canvasElems = item.getElementsByTagName("canvas")
    val canvases = List(
      canvasElems.item(0).asInstanceOf[html.Canvas],
      canvasElems.item(1).asInstanceOf[html.Canvas])
    (canvases.find(_.id == inCanvasId).get,
      canvases.find(_.id == outCanvasId).get) }
}
