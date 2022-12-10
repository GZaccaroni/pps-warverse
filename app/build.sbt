val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "warverse",
    version := IO.read(file("../version.txt")).trim,
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-jackson" % "4.0.6",
      "io.monix" %% "monix" % "3.4.1",
      "com.github.dwickern" %% "scala-nameof" % "4.0.0" % "provided",
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "io.monix" %% "monix-testing-scalatest" % "0.4.0" % Test
    ),
    // Output paths
    assembly / mainClass := Some("it.unibo.warverse.Launcher"),
    assembly / assemblyOutputPath := file("target/warverse.jar"),
    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    Compile / doc / target := file("target/docs"),
    // Coverage settings
    jacocoReportSettings := JacocoReportSettings()
      .withFormats(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML),
    jacocoReportDirectory := file("target/coverage_report"),
    Global / gitHooksDir := Some(file("../.git/hooks")),
    Test / fork := true
  )
// Write hooks before compiling
(Compile / compile) := ((Compile / compile) dependsOn writeHooks).value
