package tbox.examples

import tbox._

trait Foo[I] { def bar(i : I) : String }

trait Baz[T]

object Main extends App {
	override def main(args: Array[String]) = {
	}
	//TBox.tree[Foo](new Foo[Int] { def bar(i : Int) = i.toString })
	lazy val test = TBox.instance[Foo]
	//val test = TBox.test[Baz]
}
