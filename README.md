### scala.meta tutorial: Exploring semantics

[![Join the chat at https://gitter.im/scalameta/scalameta](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scalameta/scalameta?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Problem statement

In the [view-bounds guide](https://github.com/scalameta/tutorial/tree/view-bounds), we've discovered syntactic APIs of scala.meta. Without requiring any external dependencies, scala.meta is capable of robustly parsing, transforming and prettyprinting Scala code.

However, platform-independent functionality in scala.meta has its limits. Since developing a typechecker for Scala is a very hard task that requires significant time investments even by [the](http://lamp.epfl.ch/) [select](http://www.jetbrains.com/) [few](http://www.typesafe.com/) who can undertake it, we decided to cut corners.

In order to perform semantic operations (functionality that goes beyond obtaining and traversing program structure as written in source files), we require a context, i.e. an implicit value that conforms to [a special interface](https://github.com/scalameta/scalameta/blob/master/scalameta/semantic/src/main/scala/scala/meta/semantic/Context.scala).

Contexts are provided by platform-dependent libraries, called hosts, and in this tutorial we will explore semantics of scala.meta trees with scalahost, a scalac-based implementation of the aforementioned Context interface. After completing the tutorial, you will understand how to obtain semantic information about scala.meta trees both at compile time and at runtime.

### Anytime metaprogramming

In many metaprogramming frameworks, full semantic information about the program only exists at compile time, so, in order to fully explore semantics, it is necessary to write compiler plugins or use other means of compile-time reflection, e.g. macros.

One of the key innovations of scala.meta is the introduction of AST persistence that mandates saving typechecked abstract syntax trees into binaries produced by the compiler. Our scalac plugin saves scala.meta trees of the programs being compiled into resulting .class files and provides a way to load these trees back for inspection. See [#147](https://github.com/scalameta/scalameta/issues/147) to learn more about our serialization format and its compatibility with TASTY.

With the introduction of AST persistence, the restrictions on program introspection are lifted, and the distinction between compile-time and runtime metaprogramming becomes obsolete.
