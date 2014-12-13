name := "rocnlp.lexicon"

version := "0.1"

scalaVersion := "2.10.4"

libraryDependencies <++= scalaVersion { scalaVersion =>
  val gremlinVersion = "3.0.0.M5"
  Seq(
    "com.michaelpollmeier" %% "gremlin-scala" % gremlinVersion,
    "com.tinkerpop" % "neo4j-gremlin" % gremlinVersion
  )
}

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.4"