lazy val sharedSettings = Seq(
  scalaVersion := "2.11.7"
)

lazy val root = Project(
  id = "root",
  base = file("root")
) settings (
  run in Compile <<= run in explorer in Compile
) aggregate (
  scrutinee,
  explorer
)

lazy val scrutinee = Project(
  id = "scrutinee",
  base = file("scrutinee")
) settings (
  sharedSettings: _*
) settings (
  addCompilerPlugin("org.scalameta" % "scalahost" % "0.0.4" cross CrossVersion.full),
  scalacOptions += "-Ybackend:GenBCode"
)

lazy val explorer = Project(
  id = "explorer",
  base = file("explorer")
) settings (
  sharedSettings: _*
) settings (
  libraryDependencies += "org.scalameta" % "scalahost" % "0.0.4" cross CrossVersion.full,
  fork in run := true,
  javaOptions in run ++= {
    val sbt_classpath = (fullClasspath in scrutinee in Compile).value
    val classpath = sbt_classpath.map(_.data.getAbsolutePath).mkString(java.io.File.pathSeparator)
    val sourcepath = (sourceDirectory in scrutinee in Compile).value.getAbsolutePath
    Seq(s"-Dsbt.paths.scrutinee.classes=$classpath", s"-Dsbt.paths.scrutinee.sources=$sourcepath")
  }
)
