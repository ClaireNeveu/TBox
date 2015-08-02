package tbox

trait Show[A] {
   def show(a : A) : String
}
object Show {
   def apply[A](implicit ev : Show[A]) = ev
   implicit class ShowOps[A](a : A) {
      def show(implicit ev : Show[A]) = ev.show(a)
   }
}
