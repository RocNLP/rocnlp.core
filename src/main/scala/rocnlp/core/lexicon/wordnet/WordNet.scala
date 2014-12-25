package rocnlp.core.lexicon.wordnet

import java.io.{FileNotFoundException, File}

import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import com.tinkerpop.gremlin.process.T._
import com.tinkerpop.gremlin.scala._
import rocnlp.core.structures.common._
import rocnlp.core.structures.common.AttributeImplicit._


/**
* Created by Omid on 12/8/2014.
*/


object WordNet {
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

  def apply(p:String) = path = p

  lazy val graph: Neo4jGraph = Neo4jGraph.open(path)
  lazy val gs = GremlinScala(graph)

  def disconnect = graph.close()

  /**
   * find the word from Wordnet
   * @param lemma of the word that you are searching for
   * @return a Word with list of wordnet synsets
   */
  def getWord(lemma:String):Word = {
    val v = gs.V.has(label,"word").has("lemma",lemma).toList()
    if (v.size == 0)
      Word(lemma,Attribute(Nil))
    else {
      val senses = gs.V.has(label, "word").has("lemma", lemma).out("synset")
        .toList().map(element => vertexToSynset(element))
      Word(v.head.value[String]("lemma"), senses)
    }
  }


  def getSynsets(lemma:String, pos:WordNetPOS):Option[List[Synset]] = {

    val v = gs.V.has(label,"word").has("lemma",lemma).toList()
    if (v.size == 0)
      None
    else
      Some(gs.V.has(label, "word").has("lemma", lemma).out("synset")
        .toList().map(element => vertexToSynset(element)).filter(_.wordnetPos == pos))
  }

  def getSynsets(pos:WordNetPOS):Option[List[Synset]] =
    Some(gs.V.has(label,"synset").has("wordnetPos",pos.value).toList().map(vertexToSynset(_)))

  def getSynset(tag:SenseTag):Option[Synset] = {
    val seneses = gs.V.has(label,"synset").has("senseKey",tag.value).toList()
    if(seneses.size > 0)
      Some(vertexToSynset(seneses.head))
    else
      None
  }

  def getRelation (synset:Synset, relation:WordNetRelation):Option[List[Synset]] = {
    val v = gs.V.has(label,"synset").has("senseKey",synset.senseKey).out(relation.value).toList()
    if(v.size > 0)
      Some(v.map(vertexToSynset(_)))
    else
      None
  }


  private def getSentence(w:ScalaVertex)= {
    var v = w
    var words = IndexedSeq.empty[Word]

    while(v.out("next").toList.size > 0) {
      v = v.out("next").head()
      //val vvv = v.outE().toList()
      if (v.out("senseTag").toList.size > 0) {
        val SK = SenseTag(v.out("senseTag").head().value[String]("senseKey"))
        val ww = Word(v.value[String]("lemma"),
          Attribute(WordNetPOS(v.value[String]("pos")) :: Index(v.value[Int]("sIndex")) :: SK :: Nil))
        words = words :+ ww
      }
      else {
        val ww = Word(v.value[String]("lemma"),
          Attribute(WordNetPOS(v.value[String]("pos")) :: Index(v.value[Int]("sIndex")) :: Nil))
        words = words :+ ww
      }
//      v = v.out("next").head()
    }
    new Sentence(words.map(_.lemma).mkString(" "), words)
  }

  private def vertexToSynset(v:ScalaVertex)= {
    def getDefs = {
      val defs = v.out("definition").toList
      //defs.map(d => getSentence(d.out("next").toList.get(0))).toIndexedSeq
      defs.map(d => getSentence(d)).toIndexedSeq
    }

    def getExmps= {
      val exps = v.out("example").toList
      //exps.map(d => getSentence(d.out("next").toList.get(0))).toIndexedSeq
      exps.map(d => getSentence(d)).toIndexedSeq
    }

    Synset(v.value[String]("senseKey"),v.value[String]("senseID")
      , v.value[String]("lemma").split(",").map(Word(_,Nil)).toSeq
      , v.value[String]("gloss"),WordNetPOS(v.value[String]("wordnetPos")),getDefs,getExmps)
  }

  // implicits for synsets
  implicit class SynsetImplicits(val s: Synset) {
    def ==>(relation: WordNetRelation):Option[List[Synset]] = {
      getRelation(s,relation)
    }
  }
}
