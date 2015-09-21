### scala.meta tutorial: Exploring semantics

[![Join the chat at https://gitter.im/scalameta/scalameta](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scalameta/scalameta?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Problem statement

In the [view-bounds guide](https://github.com/scalameta/tutorial/tree/view-bounds), we've discovered syntactic APIs of scala.meta. Without requiring any external dependencies, scala.meta is capable of robustly parsing, transforming and prettyprinting Scala code.

However, platform-independent functionality in scala.meta has its limits. Since developing a typechecker for Scala is a very hard task that requires significant time investments even by [the](http://lamp.epfl.ch/) [select](http://www.jetbrains.com/) [few](http://www.typesafe.com/) who can undertake it, we decided to cut corners.

In order to perform semantic operations (functionality that goes beyond obtaining and traversing program structure as written in source files), we require a context, i.e. an implicit value that conforms to [a special interface](https://github.com/scalameta/scalameta/blob/master/scalameta/semantic/src/main/scala/scala/meta/semantic/Context.scala).

Contexts are provided by platform-dependent libraries, called hosts, and in this tutorial we will explore semantics of scala.meta trees with scalahost, a scalac-based implementation of the aforementioned Context interface. After completing the tutorial, you will understand how to obtain semantic information about scala.meta trees both at compile time and at runtime.