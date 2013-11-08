name := "eventual"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  // ReactiveMongo dependencies
  "org.reactivemongo" %% "reactivemongo" % "0.9" exclude("org.scala-stm", "scala-stm_2.10.0"),
  // ReactiveMongo Play plugin dependencies
  "org.reactivemongo" %% "play2-reactivemongo" % "0.9" exclude("org.scala-stm", "scala-stm_2.10.0")
)

play.Project.playScalaSettings
