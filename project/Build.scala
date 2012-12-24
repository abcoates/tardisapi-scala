import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "tardisapi"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "mysql" % "mysql-connector-java" % "5.1.22", // [ABC]
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4", // [ABC]
      "com.roundeights" % "hasher" % "0.3" from "http://cloud.github.com/downloads/Nycto/Hasher/hasher_2.9.1-0.3.jar" // [ABC]
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
