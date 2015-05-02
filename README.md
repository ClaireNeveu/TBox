TBox provides a data-type to erase types to constraints (context bounds). 
Instances of the type-class used are automatically created for `TBox`.

Given `Int`, `String`, and `Double` you can erase their type to `Show` and 
thus pass around a well-typed list of objects that can be displayed.
```scala
val showables : List[TBox[Show]] = List(TBox[Show](1), TBox[Show]("two"), TBox[Show](3.0))
showables.foreach(show)
// Prints
// > 1
// > two
// > 3.0
```

## Limitations
TBox can only create instances for single-parameter type-classes where each 
abstract method takes a value of the parameterized type as an argument.

Therefore you cannot instantiate a `TBox[Num]` (as defined in Haskell) because `fromInteger` 
cannot be defined in `Num[TBox[Num]]`.
