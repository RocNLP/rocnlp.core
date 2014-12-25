package rocnlp.core.lexicon.wordnet

/**
 * Created by Omid on 12/16/2014.
 */

import java.net.URL

import sbt.IO._

import java.io.File

object WordNetDownloader extends App {

  download

  def download = {
    var path = if (java.nio.file.Files.notExists(new File("lexicon-paths.properties").toPath()))
      "./data/lexicon/wordnet"
    else {
      val wordnetPath = scala.io.Source.fromFile("lexicon-paths.properties")
        .getLines()
        .find(_.startsWith("wordnet="))

      if (wordnetPath.size > 0)
        wordnetPath.map(_.replace("wordnet=", "")).head
      else
        "./data/lexicon/wordnet"
    }

    if (java.nio.file.Files.notExists(new File(path).toPath()))
      println("path does not exist, creating it in: " + path)

    println("start downloading and unziping, it will take some minutes, please wait ...")
    unzipURL(new URL("http://cs.rochester.edu/~omidb/data/wordnet-3.0.1.zip"), new File(path))
    println("Downloading and Extracting done")
  }
}
