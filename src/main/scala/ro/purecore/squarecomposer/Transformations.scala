package ro.purecore.squarecomposer

class Transformations(val squares: List[Square]) extends AnyVal {

  def compactDown: List[Square] = {
    val maxY = squares.maxBy(_.y).y
    squares
      .groupBy(_.x)
      .flatMap { case (x, havingSameX) =>
        val sortedByY = havingSameX.sortBy(_.y)
        val currMaxY = sortedByY.last.y
        for (square <- sortedByY)
          yield square.copy(y = square.y + (maxY - currMaxY)) }
      .toList }

  def compactLeft(squares: List[Square]): List[Square] =
    squares
      .groupBy(_.x)
      .values.toList
      .sortBy(_.head.x)
      .foldLeft(List.empty[List[Square]]) { case (acc, havingSameX) =>
        if (acc.nonEmpty && havingSameX.head.x > acc.last.head.x + 1)
          acc :+ havingSameX.map(_.copy(x = acc.last.head.x + 1))
        else acc :+ havingSameX }
      .flatten

  def moveVertically(deltaY: Int, color: Option[Color] = None): List[Square] =
    squares.map(s => s.copy(color = color.getOrElse(s.color), y = s.y + deltaY))

  def stackAbove(colors: List[Color]): List[Square] = {
    assert(
      squares.forall(s => s.x >= 0 || s.y >= 0),
      "Stacking doesn't work with negative coords!")

    val minY = squares.minBy(_.y).y
    val minYRequired = colors.size
    val overlapY = if (minYRequired > minY) minYRequired - minY else 0
    val liftedSquares = if (overlapY > 0) moveVertically(overlapY) else squares
    val bottomSquares = liftedSquares.groupBy(_.x).map(_._2.minBy(_.y)).toList

    val stacked = colors.flatMap { c =>
      val deltaY = colors.indexOf(c) + 1
      bottomSquares.moveVertically(-deltaY, Some(c)) }
    stacked ++ liftedSquares }

  def stackBelow(colors: List[Color]): List[Square] = {
    assert(
      squares.forall(s => s.x >= 0 || s.y >= 0),
      "Stacking doesn't work with negative coords!")

    val topSquares = squares.groupBy(_.x).map(_._2.maxBy(_.y)).toList

    val stacked = colors.flatMap { c =>
      val deltaY = colors.indexOf(c) + 1
      topSquares.moveVertically(deltaY, Some(c)) }
    stacked ++ squares }

  def stackAbovePreviousIfSameColor(): List[Square] = {
    assert(
      squares.groupBy(_.y).size == 1,
      "Stacking same color only supports squares with same Y value")

    val stackedByColor = squares
      .sortBy(_.x)
      .foldLeft(List.empty[Square]) { case (acc, square) =>
        if (acc.nonEmpty && acc.last.color == square.color)
          acc :+ square.copy(x = acc.last.x, y = acc.last.y-1)
        else acc :+ square }

    compactLeft(stackedByColor) }
}

object Transformations {

  val orange: Color = "#b35900"
  val yellow: Color = "#facd22"
  val blue: Color = "#4e7398"
  val brown: Color = "#663300"

  val logotype = List(
    Square(blue,   0, 0), Square(orange, 1, 0),
    Square(orange, 0, 1), Square(blue,   1, 1))

  val initialFigure0 = List(
    Square(blue,   0, 0),                                                                                     Square(blue,   5, 0),
    Square(orange, 0, 1), Square(blue,   1, 1),                                         Square(blue,   4, 1), Square(orange, 5, 1),
    Square(orange, 0, 2), Square(orange, 1, 2), Square(blue, 2, 2), Square(blue, 3, 2), Square(orange, 4, 2), Square(orange, 5, 2))
  val initialFigure1 = List(
    Square(orange, 0, 1), Square(blue,   1, 1), Square(blue,  2, 1), Square(brown, 3, 1),
    Square(orange, 0, 2), Square(orange, 1, 2), Square(brown, 2, 2), Square(brown, 3, 2))
  val initialFigure2 = List(
    Square(orange, 0, 0),                                           Square(orange, 3, 0),
    Square(blue,   0, 1), Square(brown, 1, 1), Square(brown, 2, 1), Square(blue,   3, 1),
    Square(blue,   0, 2), Square(blue,  1, 2), Square(blue,  2, 2), Square(blue,   3, 2))
  val initialFigure3 = List(
    Square(brown,  0, 2), Square(orange, 1, 2), Square(orange, 2, 2),
    Square(blue,   3, 2), Square(blue,   4, 2), Square(blue,   5, 2),
    Square(orange, 6, 2), Square(orange, 7, 2), Square(brown,  8, 2))

  val definitions = Seq(

    Transformation(
      uid = "0.1",
      prevUid = None,
      nextUid = Some("0.2"),
      name = "Transformation",
      input = initialFigure0,
      transformations = List(
        (squares: List[Square]) => squares.map(_.copy(color = blue)),
        (squares: List[Square]) => squares
          .map(s => if (s.color != blue) s.copy(color = yellow) else s)
      ),
      sourceCodes = List(
        """squares.map(_.copy(color = blue))""",
        """squares.map(s => if (s.color != blue) s.copy(color = blue) else s)"""
          .stripMargin
      ),
      functions = List.empty[String]),

    Transformation(
      uid = "0.2",
      prevUid = Some("0.1"),
      nextUid = Some("0.3"),
      name = "Rejection",
      input = initialFigure0,
      transformations = List(
        (squares: List[Square]) => squares.filter(_.color == blue).compactDown,
        (squares: List[Square]) => squares.filter(_.color == orange)
      ),
      sourceCodes = List(
        """squares.filter(_.color == blue).compactDown""",
        """squares.filter(_.color == orange)"""
      ),
      functions = List("compactDown")),

    Transformation(
      uid = "0.3",
      prevUid = Some("0.2"),
      nextUid = Some("0.4"),
      name = "Composition",
      input = initialFigure0,
      transformations = List(
        (squares: List[Square]) =>
          squares.filter(_.color == blue).compactDown.stackAbove(List(orange))
      ),
      sourceCodes = List(
        """squares.filter(_.color == blue).compactDown.stackAbove(List(orange))"""
      ),
      functions = List("compactDown", "stackAbove")),

    Transformation(
      uid = "0.4",
      prevUid = Some("0.3"),
      nextUid = Some("1.1"),
      name = "Spanish flag",
      input = initialFigure0,
      transformations = List(
        (squares: List[Square]) =>
          squares.filter(_.color == blue).compactDown.stackAbove(List(orange, blue))
      ),
      sourceCodes = List(
        """squares.filter(_.color == blue).compactDown.stackAbove(List(orange, blue))"""
      ),
      functions = List("compactDown", "stackAbove")),

    Transformation(
      uid = "1.1",
      prevUid = Some("0.4"),
      nextUid = Some("1.2"),
      name = "Mercury",
      input = initialFigure1,
      transformations = List(
        (squares: List[Square]) => squares
          .map(square =>
            if(square.color == brown) square.copy(color = orange)
            else square)
          .stackAbove(List(orange))
      ),
      sourceCodes = List(
        """squares
          |  .map(square =>
          |    if(square.color == brown) square.copy(color = orange)
          |    else square)
          |  .stackAbove(List(orange))"""
          .stripMargin
      ),
      functions = List.empty[String]),

    Transformation(
      uid = "1.2",
      prevUid = Some("1.1"),
      nextUid = Some("1.3"),
      name = "Venus",
      input = initialFigure1,
      transformations = List(
        (squares: List[Square]) => squares
          .groupBy(_.x)
          .filter(_._2.exists(_.color == orange))
          .flatMap(_._2)
          .map(square =>
            if(square.color == blue) square.copy(color = orange)
            else square)
          .toList
      ),
      sourceCodes = List(
        """squares
          |  .groupBy(_.x)
          |  .filter(_._2.exists(_.color == orange))
          |  .flatMap(_._2)
          |  .map(square =>
          |    if(square.color == blue) square.copy(color = orange)
          |    else square)
          |  .toList"""
          .stripMargin
      ),
      functions = List.empty[String]),

    Transformation(
      uid = "1.3",
      prevUid = Some("1.2"),
      nextUid = Some("1.4"),
      name = "Earth",
      input = initialFigure2,
      transformations = List(
        (squares: List[Square]) => squares
          .filter(_.color == blue)
          .moveVertically(-1)
          .stackBelow(List(brown))
      ),
      sourceCodes = List(
        """squares
          |  .filter(_.color == blue)
          |  .moveVertically(-1)
          |  .stackBelow(List(brown))"""
          .stripMargin
      ),
      functions = List("moveVertically", "stackBelow")),

    Transformation(
      uid = "1.4",
      prevUid = Some("1.3"),
      nextUid = Some("2.1"),
      name = "Mars",
      input = initialFigure1,
      transformations = List(
        (squares: List[Square]) =>
          squares.map(square => square.copy(color = orange))
      ),
      sourceCodes = List(
        """squares.map(square => square.copy(color = orange))"""
      ),
      functions = List.empty[String]),

    Transformation(
      uid = "2.1",
      prevUid = Some("1.4"),
      nextUid = None,
      name = "Bricklayer",
      input = initialFigure3,
      transformations = List(
        (squares: List[Square]) => squares
          .stackAbovePreviousIfSameColor()
          .map(s => if (s.color == blue) s.copy(color = brown) else s)
      ),
      sourceCodes = List(
        """squares
          |  .stackAbovePreviousIfSameColor()
          |  .map(s => if (s.color == blue) s.copy(color = brown) else s)"""
          .stripMargin
      ),
      functions = List("stackAbovePreviousIfSameColor"))
  )
}
