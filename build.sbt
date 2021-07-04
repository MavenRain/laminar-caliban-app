enablePlugins(ScalaJSPlugin)

name := "Laminar Caliban App"
scalaVersion := "3.0.0"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "com.github.ghostdogpr" %%% "caliban-client" % "1.0.1",
  "com.raquo" %%% "laminar" % "0.13.0",
  ("org.scala-js" %%% "scalajs-dom" % "1.1.0")
    .cross(CrossVersion.for3Use2_13),
  ("dev.zio" %%% "zio" % "1.0.9")
    .cross(CrossVersion.for3Use2_13)
)

// Add support for the DOM in `run` and `test`
jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()

// uTest settings
libraryDependencies +=
  ("com.lihaoyi" %%% "utest" % "0.7.9" % "test")
    .cross(CrossVersion.for3Use2_13)
testFrameworks += new TestFramework("utest.runner.Framework")
