name := "das-alpha-taxservice-mock"

lazy val `das-alpha-taxservice-mock` = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)

  .enablePlugins(GitVersioning)
  .enablePlugins(GitBranchPrompt)

git.useGitDescribe := true

scalaVersion := "2.11.12"

PlayKeys.devSettings := Seq("play.server.http.port" -> "9002")

resolvers += Resolver.bintrayRepo("hmrc", "releases")

// need this because we've disabled the PlayLayoutPlugin. without it twirl templates won't get
// re-compiled on change in dev mode
PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value

scalafixDependencies in ThisBuild ++= Seq(
  "org.reactivemongo" %% "reactivemongo-scalafix" % "1.0.0")

libraryDependencies ++= Seq(
  ws,
  "com.nulab-inc" %% "play2-oauth2-provider" % "1.0.0",
  "uk.gov.hmrc" %% "domain" % "5.3.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.typelevel" %% "cats-core" % "0.9.0",
  "com.github.melrief" %% "pureconfig" % "0.1.9",
  "org.reactivemongo" %% "reactivemongo-play-json-compat" % "1.0.0-play25-rc.3",
  "org.reactivemongo" %% "reactivemongo" % "1.0.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "1.0.0-play25-rc.3",
  "org.reactivemongo" %% "reactivemongo-bson-api" % "1.0.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test
)
