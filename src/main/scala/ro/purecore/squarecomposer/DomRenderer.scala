package ro.purecore.squarecomposer

import org.scalajs.dom

import scalatags.JsDom.all._

object DomRenderer {
  def renderAll(t: Transformation): List[(dom.Node, String, String)] =
    t.transformations.map(render(t)(_))

  def render(
    t: Transformation)(
    f: List[Square] => List[Square])
  : (dom.Node, String, String) = {

    val idStr = t.uid.replaceAllLiterally(".", "")
    val inputCanvasId = s"initial-figure-$idStr"
    val outputCanvasId = s"transformation-$idStr"

    val output = f(t.input)
    val w =
      (Math.max(t.input.maxBy(_.x).x, output.maxBy(_.x).x) + 1) * defaultSize
    val h =
      (Math.max(t.input.maxBy(_.y).y, output.maxBy(_.y).y) +1) * defaultSize

    val inCanvas = canvas(
      id := inputCanvasId,
      "width".attr := w.toString,
      "height".attr := h.toString)
    val outCanvas = canvas(
      id := outputCanvasId,
      "width".attr := w.toString,
      "height".attr := h.toString)
    val item =
      div(`class` := "item")(
        h4(
          span(`class` := "section-no")(t.uid),
          t.name),
        div(
          inCanvas),
        div(
          div(
            `class` := "snippet")(
            pre(
              `class` := "prettyprint lang-scala")(
              t.sourceCodes.mkString("\n\n")))),
        div(
          outCanvas),
        div(`class` := "jump-to-def")("Jump to function:"))
        .render

    for (func <- t.functions) item.appendChild(
      a(
        `class` := "link-to-def", href := s"#def-${snakify(func)}")(
        pre(func)).render)

    (item, inputCanvasId, outputCanvasId) }
}
