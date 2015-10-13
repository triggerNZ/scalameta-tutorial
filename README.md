### scala.meta tutorial

[![Join the chat at https://gitter.im/scalameta/scalameta](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scalameta/scalameta?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Target audience

  * :white_check_mark: Tool authors
    * Want to gather information about code?
    * Want to generate programs from scratch?
    * ...or by modifying existing ones?
    * Then scala.meta is what you need!
  * :x: Macro writers
    * No macro support for 0.1
    * However, both def macros and macro annotations are tentatively planned for 1.0

### Killer feature

:+1: Trees that work
  * No desugarings
  * Precise range positions
  * Immutable semantic attributes
  * Check out [State of the Meta, Summer 2015](http://scalamacros.org/paperstalks/2015-06-09-StateOfTheMetaSummer2015.pdf) for architectural details
  * Or read on to observe this in practice

### Potential risks
  * This is pre-release software
  * Layout inference needs work ([#164](https://github.com/scalameta/scalameta/issues/164))
  * Semantic infrastructure is far from completion ([#148](https://github.com/scalameta/scalameta/issues/148))
  * Performance could use some improvements ([#149](https://github.com/scalameta/scalameta/issues/149#issuecomment-110476298) and [#152](https://github.com/scalameta/scalameta/issues/152))
  * Most of that is planned to be [addressed in 0.1](https://github.com/scalameta/scalameta/milestones/0.1)

### Practical guides
  * Rewriting view bounds ([/tree/view-bounds](https://github.com/scalameta/tutorial/tree/view-bounds))
  * Exploring semantics ([/tree/exploring-semantics](https://github.com/scalameta/tutorial/tree/exploring-semantics))
  * Advanced dendrology ([/tree/advanced-dendrology](https://github.com/scalameta/tutorial/tree/advanced-dendrology))
   

### SBT instructions

To get started with scalameta, add the following to your `build.sbt`:

```
libraryDependencies +=  "org.scalameta" %% "scalameta" % "0.1.0-M0"
```
