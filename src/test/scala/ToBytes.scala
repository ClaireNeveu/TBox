package tbox

trait ToBytes[A] {
   def toBytes(a : A) : Array[Byte]
}
object ToBytes {
   def apply[A](implicit ev : ToBytes[A]) = ev
   implicit class ToBytesOps[A](a : A) {
      def toBytes(implicit ev : ToBytes[A]) = ev.toBytes(a)
   }
}
