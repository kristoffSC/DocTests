val scala2138 = "2.11.12"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := scala2138

lazy val commonSettings = Seq(
  organization := "org.home",
  scalaVersion := scala2138,
  fork := true,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked"),
  scalacOptions ++= Seq("-target:jvm-1.8"),
  // Configurations to speed up tests and reduce memory footprint
  Test / javaOptions ++= Seq(
    "-Dspark.ui.enabled=false",
    "-Dspark.ui.showConsoleProgress=false",
    "-Dspark.databricks.delta.snapshotPartitions=2",
    "-Dspark.sql.shuffle.partitions=5",
    "-Ddelta.log.cacheSize=3",
    "-Dspark.sql.sources.parallelPartitionDiscovery.parallelism=5",
    "-Xmx1024m"
  )
)

lazy val DocModule = (project in file("DocModule"))
  .enablePlugins(GenJavadocPlugin, JavaUnidocPlugin)
  .settings(
    name := "DocModule",
    scalaVersion := "2.11.12",
    commonSettings,
    Test / publishArtifact := false,
    crossPaths := false,
    JavaUnidoc / unidoc / javacOptions := Seq(
      "-public",
      "-windowtitle", "Doc Test" + version.value.replaceAll("-SNAPSHOT", "") + " JavaDoc",
      "-noqualifier", "java.lang",
      "-tag", "implNote:a:Implementation Note:",
      "-Xdoclint:all"
    ),
    JavaUnidoc / unidoc /  unidocAllSources := {
      (JavaUnidoc / unidoc / unidocAllSources).value
        // exclude internal classes
        .map(_.filterNot(_.getCanonicalPath.contains("/internal/")))
        .map(_.filterNot(_.getCanonicalPath.contains("\\internal\\"))) // For Windows Env
    },
    // Ensure unidoc is run with tests. Must be cleaned before test for unidoc to be generated.
    (Test / test) := ((Test / test) dependsOn (Compile / unidoc)).value
)