package com.kolemannix

import org.scalatest.FreeSpec
import scala.annotation.StaticAnnotation
import scala.collection.immutable.Seq
import scala.meta._

class Bug extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ Defn.Class(_, name, Nil, _, _) =>
        val companion = q"object ${Term.Name(name.value)}"
        Term.Block(Seq(cls, companion))
      case bad => abort("Didn't match")
    }
  }
}

class BugSpec extends FreeSpec {

  @Bug
  case class Foo(a: Int)

  Foo(42)

}
