package rocnlp.core.lexicon.wordnet

import java.io.{FileNotFoundException, File}

import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import com.tinkerpop.gremlin.process.T._
import com.tinkerpop.gremlin.scala._
import rocnlp.core.structures.common._
import rocnlp.core.structures.common.AttributeImplicit._
import com.tinkerpop.gremlin.process.Traverser
import com.tinkerpop.gremlin.scala._
import rocnlp.core.structures.trees.Syntax.stanford.CollapsedCCDependency

import scalax.collection.edge.LDiEdge


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

  lazy val graph = Neo4jGraph.open(path)
  lazy val gs = GremlinScala(graph)

  def disconnect = {
    if (graph != null)
      graph.close()
  }

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


  def getSynsets(lemma:String, pos:WordNetPOS):Option[IndexedSeq[Synset]] = {
    val v = gs.V.has(label,"word").has("lemma",lemma).toList()
    if (v.size == 0)
      None
    else {
      val oute = gs.V.has(label, "word").has("lemma", lemma).outE("synset").toList()
      val tups = oute.map(e => (vertexToSynset(e.inV.toList.get(0)),e.value[Int]("senseNumber")))
        .filter(_._1.wordnetPos== pos)
      Some(tups.sortBy(_._2).map(_._1).toIndexedSeq)
    }
//      Some(gs.V.has(label, "word").has("lemma", lemma).out("synset")
//        .toList().map(element => vertexToSynset(element)).filter(_.wordnetPos == pos))
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

  def getRelation(synset:Synset, relation:WordNetRelation):Option[List[Synset]] = {
    val v = gs.V.has(label,"synset").has("senseKey",synset.senseKey).out(relation.value).toList()
    if(v.size > 0)
      Some(v.map(vertexToSynset(_)))
    else
      None
  }


//  private def getSentence(w:ScalaVertex)= {
//    var v = w
//    var words = IndexedSeq.empty[Word]
//
//    while(v.out("next").toList.size > 0) {
//      v = v.out("next").head()
//      //val vvv = v.outE().toList()
//      if (v.out("senseTag").toList.size > 0) {
//        val SK = SenseTag(v.out("senseTag").head().value[String]("senseKey"))
//        val ww = Word(v.value[String]("lemma").get,
//          Attribute(PenPOS(v.value[String]("pos").get) :: Index(v.value[Int]("sIndex").get) :: SK :: Nil))
//        words = words :+ ww
//      }
//      else {
//        val ww = Word(v.value[String]("lemma").get,
//          Attribute(PenPOS(v.value[String]("pos").get) :: Index(v.value[Int]("sIndex").get) :: Nil))
//        words = words :+ ww
//      }
////      v = v.out("next").head()
//    }
//    new Sentence(words.map(_.lemma).mkString(" "), words)
//  }

  import collection.JavaConversions._
  private def getSentence(w:ScalaVertex) = {

    val path = w.as("a").out("next").jump(
      to = "a",
      jumpPredicate = { t: Traverser[Vertex] =>
        t.get().out("next").size > 0
      }
    ).path.toList.head
    val words = path.objects.collect {
      case v: Vertex => {
        if(v.has("lemma").size > 0) {
          Some((ScalaVertex(v),extractWord(v)))
        }
        else
          None
      }
      case _ => None
    }.flatten.toIndexedSeq

    val ws =words.map(_._2)

    val dependency = getDependency(words)
    Sentence(ws.map(_.lemma).mkString(" "), ws,dependency::Nil)
  }

  import scalax.collection.edge.Implicits._

  private def extractWord(v:ScalaVertex): Word = {
    if (v.out("senseTag").toList.size > 0) {
      val SK = SenseTag(v.out("senseTag").toList.head.value[String]("senseKey"))
      Word(v.value[String]("lemma").get,
        Attribute(PenPOS(v.value[String]("pos").get) :: Index(v.value[Int]("sIndex").get) :: SK :: Nil))
    }
    else {
      Word(v.value[String]("lemma").get,
        Attribute(PenPOS(v.value[String]("pos").get) :: Index(v.value[Int]("sIndex").get) :: Nil))
    }
  }

  private def getDependency(words: IndexedSeq[(ScalaVertex,Word)]) = {
    val edges = words.flatMap { case (v, w) =>
      v.outE().has("kind", "syntactic-dependency").toList()
        .map(e =>
        (w ~+> (words.filter(_._1.id == e.inV.toList.head.id).map(_._2).head))(e.label()))
    }
    CollapsedCCDependency(scalax.collection.Graph.from(words.map(_._2),edges))
  }

  private def vertexToSynset(v:ScalaVertex)= {
    def getDefs = {
      val defs = v.out("definition").toList
      //val a = defs.map(d => getSentence2(d)).toIndexedSeq
      val b = defs.map(d => getSentence(d)).toIndexedSeq
      b
    }

    def getExmps= {
      val exps = v.out("example").toList
      exps.map(d => getSentence(d)).toIndexedSeq
    }

    Synset(v.value[String]("senseKey").get,v.value[String]("senseID").get
      , v.value[String]("lemma").get.split(",").map(Word(_,Nil)).toSeq
      , v.value[String]("gloss").get,WordNetPOS(v.value[String]("wordnetPos").get),getDefs,getExmps)
  }

  // implicits for synsets
  implicit class SynsetImplicits(val s: Synset) {
    def ==>(relation: WordNetRelation):Option[List[Synset]] = {
      getRelation(s,relation)
    }
  }
}
