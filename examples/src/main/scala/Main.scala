package tbox.examples

import tbox._

trait Foo[I] {
   def bar(i : I, s : String) : String
   def baz(d : Double, i : I) : Int
	val foo : String
}

trait Baz[T]

object Main extends App {
   override def main(args : Array[String]) = {
   }
   //TBox.tree[Foo](new Foo[Int] { def bar(i : Int) = i.toString })
   //lazy val test = TBox.instance[Foo]
   implicitly[Foo[TBox[Foo]]]
   //val test = TBox.test[Baz]
}
