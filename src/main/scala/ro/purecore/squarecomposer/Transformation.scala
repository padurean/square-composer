package ro.purecore.squarecomposer

case class Transformation(
  uid: String,
  prevUid: Option[String],
  nextUid: Option[String],
  name: String,
  input: List[Square],
  transformations: List[List[Square] => List[Square]],
  sourceCodes: List[String],
  functions: List[String]) {

  def map(v: Int):List[Square] = transformations(v)(input)
}
