package tutorial.webapp

import com.raquo.laminar.api.L.{
  child, color, documentEvents, button,
  div, EventBus, h1, onClick, p, render,
  unsafeWindowOwner, given
}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.{document, Event, MouseEvent, Node}
import org.scalajs.dom.html.Document
import org.scalajs.dom.raw.Element
import scala.compiletime.constValue
import scala.util.chaining.scalaUtilChainingOps
import sttp.client3.quick.{backend, UriContext}
import zio.ZIO.{fromEither, fromFuture}
import zio.Runtime.default.unsafeRunAsync_

val revision = 43
@main
def run =
  documentEvents.onDomContentLoaded.foreach(_ => setupUI())(unsafeWindowOwner)
val clickBus = EventBus[Unit]
def setupUI(): Unit =
  Client
    .ColorQueries
    .characters(Client.Character.view)
    .toRequest(uri"http://localhost:8088/api")
    .send(backend)
    .map(_.body)
    .pipe(future => fromFuture(_ => future))
    .flatMap(fromEither(_))
    .map(response =>
      render(document.body, div().amend(
        Seq(h1("Hello world", color := "red"), button("Click me!", color := "green", onClick.mapTo(()) --> clickBus))
        ++ response.fold(Seq[ReactiveHtmlElement[_]]())(_.flatMap(character => Seq(character.name, character.age)).map(_.toString).map(text => p(text, color := "blue")))
        :+ div(child.maybe <-- clickBus.events.mapTo(Some(div(s"You clicked the button!  Revision $revision"))))
      ))
    )
    .pipe(unsafeRunAsync_(_))
