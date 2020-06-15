name := "Scorinator"

version := "1.1"

scalaVersion := "2.13.2"

mainClass in assembly := Some("com.webfarm.Scorinator.ScoreRunner")

assemblyJarName in assembly := "Scorinator.jar"

assemblyOutputPath in assembly := baseDirectory.value / "scorinator.jar"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.3",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.tototoshi" %% "scala-csv" % "1.3.6",
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1"
)

target in Compile in doc := baseDirectory.value / "api"

scalacOptions ++= Seq("-deprecation")


