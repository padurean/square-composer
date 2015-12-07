package ro.purecore.squarecomposer

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
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
        Transformations.definitions.head)

      mainDiv.appendChild(
        DomRenderer.renderCommonCode("Appendix:", "Common Code", commonCode))

      dom.onkeydown = { (e: dom.KeyboardEvent) =>
        for (t <- Transformations.definitions.find(_.uid == SquareComposer.state)) {
          if (e.keyCode == KeyCode.left && t.prevUid.isDefined) {
            Effects.drawForUid(t.prevUid.get, mainDiv, drawOutputFigure = false)  }
          else if (e.keyCode == KeyCode.right && t.nextUid.isDefined) {
            Effects.drawForUid(t.nextUid.get, mainDiv, drawOutputFigure = false) }
          else if (e.keyCode == KeyCode.enter) {
            Effects.drawForUid(t.uid, mainDiv, drawOutputFigure = true) } } }
    }

    dom.setTimeout(() => run(), 50) }
}
