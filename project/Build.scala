import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "tardisapi"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "mysql" % "mysql-connector-java" % "5.1.22" // [ABC]
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
