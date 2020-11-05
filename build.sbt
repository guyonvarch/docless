
organization := "dividat"

name := "docless"

version := "0.1.1"

bintrayOrganization := Some("dividat")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

val circeVersion               = "0.13.0"
val enumeratumVersion          = "1.6.1"
val enumeratumCirceVersion     = "1.6.1"
val catsVersion                = "2.1.1"
val shapelessVersion           = "2.3.3"
val ammoniteVersion            = "2.2.0"
val scalaTestVersion           = "3.2.0"
val jsonSchemaValidatorVersion = "2.2.6"

scalaVersion := "2.13.3"

crossScalaVersions := Seq("2.12.8", "2.13.3")

useGpg := true
useGpgAgent := true

scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, n)) if n < 13 => Seq("-language:higherKinds")
  case _                      => Seq()
})

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect"         % scalaVersion.value,
  "com.chuusai"    %% "shapeless"            % shapelessVersion,

  "com.beachape"   %% "enumeratum"           % enumeratumVersion,
  "com.beachape"   %% "enumeratum-circe"     % enumeratumCirceVersion,
  "org.typelevel"  %% "cats-core"            % catsVersion,
  "org.typelevel"  %% "cats-kernel"          % catsVersion,
  "org.typelevel"  %% "cats-macros"          % catsVersion,
  "io.circe"       %% "circe-core"           % circeVersion,
  "io.circe"       %% "circe-parser"         % circeVersion,
  "io.circe"       %% "circe-generic"        % circeVersion,
  "org.scalatest"  %% "scalatest"            % scalaTestVersion % "test",
  "com.github.fge" % "json-schema-validator" % jsonSchemaValidatorVersion % "test",
  "com.lihaoyi"    % "ammonite"              % ammoniteVersion % "test" cross CrossVersion.full
)

val predef = Seq(
  "import com.timeout.docless.schema._",
  "import com.timeout.docless.swagger._",
  "import cats._",
  "import cats.syntax.all._",
  "import cats.instances.all._"
)

initialCommands in (Test, console) +=
  s"""
    |ammonite.Main(predef="${predef.mkString(";")}").run()
  """.stripMargin
