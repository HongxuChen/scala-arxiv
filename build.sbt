name := "scala-arxiv"

organization := "info.hongxuchen"

version := "0.0.1"

scalaVersion := "2.12.1"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)