package tbox

import reflect.macros.blackbox.Context

private[tbox] object TBoxMacros {
	def instance[T[_]](c : Context)(implicit tt : c.WeakTypeTag[T[_]]) : c.Expr[T[TBox[T]]] = {
		import c.universe._
		/* 1. Determine Type Argument
		 * 2. Get all abstract members
		 * 3. For each abstract members create a DefDef
		 *    The DefDef should delegate to the tbox's
		 *    instance. Each argument that matched the
		 *    type arg should delegate to the tbox's
		 *    value.
		 * 4. Create a ClassDef with the list of DefDefs
		 *    as the body.
		 */
		val abstractMembers = tt.tpe.members
			.filter(_.isAbstract)
			.map { member =>
				val typ = member.asMethod.typeSignature
				val name = member.name.asInstanceOf[TermName]
				val arg = typ.asInstanceOf[MethodType].params.head.name.toTermName
				val targ = typ.asInstanceOf[MethodType].params.head.typeSignature
				val tboxType = tq"TBox[$tt]"
				val varg = ValDef(Modifiers(Flag.PARAM), arg, tboxType, EmptyTree)
				q"""def ${name}($varg) =
				$arg.instance.$name($arg.value)"""
			}
		val tree = c.parse(showCode(q"""new $tt[TBox[$tt]] {
			..$abstractMembers
			}"""))
		println(tree)
		c.Expr[T[TBox[T]]](tree)
	}

	def test[T[_]](c : Context)(implicit tt : c.WeakTypeTag[T[_]]) : c.Expr[T[Int]] = {
		import c.universe._
		val foo = c.Expr[T[Int]](c.parse(showCode(q"new $tt[Int] {}")))
		println(foo)
		foo
	}

	def tree[T[_]](c : Context)(a : c.Expr[Any])(implicit tt : c.WeakTypeTag[T[_]]) : c.Expr[Any] = {
		import c.universe._

		val t = weakTypeOf[T[_]]
		// If is TypeRef and not in method type args: replace with delegate.
		val tm = t.members.filter(_.isAbstract).map(_.asMethod.paramLists.map(_.map(_.typeSignature)))
		println("TYPE " + showRaw(tm))
		println("TYPE " + tm)

		println("//////////////////////////////////")
		println(showRaw(a.tree))
		a
	}

	def delegateMethod(c : Context)(arg : String, func : String) = {
		import c.universe._

		val body = Apply(
			Select(
				Select(Ident(TermName(arg)), TermName("instance")),
				TermName(func)),
			List(Select(Ident(TermName(arg)), TermName("value"))))

		DefDef(
			Modifiers(),
			TermName(func),
			List(),
			List(
				List(
					ValDef(
						Modifiers(Flag.PARAM),
						TermName(arg),
						TypeTree(),
						EmptyTree))),
			TypeTree(),
			body)
	}
}

/*
Trait instantiation

Block(
	List(
		ClassDef(
			Modifiers(FINAL),
			TypeName("$anon"),
			List(),
			Template(
				List(
					TypeTree(),
					TypeTree().setOriginal(Select(This(TypeName("Main")), tbox.examples.Main.Foo))), noSelfType, List(DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(), Block(List(Apply(Select(Super(This(TypeName("$anon")), typeNames.EMPTY), termNames.CONSTRUCTOR), List())), Literal(Constant(())))))))),
	Apply(Select(New(Ident(TypeName("$anon"))), termNames.CONSTRUCTOR), List()))


/
Block(
	List(
		ClassDef(
			Modifiers(FINAL),
			TypeName("$anon"),
			List(),
			Template(
				List(Select(Select(This(TypeName("java")), java.lang), java.lang.Object)),
				noSelfType,
				List(
					DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(), Block(List(Apply(Select(Super(This(TypeName("$anon")), typeNames.EMPTY), termNames.CONSTRUCTOR), List())), Literal(Constant(())))),
					DefDef(Modifiers(), TermName("foo"), List(), List(List(ValDef(Modifiers(PARAM), TermName("bar"), TypeTree().setOriginal(Select(Select(This(TypeName("scala")), scala.Predef), TypeName("String"))), EmptyTree))), TypeTree(), Literal(Constant(5))))))),
	Apply(Select(New(Ident(TypeName("$anon"))), termNames.CONSTRUCTOR), List()))
 */
