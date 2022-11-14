val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "warverse",
    version := IO.read(file("../version.txt")).trim,
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % Test,
    // Output paths
    assembly / mainClass := Some("it.unibo.warverse.Launcher"),
    assembly / assemblyOutputPath := file("target/warverse.jar"),
    Compile / doc / target := file("target/docs"),
    // Coverage settings
    jacocoReportSettings := JacocoReportSettings()
      .withFormats(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML),
    jacocoReportDirectory := file("target/coverage_report"),
    Global / gitHooksDir := Some(file("../.git/hooks"))
  )
// Write hooks before compiling
(Compile / compile) := ((Compile / compile) dependsOn writeHooks).value