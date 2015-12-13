package ro.purecore.squarecomposer

import org.scalajs.dom

import scalatags.JsDom.all._

object DomRenderer {
  val inputCanvasId = s"initial-figure-canvas"
  val outputCanvasId = s"transformation-canvas"

  def renderAll(t: Transformation): List[dom.Node] =
    t.transformations.map(render(t)(_))

  def render(
    t: Transformation,
    version: Int = 0)(
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
    val srcCodeString = t.sourceCodes(version)
    val runVersionJs = "parseInt(document.getElementById('transformation-version').value)"
    val onClickRunCb =
      s"ro.purecore.squarecomposer.Effects().drawForUid('${t.uid}', document.getElementById('main'), $runVersionJs, true); return false;"
    val item =
      div(`class` := "item")(
        h4(span(`class` := "section-no")(t.uid), t.name),
        div(inCanvas),
        div(`class` := "snippetWrapper")(
          div(`class` := "snippet")(
            pre(`class` := "prettyprint lang-scala")(srcCodeString),
            div(`class` := "item-actions-box")(
              if (t.sourceCodes.size > 1) {
              div(`class` := "item-versions-box")(
                for (v <- t.sourceCodes.indices) yield {
                  val currCb =
                    s"document.getElementById('transformation-version').value = $v; ro.purecore.squarecomposer.Effects().drawForUid('${t.uid}', document.getElementById('main'), $v, false); return false;"
                  val versionBtnClass = if (v != version) "blue" else "blue selected"
                  div(
                    `class` := "butoneWrapper")(
                    a(`class` := versionBtnClass, href := "#", onclick := currCb, title := s"Version #${v+1} (TIP: key ${v+1})")(span(s"V${v+1}"))) }

              ) } else "",
              div(
                `class` := "butoneWrapper",
                id := "run-btn")(
                a(href := "#", onclick := onClickRunCb, title := "Run, Forrest! RUN! :)")(span("Run!"))))
          ),
        input(`type` := "hidden", name := "transformation-version", id := "transformation-version", `class` := "transformation-version", value := version.toString)),
        div(outCanvas)
      )
      .render

    if (t.functions.nonEmpty) {
      item.appendChild(div(`class` := "jump-to-def")("Jump to function:").render)
        for (funcDocu <- t.functions) {
          val (docUrl, linkTarget) = funcDocu.docUrl
            .fold(s"#def-${ dashify(funcDocu.name)}", "")((_, "_blank"))
          val linkToDef =
            a(`class` := "link-to-def", target := linkTarget, href := docUrl)(pre (funcDocu.name))
          item.appendChild(linkToDef.render) }
    }

    val onClickPrevCb = t.prevUid
      .map( prevUid =>
        s"ro.purecore.squarecomposer.Effects().drawForUid('$prevUid', document.getElementById('main'), 0, false); return false;")
      .getOrElse("return false;")
    val onClickNextCb = t.nextUid
      .map( nextUid =>
        s"ro.purecore.squarecomposer.Effects().drawForUid('$nextUid', document.getElementById('main'), 0, false); return false;")
      .getOrElse("return false;")
    item.appendChild(
      div(id := "goto-box")(
        a(id := "goto-prev", href := "#", title := "< Previous Transformation", onclick := onClickPrevCb)(raw("&lt;")),
        a(id := "goto-next", href := "#", title := "Next Transformation >", onclick := onClickNextCb)(raw("&gt;")))
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
