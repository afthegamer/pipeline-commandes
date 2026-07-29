ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version      := "0.1.0"
ThisBuild / organization := "fr.formation"

lazy val root = (project in file("."))
  .settings(
    name := "pipeline-commandes-etudiant",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
