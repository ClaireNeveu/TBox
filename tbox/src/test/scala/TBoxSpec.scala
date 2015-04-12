package tbox

trait ToJson[A] {
		def toJson(a: A): String
}
object ToJson {
	def apply[A](implicit ev : ToJson[A]) = ev
	implicit class ToJsonOps[A](a : A) {
		def toJson(implicit ev: ToJson[A]) = ev.toJson(a)
	}
}

case class Foo(a: String)
object Foo {
	implicit val Foo_ToJson: ToJson[Foo] = new ToJson[Foo] {
		def toJson(foo: Foo) =
			s"""{"a":"${foo.a}"}"""
	}
}

case class Bar(b: Int)
object Bar {
	implicit val Bar_ToJson: ToJson[Bar] = new ToJson[Bar] {
		def toJson(bar: Bar) =
			s"""{"b":"${bar.b}"}"""
	}
}

object TBoxSpec extends App {

	import ToJson._

	override def main(args: Array[String]) {
		val toJsonList: List[TBox[ToJson]] =
			TBox[ToJson](Foo("hello")) :: TBox[ToJson](Bar(123)) :: Nil
		
		println(toJsonList.map(_.toJson))
	}
}
