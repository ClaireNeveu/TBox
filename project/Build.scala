import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform._
import ScalariformKeys._

object Build extends Build {

   lazy val base: Project = Project(
      "tbox",
      file("."),
      settings = commonSettings ++ Seq(
         libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _)
		)
   )

   def commonSettings = Defaults.defaultSettings ++ scalariformSettings ++
      Seq(
         organization := "tbox",
         version      := "1.0.0",
         scalaVersion := "2.11.6",
         scalacOptions ++= Seq(
            "-unchecked",
            "-deprecation",
            "-feature",
            "-language:higherKinds",
            "-language:postfixOps"
         ),
         ScalariformKeys.preferences := ScalariformKeys.preferences.value
            .setPreference(IndentSpaces, 3)
            .setPreference(SpaceBeforeColon, true)
            .setPreference(PreserveDanglingCloseParenthesis, true)
            .setPreference(RewriteArrowSymbols, true)
            .setPreference(DoubleIndentClassDeclaration, true)
            .setPreference(AlignParameters, true)
            .setPreference(AlignSingleLineCaseStatements, true)
       )
}
