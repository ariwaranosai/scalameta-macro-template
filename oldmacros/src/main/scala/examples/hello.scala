package examples

import scala.reflect.macros.blackbox.Context

/**
  * Created by ariwaranosai on 2016/11/8.
  *
  */

object hello {
  def greetings: String = macro greetingMacro
  def greetingMacro(c: Context): c.Tree = {
    import c.universe._

    q"""
       println("hello world!")
       "hello world!"
     """
  }

}
