package examples

import org.scalatest.{FlatSpec, Matchers}
import Mappable._

/**
  * Created by ariwaranosai on 2016/11/8.
  *
  */

case class Kancolle(name: String, id: Int)
class MappableTest extends FlatSpec with Matchers {

  it should "materializeMappable" in {

    val p = Kancolle("laoyang", 24)

    p.toMap should be (Map("name" -> "laoyang", "id" -> 24))
    fromMap[Kancolle](Map("name" -> "laoyang", "id" -> 24)) should be (p)

  }

}
