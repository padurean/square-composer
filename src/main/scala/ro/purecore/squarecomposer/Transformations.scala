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
