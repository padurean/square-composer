package ro.purecore.squarecomposer

import org.scalajs.dom

import scalatags.JsDom.all._

object DomRenderer {
  val inputCanvasId = s"initial-figure-canvas"
  val outputCanvasId = s"transformation-canvas"

  def renderAll(t: Transformation): List[dom.Node] =
    t.transformations.map(render(t)(_))

  def render(
    t: Transformation)(
    f: List[Square] => List[Square])
  : dom.Node = {

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
    val onClickRunCb =
      s"ro.purecore.squarecomposer.Effects().drawForUid('${t.uid}', document.getElementById('main')); PR.prettyPrint(); return false;"
    val item =
      div(`class` := "item")(
        h4(span(`class` := "section-no")(t.uid), t.name),
        div(inCanvas),
        div(`class` := "snippetWrapper")(
          div(`class` := "snippet")(
            pre(`class` := "prettyprint lang-scala")(srcCodesString),
            div(`class` := "item-actions-box")(
              div(`class` := "item-versions-box")(
                select(id := "item-versions")(
                  for (sci <- t.sourceCodes.indices) yield {
                    option(id := s"item-version-$sci", value := sci)(s"Version-$sci") } )),
              div(a(href := "#", onclick := onClickRunCb)(span("Run!"))))
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

    val onClickPrevCb = t.prevUid
      .map( prevUid =>
        s"ro.purecore.squarecomposer.Effects().drawForUid('$prevUid', document.getElementById('main'), false); PR.prettyPrint(); return false;")
      .getOrElse("return false;")
    val onClickNextCb = t.nextUid
      .map( nextUid =>
        s"ro.purecore.squarecomposer.Effects().drawForUid('$nextUid', document.getElementById('main'), false); PR.prettyPrint(); return false;")
      .getOrElse("return false;")
    item.appendChild(
      div(id := "goto-box")(
        a(id := "goto-prev", href := "#", onclick := onClickPrevCb)(raw("&lt;&lt;")),
        a(id := "goto-next", href := "#", onclick := onClickNextCb)(raw("&gt;&gt;")))
      .render)

    item }

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
