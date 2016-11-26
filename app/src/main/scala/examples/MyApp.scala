package examples

import menum.EnumMacros

@mcase
case class Hello(name: String, id: Int)

@main
object MyApp {

  @EnumMacros
  object kancolle {
    type Value = String
    val saratoga = "233"
    val asakaze = "272"
  }

  import kancolle._

  println(saratoga.value)
  println(kancolle.allObjects.foreach(x => println(x.value)))
  println("Hello Scala.meta macros!")
  val p = Hello("hi", 14)
  println(p.toMap)
}
