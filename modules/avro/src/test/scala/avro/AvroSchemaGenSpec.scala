package avro

import avro.testcases.{FooBar, IntOrString, RecursiveFoo}
import avro.testcases.IntOrString.{IntValue, StringValue}
import munit._
import smithy4s.schema.Schema
import smithy4s.schema.Schema.struct
import smithy4s.schema.Schema.int
import vulcan.{AvroError, Codec}
import vulcan.generic._
class AvroSchemaGenSpec extends FunSuite {

  case class Foo(a: Int, b: Option[Int])

  object Foo {
    implicit val schema: Schema[Foo] = {
      val a = int.required[Foo]("a", _.a)
      val b = int.optional[Foo]("b", _.b)
      struct(a, b)(Foo.apply)
    }.withId("avro.AvroSchemaCodecSpec", "Foo")
  }



  test("Avro Struct tests the schema generated from the codec") {
    val foo    = Foo(1, Some(2))
    val codec  = AvroCodecSchemaVisitor.fromSchema(Foo.schema)
    val expected =
      """
        |{
        |  "type": "record",
        |  "name": "Foo",
        |  "namespace": "avro.AvroSchemaCodecSpec",
        |  "fields": [
        |  {
        |  "name": "a",
        |  "type": "int"
        |  },
        |  {
        |  "name": "b",
        |  "type": [
        |  "null",
        |  "int"
        |  ]
        |  }
        |  ]
        |  }
        |""".stripMargin
    schemaEquals(codec,expected)
  }

  test("Avro Union test , will not use the case classes rather the schema model which is an int or string as opposed to record"){
    val codec  = AvroCodecSchemaVisitor.fromSchema(IntOrString.schema)
    val expected = """
                     |["int","string"]
                   """.stripMargin
    schemaEquals(codec,expected)
  }

  test("Avro enumeration test"){
    val foo = FooBar.Foo
    val bar = FooBar.Bar
    val codec  = AvroCodecSchemaVisitor.fromSchema(FooBar.schema)
    val expected =
      """
        |{"type":"enum","name":"FooBar","namespace":"avro.testcases","symbols":["foo","neq"]}
        |
        |""".stripMargin
    schemaEquals(codec,expected)
  }

  // write more tests
  test("Avro on recursive data structure"){
    val codec  = AvroCodecSchemaVisitor.fromSchema(RecursiveFoo.schema)
    val expected =
      """
        |{
        |  "type": "record",
        |  "name": "RecursiveFoo",
        |  "fields": [
        |  {
        |  "name": "foo",
        |  "type": [
        |  "null",
        |  "RecursiveFoo"
        |  ]
        |  }
        |  ]
        |  }
        |""".stripMargin
    schemaEquals(codec,expected)
  }


  private def schemaEquals[A](codec: Codec[A], expected: String): Unit = {
    assertEquals(codec.schema.map(_.toString.replaceAll("\\s+","")), Right(expected.replaceAll("\\s+","")))
  }
  private def checkAbsolutely(codec: Codec[IntOrString],value:IntOrString)( result0: Either[AvroError, codec.AvroType]) = {
    val roundTripped = for {
      schema <- codec.schema
      avroType <- result0
      decoded <- codec.decode(avroType, schema)
    } yield decoded
    assertEquals(roundTripped, Right(value))
  }
}
