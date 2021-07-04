package tutorial.webapp

import caliban.client.CalibanClientError.DecodingError
import caliban.client.FieldBuilder._
import caliban.client._
import caliban.client.__Value._

object Client {

  sealed trait Color extends scala.Product with scala.Serializable
  object Color {
    case object Blue extends Color
    case object Green extends Color
    case object Red extends Color

    implicit val decoder: ScalarDecoder[Color] = {
      case __StringValue("Blue")  => Right(Color.Blue)
      case __StringValue("Green") => Right(Color.Green)
      case __StringValue("Red")   => Right(Color.Red)
      case other                  => Left(DecodingError(s"Can't build Color from input $other"))
    }
    implicit val encoder: ArgEncoder[Color] = {
      case Color.Blue  => __EnumValue("Blue")
      case Color.Green => __EnumValue("Green")
      case Color.Red   => __EnumValue("Red")
    }
  }

  type Character
  object Character {

    final case class CharacterView(name: String, age: Int)

    type ViewSelection = SelectionBuilder[Character, CharacterView]

    def view: ViewSelection = (name ~ age).map { case (name, age) =>
      CharacterView(name, age)
    }

    def name: SelectionBuilder[Character, String] =
      _root_.caliban.client.SelectionBuilder.Field("name", Scalar())
    def age: SelectionBuilder[Character, Int] =
      _root_.caliban.client.SelectionBuilder.Field("age", Scalar())
  }

  type ColorQueries = _root_.caliban.client.Operations.RootQuery
  object ColorQueries {
    def characters[A](
        innerSelection: SelectionBuilder[Character, A]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, Option[
      List[A]
    ]] = _root_.caliban.client.SelectionBuilder
      .Field("characters", OptionOf(ListOf(Obj(innerSelection))))
    def character[A](
        name: String
    )(innerSelection: SelectionBuilder[Character, A])(implicit
        encoder0: ArgEncoder[String]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootQuery, Option[A]] =
      _root_.caliban.client.SelectionBuilder.Field(
        "character",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("name", name, "String!")(encoder0))
      )
    def colors
        : SelectionBuilder[_root_.caliban.client.Operations.RootQuery, List[
          Color
        ]] =
      _root_.caliban.client.SelectionBuilder.Field("colors", ListOf(Scalar()))
  }

  type Mutations = _root_.caliban.client.Operations.RootMutation
  object Mutations {
    def deleteCharacter(name: String)(implicit
        encoder0: ArgEncoder[String]
    ): SelectionBuilder[_root_.caliban.client.Operations.RootMutation, Option[
      Boolean
    ]] = _root_.caliban.client.SelectionBuilder.Field(
      "deleteCharacter",
      OptionOf(Scalar()),
      arguments = List(Argument("name", name, "String!")(encoder0))
    )
  }

}

