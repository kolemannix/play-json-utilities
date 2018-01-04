# play-json-utilities
Home for various PlayJson helper macros and definitions

# AutoJson macro annotation

Annotating a case class with the AutoJson annotation puts a play.api.libs.json.Format instance in its companion object.
This works whether or not the companion object is already defined.

```scala
@AutoJson
case class ILoveRecordTypes(foo: Int, bar: Option[String], baz: Double)

@AutoJson
case class BagOfRecords(records: Seq[ILoveRecordTypes])
```

# Map
