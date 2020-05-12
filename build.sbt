name := "Scorinator"

version := "1.0"

scalaVersion := "2.12.11"

mainClass in assembly := Some("com.webfarm.Scorinator.ScoreRunner")

assemblyJarName in assembly := "Scorinator.jar"

assemblyOutputPath in assembly := file("/Users/lmg42/Dropbox/GOOD/OCC/scorinator.jar")

libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.3.3",
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
	"ch.qos.logback" % "logback-classic" % "1.2.3",
	"org.slf4j" % "slf4j-api" % "1.7.30",
	"org.slf4j" % "slf4j-simple" % "1.7.30",
	"joda-time" % "joda-time" % "2.10.6",
	"com.github.tototoshi" %% "scala-csv" % "1.3.6"
)

scalacOptions += "-deprecation"


