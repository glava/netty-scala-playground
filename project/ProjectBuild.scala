import sbt._
import sbt.Keys._
 
object ProjectBuild extends Build {
	
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

	val scalaz      = "org.scalaz" %% "scalaz-core" % "7.0.6"
	val scalatest   = "org.scalatest" %% "scalatest" % "2.1.6"
    val netty       = "io.netty" % "netty" % "3.9.2.Final"
    val akka        = "com.typesafe.akka" %% "akka-actor" % "2.3.4"
 
	val sharedSettings = (
        Defaults.defaultSettings
            ++ Seq(
            scalaVersion := "2.11.0",
            crossScalaVersions := Seq(scalaVersion.value),
 
            scalacOptions ++= Seq(
                "-target:jvm-1.7"
            ),
 
            javacOptions ++= Seq(
                "-source", "7",
                "-target", "7"
            )
        )
    )
 
	lazy val root = Project(
        id = "root",
        base = file("."),
        settings = sharedSettings
    ).settings(
        libraryDependencies ++= Seq(
        	scalaz,
        	scalatest,
            netty,
            akka
            )
        )
 
}
