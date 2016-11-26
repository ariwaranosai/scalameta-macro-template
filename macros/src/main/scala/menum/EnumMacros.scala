package menum

import scala.annotation.compileTimeOnly
import scala.meta._

/**
  * Created by ariwaranosai on 2016/11/25.
  *
  */

@compileTimeOnly("@menum.EnumMacros not expand")
class EnumMacros extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val q"object $tname { ..$stats}"  = defn
    val className = Type.Name(tname.value)
    val realType = stats collectFirst {
      case q"type Value = $ty" => ty
    } getOrElse "Int".parse[Type].get

    val abstractClass = q"sealed abstract class $className { val value: $realType }"
    val nstats = stats collect {
      case q"val $kname = $value" =>
        q"""
          case object ${Term.Name(kname.syntax)} extends ${Ctor.Ref.Name(className.syntax)}{
            val value: $realType = $value
          }"""
      case o => o
    }

    val aenum = q"""val allObjects = Array(..${stats collect {
      case q"val $kname = $value" => Term.Name(kname.syntax)
    }})"""


    q"""
      object $tname {
        $abstractClass
        ..$nstats
        $aenum
      }
      """

  }
}
