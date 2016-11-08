package examples

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by ariwaranosai on 2016/11/8.
  *
  */

class helloMacroTest extends FlatSpec with Matchers{

  it should "greetings" in {
    hello.greetings should be ("hello world!")
  }

}
