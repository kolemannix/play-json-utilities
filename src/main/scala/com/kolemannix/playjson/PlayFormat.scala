package com.kolemannix.playjson

import play.api.libs.json._

object PlayFormat {
  def apply[A](reads: JsValue => JsResult[A], writes: A => JsValue): Format[A] = Format[A](
    Reads(reads),
    Writes(writes)
  )
}

object PlayMapFormat {
  def apply[A : Format, B : Format](keyFn: (B => A)): Format[Map[A, B]] = PlayFormat(
    { jsValue => jsValue.validate[Seq[B]].map(bs => bs.map(b => (keyFn(b), b)).toMap) } ,
    { m => Json.toJson(m.values) }
  )
}
