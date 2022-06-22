import sbt._

object Dependencies {
  object Versions {
    val cats       = "2.7.0"
    val catsEffect = "3.3.5"
    val circe      = "0.15.0"
    val fs2        = "3.2.8"
    val log4cats   = "2.3.2"
    val logback    = "1.2.11"
    val slf4j      = "1.7.36"
  }

  val cats = Seq(
    "org.typelevel" %% "cats-core" % Versions.cats,
    "org.typelevel" %% "cats-free" % Versions.cats,
  )

  val catsEffect = Seq(
    "org.typelevel" %% "cats-effect"      % Versions.catsEffect,
    "org.typelevel" %% "cats-effect-laws" % Versions.catsEffect,
  )

  val logging = Seq(
    "org.typelevel" %% "log4cats-core"   % Versions.log4cats,
    "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats,
    "org.slf4j"      % "slf4j-api"       % Versions.slf4j,
    "ch.qos.logback" % "logback-classic" % Versions.logback
  )

  val fs2 = Seq(
    "co.fs2" %% "fs2-core" % Versions.fs2,
    "co.fs2" %% "fs2-io"   % Versions.fs2,
  )
}
