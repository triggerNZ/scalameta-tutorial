import scala.meta._

object Test {
  def main(args: Array[String]): Unit = {
    val classpath = sys.props("sbt.paths.scrutinee.classes")
    val sourcepath = sys.props("sbt.paths.scrutinee.sources")
    implicit val c = Context(Artifact(classpath, sourcepath))
    c.sources.foreach(println)
  }
}