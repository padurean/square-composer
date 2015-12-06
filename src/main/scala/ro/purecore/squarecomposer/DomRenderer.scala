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
    val srcCodesString = t.sourceCodes.mkString("\n\n")
    val itemId = s"item-${t.uid}"
    val item =
      div(`class` := "item")(
        h4(span(`class` := "section-no")(t.uid), t.name),
        div(inCanvas),
        div(`class` := "snippetWrapper")(
          div(`class` := "snippet")(
            pre(`class` := "prettyprint lang-scala")(srcCodesString),
            div(`class` := "item-actions-box")(
              div(`class` := "item-versions-box")(
                select(id := s"$itemId-versions")(
                  for (sci <- t.sourceCodes.indices) yield {
                    option(id := s"$itemId-version-$sci", value := sci)(s"Version-$sci") } )),
              div(a(href := "#")(span("Run!"))))
          )),
        div(outCanvas)
      )
      .render

    if (t.functions.nonEmpty) {
      item.appendChild(div(`class` := "jump-to-def")("Jump to function:").render)
        for (func <- t.functions) {
          val linkToDef =
            a(`class` := "link-to-def", href := s"#def-${ snakify(func) }")(pre (func))
          item.appendChild(linkToDef.render) }
    }

    item.appendChild(
      div(id := "goto-box")(
        a(id := "goto-prev", href := "#")(raw("&lt;&lt;")),
        a(id := "goto-next", href := "#")(raw("&gt;&gt;")))
      .render)

    (item, inputCanvasId, outputCanvasId) }

  def renderCommonCode(
    sectionId: String,
    sectionName: String,
    code: String)
  : dom.Node = {
      div(`class` := "item")(
        h4(span(`class` := "section-no")(sectionId), sectionName),
        div(`class` := "snippet")(
          pre(`class` := "prettyprint lang-scala")(raw(code))))
        .render }
}
