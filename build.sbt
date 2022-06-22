val scala3Version = "3.1.2"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "EvolutionGameServer",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++=
      Dependencies.cats ++
      Dependencies.catsEffect ++
      Dependencies.logging ++
      Dependencies.fs2
  )
