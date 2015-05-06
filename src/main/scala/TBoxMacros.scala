package tbox

import reflect.macros.whitebox.Context

private[tbox] object TBoxMacros {
   def instance[T[_]](c : Context)(implicit tt : c.WeakTypeTag[T[_]]) : c.Expr[T[TBox[T]]] = {
      import c.universe._
      val abstractMembers = tt.tpe.members.filter(_.isAbstract).map {
         case member : MethodSymbol ⇒
            member.typeSignature match {
               case typ : MethodType ⇒
                  val name : TermName = member.name.toTermName
                  val tboxType = tq"tbox.TBox[$tt]"
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
                  q"""def ${name}(..$funcArgs) = $tboxArg.instance.$name(..$bodyArgs)"""
               case _ ⇒ c.abort(NoPosition, "Unexpected abstract member type.")
            }
         case t ⇒ c.abort(NoPosition, "Unexpected abstract member.")
      }
      val tree = c.parse(showCode(q"""
         new $tt[tbox.TBox[$tt]] {
			   ..$abstractMembers
			}"""))
      c.Expr[T[TBox[T]]](tree)
   }
}
