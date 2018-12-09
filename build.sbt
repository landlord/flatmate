import com.typesafe.sbt.packager.docker._

enablePlugins(AutomateHeaderPlugin, DockerPlugin, GitVersioning, GitBranchPrompt)

name := "flatmate"

organization := "au.com.titanclass"

startYear := Some(2018)
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

publishMavenStyle := true

crossPaths := false

autoScalaLibrary := false

libraryDependencies ++= Seq(
  "junit"        % "junit"           % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

headerLicense := Some(HeaderLicense.Custom("Copyright (c) Titan Class P/L, 2018"))

git.useGitDescribe := true

mappings in Docker := assembly.value.pair(Path.flatRebase("/opt/docker/lib"))
dockerCommands := Seq(
  Cmd("FROM", "scratch"),
  Cmd("COPY", "opt/docker", "/opt/docker")
)
