import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform._
import ScalariformKeys._
import com.typesafe.sbt.pgp.PgpSettings.useGpg
import wartremover._

object Build extends Build {

   lazy val base: Project = Project(
      "tbox",
      file("."),
      settings = commonSettings ++ Seq(
         libraryDependencies ++= Seq(
            "org.scala-lang" % "scala-compiler" % scalaVersion.value,
            "org.scalatest" %% "scalatest" % "2.2.4" % "test")
		)
   )

   lazy val pomStuff = {
     <url>https://github.com/ClaireNeveu/TBox</url>
     <licenses>
       <license>
         <name>BSD 3-Clause</name>
         <url>https://raw.githubusercontent.com/ClaireNeveu/TBox/master/LICENSE</url>
       </license>
     </licenses>
     <scm>
       <connection>scm:git:github.com/ClaireNeveu/TBox.git</connection>
       <developerConnection>scm:git:git@github.com:ClaireNeveu/TBox.git</developerConnection>
       <url>git@github.com:ClaireNeveu/TBox</url>
     </scm>
     <developers>
       <developer>
         <name>Claire Neveu</name>
         <url>claireneveu.com</url>
       </developer>
     </developers>
   }

   def commonSettings = Defaults.defaultSettings ++ scalariformSettings ++ wartremoverSettings ++
      Seq(
         organization := "com.claireneveu",
         version      := "1.1.1",
         scalaVersion := "2.11.6",
         scalacOptions ++= Seq(
            "-unchecked",
            "-deprecation",
            "-feature",
            "-language:higherKinds",
            "-language:postfixOps"
         ),
         wartremoverErrors ++= Warts.allBut(
            Wart.Any,
            Wart.Nothing,
            Wart.Throw,
            Wart.NonUnitStatements,
            Wart.AsInstanceOf),
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
