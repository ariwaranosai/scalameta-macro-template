package examples

@mcase
case class Hello(name: String, id: Int)

@main
object MyApp {
  println("Hello Scala.meta macros!")
  val p = Hello("hi", 14)
  println(p.toMap)
}
