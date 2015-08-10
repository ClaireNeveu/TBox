package tbox

import language.experimental.macros

/** Represents a value erased to some type-class constraint. */
trait TBox[T[_]] {
   type ErasedType
   val value : ErasedType
   val instance : T[ErasedType]
}

object TBox {
   /**
    * Construct a TBox. An explicit type ascription is required.
    * The returned TBox has its underlying type erased.
    * {{{ val tbox = TBox[ToJson](5) }}}
    */
   def apply[T[_]] = new TBoxConstructor[T]()

   implicit def instance[T[_]] : T[TBox[T]] = macro TBoxMacros.instance[T]

   final class TBoxConstructor[T[_]] private[TBox] () {
      def apply[A](_value : A)(implicit ev : T[A]) : TBox[T] = new TBox[T] {
         type ErasedType = A
         val value = _value
         val instance = ev
      }
   }
}

/** Represents a value erased to two type-class constraints. */
trait TBox2[T1[_], T2[_]] { tbox ⇒
   type ErasedType
   val value : ErasedType
   val instance1 : T1[ErasedType]
   val instance2 : T2[ErasedType]

   /** Swaps the position of the constraints in the TBox. */
   def flip : TBox2[T2, T1] = new TBox2[T2, T1] {
      type ErasedType = tbox.ErasedType
      val value = tbox.value
      val instance1 = tbox.instance2
      val instance2 = tbox.instance1
   }

   /** Throws away the second type-class constraint, turning this into a TBox. */
   def reduce : TBox[T1] = new TBox[T1] {
      type ErasedType = tbox.ErasedType
      val value = tbox.value
      val instance = tbox.instance1
   }
}

object TBox2 {
   /**
    * Construct a TBox2. Explicit type ascriptions are required.
    * The returned TBox2 has its underlying type erased.
    * {{{ val tbox = TBox[ToJson, Show]("foobar") }}}
    */
   def apply[T1[_], T2[_]] = new TBox2Constructor[T1, T2]()

   implicit def instance1[T1[_], T2[_]] : T1[TBox2[T1, T2]] = macro TBoxMacros.instance2_1[T1, T2]
   implicit def instance2[T1[_], T2[_]] : T2[TBox2[T1, T2]] = macro TBoxMacros.instance2_2[T1, T2]

   final class TBox2Constructor[T1[_], T2[_]] private[TBox2] () {
      def apply[A](_value : A)(implicit ev1 : T1[A], ev2 : T2[A]) : TBox2[T1, T2] = new TBox2[T1, T2] {
         type ErasedType = A
         val value = _value
         val instance1 = ev1
         val instance2 = ev2
      }
   }
}

/** Represents a value erased to three type-class constraints. */
trait TBox3[T1[_], T2[_], T3[_]] { tbox ⇒
   type ErasedType
   val value : ErasedType
   val instance1 : T1[ErasedType]
   val instance2 : T2[ErasedType]
   val instance3 : T3[ErasedType]

   /** Swaps the position of the constraints in the TBox. */
   def flip : TBox3[T3, T2, T1] = new TBox3[T3, T2, T1] {
      type ErasedType = tbox.ErasedType
      val value = tbox.value
      val instance1 = tbox.instance3
      val instance2 = tbox.instance2
      val instance3 = tbox.instance1
   }

   /** Shifts the position of the constraints in the TBox. */
   def rotate : TBox3[T2, T3, T1] = new TBox3[T2, T3, T1] {
      type ErasedType = tbox.ErasedType
      val value = tbox.value
      val instance1 = tbox.instance2
      val instance2 = tbox.instance3
      val instance3 = tbox.instance1
   }

   /** Throws away the last type-class constraint, turning this into a TBox2. */
   def reduce : TBox2[T1, T2] = new TBox2[T1, T2] {
      type ErasedType = tbox.ErasedType
      val value = tbox.value
      val instance1 = tbox.instance1
      val instance2 = tbox.instance2
   }

   /** Throws away all but the first type-class constraint, turning this into a TBox. */
   def reduce2 : TBox[T1] = new TBox[T1] {
      type ErasedType = tbox.ErasedType
      val value = tbox.value
      val instance = tbox.instance1
   }
}

object TBox3 {
   /**
    * Construct a TBox3. Explicit type ascriptions are required.
    * The returned TBox3 has its underlying type erased.
    * {{{ val tbox = TBox[ToJson, Show, ToBytes](true) }}}
    */
   def apply[T1[_], T2[_], T3[_]] = new TBox3Constructor[T1, T2, T3]()

   implicit def instance1[T1[_], T2[_], T3[_]] : T1[TBox3[T1, T2, T3]] = macro TBoxMacros.instance3_1[T1, T2, T3]
   implicit def instance2[T1[_], T2[_], T3[_]] : T2[TBox3[T1, T2, T3]] = macro TBoxMacros.instance3_2[T1, T2, T3]
   implicit def instance3[T1[_], T2[_], T3[_]] : T3[TBox3[T1, T2, T3]] = macro TBoxMacros.instance3_3[T1, T2, T3]

   final class TBox3Constructor[T1[_], T2[_], T3[_]] private[TBox3] () {
      def apply[A](_value : A)(implicit ev1 : T1[A], ev2 : T2[A], ev3 : T3[A]) : TBox3[T1, T2, T3] = new TBox3[T1, T2, T3] {
         type ErasedType = A
         val value = _value
         val instance1 = ev1
         val instance2 = ev2
         val instance3 = ev3
      }
   }
}
