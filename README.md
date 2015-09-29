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

### Configuring the build system

We will be using a two-project configuration that consists of `scrutinee`, a project under inspection, and `explorer`, a scala.meta-based tool that loads semantic information from the classpath of `scrutinee`. A bit below, you can find relevant excerpts of the build file, and here's the highlight of the most important points:

  1. To serialize ASTs of your projects:
    1. Use the `"org.scalameta" %% "scalahost" % "..."` compiler plugin.
    1. Specify the `-Ybackend:GenBCode` compiler option (this is necessary for Scala 2.11.7, but we hope to lift this restriction in Scala 2.11.8).
  1. To deserialize ASTs of your projects:
    1. Reference `"org.scalameta" %% "scalahost" % "..."` as a library.
    1. Obtain a mandatory classpath (because trees are serialized into class files) and an optional sourcepath (because we only serialize semantics of the code, not its surface syntax). You can take a look at build.sbt to see how we're doing this, but feel free to obtain this configuration however you see fit.
    1. Create a context from a classpath and a sourcepath and use its functionality as described in the later sections of this guide.

```scala
lazy val scrutinee = Project(
  id = "scrutinee",
  base = file("scrutinee")
) settings (
  sharedSettings: _*
) settings (
  addCompilerPlugin("org.scalameta" % "scalahost" % "..." cross CrossVersion.full),
  scalacOptions += "-Ybackend:GenBCode"
)

lazy val explorer = Project(
  id = "explorer",
  base = file("explorer")
) settings (
  sharedSettings: _*
) settings (
  libraryDependencies += "org.scalameta" % "scalahost" % "..." cross CrossVersion.full
)
```

### Setting up imports

Much like in the [view-bounds guide](https://github.com/scalameta/tutorial/tree/view-bounds), environment setup is more or less simple. First, we need to specify one wildcard import, and then we need to create a context (the latter is an additional step in comparison with view-bounds, and it might be somewhat challenging).

1) `import scala.meta._` not only brings all the platform-independent APIs in scope (as explained in view-bounds), but also imports platform-specific implementations of those APIs provided by scalahost.

2) `implicit val c = Context(Artifact(classpath, sourcepath))` creates an implicit context that provides a scalac-based implementation of platform-independent semantic services defined in scala.meta. All semantic APIs of scala.meta take a context as an implicit parameter, so it's really central to the whole business.

```scala
import scala.meta._

object Test {
  def main(args: Array[String]): Unit = {
    val classpath = sys.props("sbt.paths.scrutinee.classes")
    val sourcepath = sys.props("sbt.paths.scrutinee.sources")
    implicit val c = Context(Artifact(classpath, sourcepath))
    ???
  }
}
```

### Loading serialized trees

Given that your classpath has been compiled with the `scalahost` compiler plugin (which saves attributed ASTs into the resulting classfiles), and that you have successfully instantiated a context, loading scala.meta trees that correspond to the classpath is as writing `c.sources`:

```scala
c.sources.foreach(println)
```

```
11:44 ~/Projects/tutorial (exploring-semantics)$ sbt run
[info] Set current project to tutorial (in build file:~/Projects/tutorial/)
[info] Set current project to root (in build file:~/Projects/tutorial/)
[info] Running Test
/** scaladoc for bar */
class Bar

/**
 * Scaladoc for Foo class
 */
class Foo extends Bar {
  def bar(a: Int) = a
}

```
