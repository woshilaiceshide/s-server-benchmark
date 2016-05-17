organization := "woshilaiceshide"

name := "s-server-benchmark"

version := "1.0-SNAPSHOT"

compileOrder in Compile := CompileOrder.Mixed

transitiveClassifiers := Seq("sources")

EclipseKeys.withSource := true

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation","-optimise", "-encoding", "utf8", "-Yno-adapted-args")

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")

retrieveManaged := false

enablePlugins(JavaAppPackaging)

unmanagedSourceDirectories in Compile <+= baseDirectory( _ / "src" / "java" )

unmanagedSourceDirectories in Compile <+= baseDirectory( _ / "src" / "scala" )

//libraryDependencies in Test += "io.netty" % "netty-all" % "4.0.36.Final"
libraryDependencies += "io.netty" % "netty-all" % "4.0.36.Final"
libraryDependencies += "io.netty" % "netty-example" % "4.0.36.Final" exclude("io.netty", "netty-tcnative")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-agent" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-camel" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-contrib" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.4.5"
//libraryDependencies += "com.typesafe.akka" %% "akka-osgi" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-tck" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-distributed-data-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-typed-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-jackson-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-xml-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.5"
libraryDependencies += "com.typesafe.akka" %% "akka-typed-experimental" % "2.4.5"

libraryDependencies ++= {
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%   "spray-routing" % sprayV,
    "io.spray"            %%   "spray-caching" % sprayV,
    "io.spray"            %%   "spray-can"     % sprayV,
    "io.spray"            %%   "spray-client"  % sprayV,
    "io.spray"            %%   "spray-http"    % sprayV,
    "io.spray"            %%   "spray-httpx"   % sprayV,
    "io.spray"            %%   "spray-io"      % sprayV,
    "io.spray"            %%   "spray-util"    % sprayV
  )
}
