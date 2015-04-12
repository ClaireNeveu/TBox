import sbt._
import sbt.Keys._

object Build extends Build {

   lazy val root: Project = Project(
      "root",
      file("."),
      aggregate = Seq(base, examples),
      settings = commonSettings ++ Seq(
         publishArtifact := false
      )
   )

   lazy val base: Project = Project(
      "tbox",
      file("tbox"),
      settings = commonSettings ++ Seq(
         version := "0.1-SNAPSHOT",
         libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _),
			libraryDependencies ++= Seq(
				"net.bytebuddy" % "byte-buddy" % "0.5.3"
			)
		)
   )

   lazy val examples: Project = Project(
      "examples",
      file("examples"),
      settings = commonSettings ++ Seq(
         version := "0.1-SNAPSHOT"
      )
   ).dependsOn(base)

   def commonSettings = Defaults.defaultSettings ++
      Seq(
         organization := "tbox"
       , version      := "0.0.1-SNAPSHOT"
       , scalaVersion := "2.11.6"
       , scalacOptions ++= Seq(
            "-unchecked"
          , "-deprecation"
          , "-feature"
          , "-language:higherKinds"
          , "-language:postfixOps"
          )
       )
}
