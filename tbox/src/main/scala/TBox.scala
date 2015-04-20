package tbox

import language.experimental.macros
import scala.reflect.runtime.universe._

trait TBox[T[_]] {
   type ErasedType
   val value : ErasedType
   val instance : T[ErasedType]
}
object TBox {
   def apply[T[_]] = new TBoxConstructor[T]

   implicit def instance[T[_]] : T[TBox[T]] = macro TBoxMacros.instance[T]

   class TBoxConstructor[T[_]] private[TBox] {
      def apply[A](_value : A)(implicit ev : T[A]) : TBox[T] = new TBox[T] {
         type ErasedType = A
         val value = _value
         val instance = ev
      }
   }
}
