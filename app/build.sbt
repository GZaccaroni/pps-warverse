val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "warverse",
    version := IO.read(file("../version.txt")),
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % Test,
    // Linter settings,
    wartremoverErrors ++= Warts.unsafe,
    assembly / assemblyOutputPath := file("target/warverse.jar"),
    Compile / doc / target := file("target/docs")
  )
