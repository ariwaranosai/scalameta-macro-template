package examples

import java.io.{ByteArrayOutputStream, PrintStream}

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by ariwaranosai on 2016/11/9.
  *
  */

class PrintfTest extends FlatSpec with Matchers {

  behavior of "PrintfTest"

  it should "printf" in {
    val out: ByteArrayOutputStream = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(out)) {
      Printf.printf("%d+%d=%s\n", 1, 3 * 4, "13")
    }

    out.toString should be ("1+12=13\n")
  }

}
