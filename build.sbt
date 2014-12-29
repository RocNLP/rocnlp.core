name := "core"

organization := "org.rocnlp"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += Resolver.url("typesafe-ivy-repo", url("http://typesafe.artifactoryonline.com/typesafe/releases"))(Resolver.ivyStylePatterns)

//libraryDependencies <++= scalaVersion { scalaVersion =>
//  val gremlinVersion = "3.0.0.M5"
//  Seq(
//    "com.michaelpollmeier" %% "gremlin-scala" % gremlinVersion,
//    "com.tinkerpop" % "neo4j-gremlin" % gremlinVersion
//  )
//}

libraryDependencies <++= scalaVersion { scalaVersion =>
  val gremlinVersion = "3.0.0.M6"
  Seq(
    "com.michaelpollmeier" %% "gremlin-scala" % "3.0.0.M6c" exclude("org.slf4j", "slf4j-log4j12"),
    "com.tinkerpop" % "neo4j-gremlin" % gremlinVersion
  )
}


libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.4"

libraryDependencies += "org.scala-sbt" % "io" % "0.13.6"

libraryDependencies += "com.assembla.scala-incubator" %% "graph-core" % "1.9.0"


//publishing
//
publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http:rocnlp.org</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:rocnlp/rocnlp.core.git</url>
      <connection>scm:git:git@github.com:rocnlp/rocnlp.core.git</connection>
    </scm>
    <developers>
      <developer>
        <id>omidb</id>
        <name>Omid Bakhshandeh</name>
        <url>http://cs.rochester.edu/~omidb</url>
      </developer>
    </developers>)