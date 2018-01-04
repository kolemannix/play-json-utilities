package com.kolemannix

import com.kolemannix.playjson.AutoJson
import org.scalatest.{ FreeSpec, Matchers }
import play.api.libs.json._

class AutoJsonAnnotationSpec extends FreeSpec with Matchers {

  @AutoJson case class Foo(bar: String)
  @AutoJson(flatten = false) case class Foooo(bar: Int)


  @AutoJson case class Baz(a: Int, b: String, c: Foo, s: Seq[Foo])

  @AutoJson case class WithCompanion(a: Int)
  object WithCompanion {
    val iNeedThis: Int = 42
  }

  private def assertRoundtrip[A: Format](exemplar: A): Unit = {
    Json.parse(Json.toJson(exemplar).toString).as[A] shouldBe exemplar
  }

  "Single-value case classes should" - {
    val exemplar: Foo = Foo("asdf")
    "be represented as values, not objects" in {
      Json.toJson(exemplar) shouldBe a[JsString]
    }
    "roundtrip" in {
      assertRoundtrip(exemplar)
    }
  }

  "Multi-value case classes, with nested objects should" - {
    val exemplar: Baz = Baz(42, "foobear", Foo("I am foo"), Foo("a") :: Foo("b") :: Foo("c") :: Nil)
    "roundtrip" in {
      assertRoundtrip(exemplar)
    }
  }

  @AutoJson(flatten = false) case class FlattenTest1(bar: Int)
  @AutoJson(false) case class FlattenTest2(bar: Int)
  "Flatten argument behaves as expected" in {
    //    @AutoJson case class FlattenTest(bar: Int)
    Json.toJson(FlattenTest1(42)) shouldBe a[JsObject]
    Json.toJson(FlattenTest1(42)) shouldNot be (a[JsNumber])

    Json.toJson(FlattenTest2(42)) shouldBe a[JsObject]
    Json.toJson(FlattenTest2(42)) shouldNot be (a[JsNumber])
  }

  "Case classes with existing companion objects cause no problem" in {
    assertRoundtrip(WithCompanion(42))
    WithCompanion.iNeedThis shouldBe 42
  }

  "Case classes with no fields are rejected" in {
    "@AutoJson case class Bear()" shouldNot compile
  }
  "Case classes with type parameters are rejected" in {
    "@AutoJson case class Bear[T](a: Int)" shouldNot compile
  }
  "Case classes with multiple parameter lists are rejected" in {
    "@AutoJson case class Bear[T](a: Int)(b: Int)" shouldNot compile
  }

}

