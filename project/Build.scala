import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform._
import ScalariformKeys._
import com.typesafe.sbt.pgp.PgpSettings.useGpg

object Build extends Build {

   lazy val base: Project = Project(
      "tbox",
      file("."),
      settings = commonSettings ++ Seq(
         libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _)
		)
   )

   lazy val pomStuff = {
     <url>https://github.com/ChrisNeveu/TBox</url>
     <licenses>
       <license>
         <name>BSD 3-Clause</name>
         <url>https://raw.githubusercontent.com/ChrisNeveu/TBox/master/LICENSE</url>
       </license>
     </licenses>
     <scm>
       <connection>scm:git:github.com/ChrisNeveu/TBox.git</connection>
       <developerConnection>scm:git:git@github.com:ChrisNeveu/TBox.git</developerConnection>
       <url>git@github.com:ChrisNeveu/TBox</url>
     </scm>
     <developers>
       <developer>
         <name>Chris Neveu</name>
         <url>chrisneveu.com</url>
       </developer>
     </developers>
   }

   def commonSettings = Defaults.defaultSettings ++ scalariformSettings ++
      Seq(
         organization := "com.chrisneveu",
         version      := "1.0.0-SNAPSHOT",
         scalaVersion := "2.11.6",
         scalacOptions ++= Seq(
            "-unchecked",
            "-deprecation",
            "-feature",
            "-language:higherKinds",
            "-language:postfixOps"
         ),
         useGpg := true,
         pomExtra := pomStuff,
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
