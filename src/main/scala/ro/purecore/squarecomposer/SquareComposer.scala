package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode._
import org.scalajs.dom.html
import ro.purecore.squarecomposer.CommonCodeListing._

import scala.language.postfixOps
import scala.scalajs.js.annotation.JSExport


@JSExport
object SquareComposer {

  @JSExport
  var state: String = Transformations.definitions.head.uid

  @JSExport
  def main(logoCanvas: html.Canvas, mainDiv: html.Div): Unit = {

    def transformationVersion(d: html.Div): Int = d
      .getElementsByClassName("transformation-version")
      .item(0)
      .attributes
      .getNamedItem("value")
      .value
      .toInt

    def run() = {
      Effects.draw(
        Transformations.logotype,
        0,
        0,
        logoCanvas.width/2,
        "0px")(
        logoCanvas.getContext2D)

      Effects.renderFirstTransformationAndDraw(
        mainDiv,
        Transformations.definitions.head,
        0)

      mainDiv.appendChild(
        DomRenderer.renderCommonCode("Appendix:", "Common Code", commonCode))

      dom.onkeydown = { (e: dom.KeyboardEvent) =>
        for (t <- Transformations.definitions.find(_.uid == SquareComposer.state)) {
          if (e.keyCode == left && t.prevUid.isDefined) {
            Effects.drawForUid(
              t.prevUid.get,
              mainDiv,
              version = 0,
              drawOutputFigure = false)  }
          else if (e.keyCode == right && t.nextUid.isDefined) {
            Effects.drawForUid(
              t.nextUid.get,
              mainDiv,
              version = 0,
              drawOutputFigure = false) }
          else if (e.keyCode == enter) {
            Effects.drawForUid(
              t.uid,
              mainDiv,
              version = transformationVersion(mainDiv),
              drawOutputFigure = true) }
          else if (e.keyCode == escape) {
            Effects.drawForUid(
              t.uid,
              mainDiv,
              version = transformationVersion(mainDiv),
              drawOutputFigure = false) }
          else {
            val numKeys = List(num1, num2, num3, num4, num5)
            val isNumeric = numKeys.contains(e.keyCode)
            if (isNumeric) {
              val v = e.keyCode match {
                case `num1` => 0
                case `num2` => 1
                case `num3` => 2
                case `num4` => 3
                case `num5` => 4
              }
              if (t.sourceCodes.indices.contains(v)) {
                Effects.drawForUid(
                  t.uid,
                  mainDiv,
                  version = v,
                  drawOutputFigure = false) } } } } }
    }

    dom.setTimeout(() => run(), 50) }
}
