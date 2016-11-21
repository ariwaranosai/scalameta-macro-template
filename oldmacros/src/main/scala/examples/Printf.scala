package examples

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Stack}
import scala.reflect.macros.blackbox

/**
  * Created by ariwaranosai on 2016/11/9.
  *
  */

object Printf {

  def printf(format: String, params: Any*): Unit = macro printf_impl

  def printf_impl(c: blackbox.Context)(format: c.Expr[String], params: c.Expr[Any]*): c.Expr[Unit] = {
    import c.universe._

    // 从format的Expr上匹配出来具体的String
    val Literal(Constant(s_format: String)) = format.tree

    val evals = ListBuffer[ValDef]()

    // 先把参数部分加到evals里面，然后返回那个参数名字
    // printf("%d", 1 + 3) =>
    // val eval$macros$1 = 1 + 3
    // eval$macros$1
    def precompute(value: Tree, tpe: Type): Ident = {
      val freshName = TermName(c.freshName("eval$"))
      evals += ValDef(Modifiers(), freshName, TypeTree(tpe), value)
      Ident(freshName)
    }

    val paramsStack = mutable.Stack[Tree](params map (_.tree): _*)
    // 拼出来之后需要print的内容
    val refs = s_format.split("(?<=%[\\w%])|(?=%[\\w%])") map {
      case "%d" => precompute(paramsStack.pop, typeOf[Int])
      case "%s" => precompute(paramsStack.pop, typeOf[String])
      case "%%" => Literal(Constant("%"))
      case part => Literal(Constant(part))
    }

    // 先计算值，然后一次次调用print输出
    val stats = evals ++ refs.map(ref => reify(print(c.Expr[Any](ref).splice)).tree)

    c.Expr[Unit](Block(stats.toList, Literal(Constant())))

  }

}
