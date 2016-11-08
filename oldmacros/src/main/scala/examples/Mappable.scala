package examples

import scala.reflect.macros.blackbox.Context

/**
  * Created by ariwaranosai on 2016/11/8.
  *
  */

trait Mappable[T] {
  def toMap(t: T): Map[String, Any]
  def fromMap(map: Map[String, Any]): T
}


object Mappable {
  implicit class MappableOps[T](cclass: T) {
    def toMap(implicit m: Mappable[T]) = m.toMap(cclass)
  }

  def fromMap[T](map: Map[String, Any])(implicit m: Mappable[T]) = m.fromMap(map)

  implicit def materializeMappable[T]: Mappable[T] =
    macro materializeMappableImpl[T]

  def materializeMappableImpl[T: c.WeakTypeTag](c: Context): c.Expr[Mappable[T]] = {
    import c.universe._

    val ty = weakTypeOf[T]
    val declarations = ty.decls

    val ctor = declarations.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get

    val params = ctor.paramLists.head

    val com = ty.typeSymbol.companion

    val (toMapParams, fromMapParams) = params.map(x => {
      val name = x.asTerm.name
      val key = name.decodedName.toString
      val rtype = ty.decl(name).typeSignature
      (q"$key -> t.$name", q"map($key).asInstanceOf[$rtype]")
    }).unzip


    c.Expr[Mappable[T]] {
      q"""
         new Mappable[$ty] {
            def toMap(t: $ty): Map[String, Any] = Map(..$toMapParams)
            def fromMap(map: Map[String, Any]): $ty = $com(..$fromMapParams)
         }
       """
    }
  }
}
