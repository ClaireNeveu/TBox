package tbox

trait ToJson[A] {
   def toJson(a : A) : String
}
object ToJson {
   def apply[A](implicit ev : ToJson[A]) = ev
   implicit class ToJsonOps[A](a : A) {
      def toJson(implicit ev : ToJson[A]) = ev.toJson(a)
   }
}
