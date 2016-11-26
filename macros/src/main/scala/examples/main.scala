package examples


import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._
import scala.meta.Term.Param

@compileTimeOnly("@examples.Main not expanded")
class main extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val q"object $name { ..$stats }" = defn
    val main = q"def main(args: Array[String]): Unit = { ..$stats }"
    q"object $name { $main }"
  }
}

@compileTimeOnly("@examples.mcase not expanded")
class mcase extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val q"case class $tname[..$tparams] ..$mods (...$paramss)" = defn
    val params: Seq[Param] = paramss.head
    val attr = params.map(pa => q"(${pa.name.syntax} ->${Term.Name(pa.name.value)})")

    val toMap =
      q"""def toMap: _root_.scala.collection.Map[String, Any] =
                _root_.scala.collection.Map(..${attr.asInstanceOf[Seq[Term.Arg]]})"""

    q"""
      case class $tname[..$tparams] ..$mods (...$paramss){
        $toMap
      }
    """
  }
}
