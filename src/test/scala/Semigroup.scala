package tbox

trait Semigroup[A] {
   def append(a1 : A, a2 : A) : A
}
object Semigroup {
   def apply[A](implicit ev : Semigroup[A]) = ev
   implicit class SemigroupOps[A](self : A) {
      def ++(other : A)(implicit ev : Semigroup[A]) = ev.append(self, other)
   }

   implicit val Int_Semigroup = new Semigroup[Int] {
      def append(i1 : Int, i2 : Int) = i1 + i2
   }

   implicit val String_Semigroup = new Semigroup[String] {
      def append(s1 : String, s2 : String) = s1 + s2
   }
}
