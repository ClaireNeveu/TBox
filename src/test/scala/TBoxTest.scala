package tbox

import org.scalatest.FunSuite

class TBoxSpec extends FunSuite {

   case class Foo(a : String)
   object Foo {
      implicit val Foo_ToJson : ToJson[Foo] = new ToJson[Foo] {
         def toJson(foo : Foo) =
            s"""{"a":"${foo.a}"}"""
      }
      implicit val Foo_Show : Show[Foo] = new Show[Foo] {
         def show(foo : Foo) =
            s"""Foo(${foo.a})"""
      }
   }

   case class Bar(b : Int)
   object Bar {
      implicit val Bar_ToJson : ToJson[Bar] = new ToJson[Bar] {
         def toJson(bar : Bar) =
            s"""{"b":${bar.b}}"""
      }
      implicit val Bar_Show : Show[Bar] = new Show[Bar] {
         def show(bar : Bar) =
            s"""Bar(${bar.b})"""
      }
   }

   test("TBox should work for simple type-classes.") {
      val toJsonList : List[TBox[ToJson]] =
         TBox[ToJson](Foo("hello")) :: TBox[ToJson](Bar(123)) :: Nil

      assert(toJsonList.map(_.toJson) == List("""{"a":"hello"}""", """{"b":123}"""))
   }

   test("TBox2 should work for simple type-classes.") {
      val hetList : List[TBox2[ToJson, Show]] =
         TBox2[ToJson, Show](Foo("hello")) :: TBox2[ToJson, Show](Bar(123)) :: Nil

      assert(hetList.map(_.toJson) == List("""{"a":"hello"}""", """{"b":123}"""))
      assert(hetList.map(_.show) == List("""Foo(hello)""", """Bar(123)"""))
   }
}
