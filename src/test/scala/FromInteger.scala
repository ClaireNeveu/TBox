package tbox

trait FromInteger[A] {
   def fromInteger(i : Int) : A
}
object FromInteger {
   def apply[A](implicit ev : FromInteger[A]) = ev
   implicit val Int_FromInteger = new FromInteger[Int] {
      def fromInteger(i : Int) = i
   }
}
