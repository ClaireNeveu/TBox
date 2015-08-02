package tbox

import reflect.macros.whitebox.Context

private[tbox] object TBoxMacros {
   def instance[T[_]](_c : Context)(implicit tt : _c.WeakTypeTag[T[_]]) : _c.Expr[T[TBox[T]]] =
      (new TBoxMacros { val c : _c.type = _c }).instance[T]

   def instance2_1[T1[_], T2[_]](_c : Context)(implicit tt : _c.WeakTypeTag[T1[_]], tt2 : _c.WeakTypeTag[T2[_]]) : _c.Expr[T1[TBox2[T1, T2]]] =
      (new TBoxMacros { val c : _c.type = _c }).instance2_1[T1, T2]

   def instance2_2[T1[_], T2[_]](_c : Context)(implicit tt : _c.WeakTypeTag[T1[_]], tt2 : _c.WeakTypeTag[T2[_]]) : _c.Expr[T1[TBox2[T1, T2]]] =
      (new TBoxMacros { val c : _c.type = _c }).instance2_2[T1, T2]

   def instance3_1[T1[_], T2[_], T3[_]](_c : Context)(
      implicit t1 : _c.WeakTypeTag[T1[_]],
      t2 : _c.WeakTypeTag[T2[_]],
      t3 : _c.WeakTypeTag[T3[_]]) : _c.Expr[T1[TBox3[T1, T2, T3]]] =

      (new TBoxMacros { val c : _c.type = _c }).instance3_1[T1, T2, T3]

   def instance3_2[T1[_], T2[_], T3[_]](_c : Context)(
      implicit t1 : _c.WeakTypeTag[T1[_]],
      t2 : _c.WeakTypeTag[T2[_]],
      t3 : _c.WeakTypeTag[T3[_]]) : _c.Expr[T1[TBox3[T1, T2, T3]]] =

      (new TBoxMacros { val c : _c.type = _c }).instance3_2[T1, T2, T3]

   def instance3_3[T1[_], T2[_], T3[_]](_c : Context)(
      implicit t1 : _c.WeakTypeTag[T1[_]],
      t2 : _c.WeakTypeTag[T2[_]],
      t3 : _c.WeakTypeTag[T3[_]]) : _c.Expr[T1[TBox3[T1, T2, T3]]] =

      (new TBoxMacros { val c : _c.type = _c }).instance3_3[T1, T2, T3]
}

private[tbox] trait TBoxMacros {
   val c : Context

   import c.universe._

   private def instanceImpl(
      instanceIndex : Option[Int],
      tboxType : Tree,
      typeClassTag : WeakTypeTag[_]) : Tree = {

      val abstractMembers = typeClassTag.tpe.members.filter(_.isAbstract).map {
         case member : MethodSymbol ⇒
            member.typeSignature match {
               case typ : MethodType ⇒
                  val name : TermName = member.name.toTermName
                  val tboxArg = typ.params.find(_.typeSignature.typeSymbol.asType.isAbstractType)
                     .getOrElse(c.abort(NoPosition, "Each abstract method must take a tbox."))
                     .name.toTermName
                  val args = typ.params.map { param ⇒
                     val pType = param.typeSignature.typeSymbol.asType
                     if (pType.isAbstractType) // Parameter is a TBox.
                        Select(Ident(param.name.toTermName), TermName("value")) ->
                           ValDef(
                              Modifiers(Flag.PARAM),
                              param.name.toTermName,
                              tboxType,
                              EmptyTree)
                     else
                        Ident(param.name.toTermName) ->
                           ValDef(
                              Modifiers(Flag.PARAM),
                              param.name.toTermName,
                              TypeTree(param.typeSignature),
                              EmptyTree)
                  }
                  val funcArgs : List[Tree] = args.map(_._2)
                  val bodyArgs : List[Tree] = args.map(_._1)
                  val instance = newTermName("instance" + instanceIndex.getOrElse(""))
                  q"""def ${name}(..$funcArgs) = $tboxArg.$instance.$name(..$bodyArgs)"""
               case _ ⇒ c.abort(NoPosition, "Unexpected abstract member type.")
            }
         case t ⇒ c.abort(NoPosition, "Unexpected abstract member.")
      }
      c.parse(showCode(q"""
         new $typeClassTag[$tboxType] {
            ..$abstractMembers
         }"""))
   }

   def instance[T[_]](implicit tt : c.WeakTypeTag[T[_]]) : c.Expr[T[TBox[T]]] =
      c.Expr[T[TBox[T]]](instanceImpl(None, tq"tbox.TBox[$tt]", tt))

   def instance2_1[T1[_], T2[_]](implicit t1 : c.WeakTypeTag[T1[_]], t2 : c.WeakTypeTag[T2[_]]) : c.Expr[T1[TBox2[T1, T2]]] =
      c.Expr[T1[TBox2[T1, T2]]](instanceImpl(Some(1), tq"tbox.TBox2[$t1, $t2]", t1))

   def instance2_2[T1[_], T2[_]](implicit t1 : c.WeakTypeTag[T1[_]], t2 : c.WeakTypeTag[T2[_]]) : c.Expr[T1[TBox2[T1, T2]]] =
      c.Expr[T1[TBox2[T1, T2]]](instanceImpl(Some(2), tq"tbox.TBox2[$t1, $t2]", t2))

   def instance3_1[T1[_], T2[_], T3[_]](
      implicit t1 : c.WeakTypeTag[T1[_]],
      t2 : c.WeakTypeTag[T2[_]],
      t3 : c.WeakTypeTag[T3[_]]) : c.Expr[T1[TBox3[T1, T2, T3]]] =

      c.Expr[T1[TBox3[T1, T2, T3]]](instanceImpl(Some(1), tq"tbox.TBox3[$t1, $t2, $t3]", t1))

   def instance3_2[T1[_], T2[_], T3[_]](
      implicit t1 : c.WeakTypeTag[T1[_]],
      t2 : c.WeakTypeTag[T2[_]],
      t3 : c.WeakTypeTag[T3[_]]) : c.Expr[T1[TBox3[T1, T2, T3]]] =

      c.Expr[T1[TBox3[T1, T2, T3]]](instanceImpl(Some(2), tq"tbox.TBox3[$t1, $t2, $t3]", t2))

   def instance3_3[T1[_], T2[_], T3[_]](
      implicit t1 : c.WeakTypeTag[T1[_]],
      t2 : c.WeakTypeTag[T2[_]],
      t3 : c.WeakTypeTag[T3[_]]) : c.Expr[T1[TBox3[T1, T2, T3]]] =

      c.Expr[T1[TBox3[T1, T2, T3]]](instanceImpl(Some(3), tq"tbox.TBox3[$t1, $t2, $t3]", t3))
}
