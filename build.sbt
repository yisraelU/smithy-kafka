ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"
Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("fmt", ";scalafmtSbt;scalafmtAll;")
val modulePrefix = "smithy4s-kafka"

lazy val root = (project in file("."))
  .settings(
    name := modulePrefix
  )
  .aggregate(protocol, kafka)

lazy val protocol = (project in file("modules/protocol"))
  .settings(
    name := s"$modulePrefix-protocol",
    libraryDependencies ++= Seq(Dependencies.Smithy.model),
    Compile / packageSrc / mappings := (Compile / packageSrc / mappings).value
      .filterNot {
        case (file, path) =>
          path.equalsIgnoreCase("META-INF/smithy/manifest")
      },
    autoScalaLibrary := false
  )

lazy val kafka = (project in file("modules/kafka"))
  .settings(
    name := s"$modulePrefix",
    libraryDependencies ++= Seq(
      Dependencies.fs2Kafka,
      Dependencies.vulcan,
      Dependencies.Smithy4s.core,
      Dependencies.Smithy4s.json
    )
  )
  .dependsOn(protocol, avro)

lazy val avro = (project in file("modules/avro"))
  .settings(
    name := s"$modulePrefix-avro",
    libraryDependencies ++= Seq(
      Dependencies.vulcan,
      Dependencies.vulcanGeneric,
      Dependencies.Smithy4s.core,
      Dependencies.munit
    )
  )
  .dependsOn(protocol)
