import sbt._

object Dependencies {

  val avro          = "org.apache.avro"  % "avro"           % "1.11.0"
  val circeCore     = "io.circe"        %% "circe-core"     % "0.14.1"
  val munit         = "org.scalameta"   %% "munit"          % "0.7.29" % Test
  val decline       = "com.monovore"    %% "decline"        % "2.4.1"
  val fs2Kafka      = "com.github.fd4s" %% "fs2-kafka"      % "3.2.0"
  val vulcan        = "com.github.fd4s" %% "vulcan"         % "1.9.0"
  val vulcanGeneric = "com.github.fd4s" %% "vulcan-generic" % "1.9.0"  % Test

  object Smithy4s {
    val core = "com.disneystreaming.smithy4s" %% "smithy4s-core" % "0.18.3"
    val json = "com.disneystreaming.smithy4s" %% "smithy4s-json" % "0.18.3"
  }

  object Smithy {
    val version = "1.40.0"
    val model   = "software.amazon.smithy" % "smithy-model" % version
    val build   = "software.amazon.smithy" % "smithy-build" % version
    val all     = Seq(model, build)
  }

}
