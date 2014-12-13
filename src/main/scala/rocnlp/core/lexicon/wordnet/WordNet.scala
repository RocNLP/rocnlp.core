package rocnlp.lexicon.wordnet

import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import com.tinkerpop.gremlin.process.T._
import com.tinkerpop.gremlin.scala._
import rocnlp.structures.common._
import rocnlp.structures.common.AttributeImplicit._
import shapeless._
import syntax.std.traversable._

/**
* Created by Omid on 12/8/2014.
*/

case class WordNetPOS(v:String) extends POS(v){
  object Adjective extends WordNetPOS("ADJ")
  object Noun extends WordNetPOS("Noun")
  object Verb extends WordNetPOS("Verb")
  object Adverb extends WordNetPOS("ADV")
}

case class Synset(senseKey:String, senseId:String, lemmas: Seq[Word], gloss:String, pos: WordNetPOS
                  , definitions: IndexedSeq[Sentence]
                  , examples: IndexedSeq[Sentence]) extends ILexicalEntry

case class SenseKey(value:String) extends IAttribute
object WordNet {
  val path = "E:/databases/wordnet"
  val graph: Neo4jGraph = Neo4jGraph.open(path)
  val gs = GremlinScala(graph)
  def disconnect = graph.close()

  def getWord(s:String) = {
    val v = gs.V.has(label,"word").has("lemma",s).toList()
    gs.V.has(label,"word").has("lemma",s).out()
    if (v.size == 0)
      Word(s,Attribute(Nil))
    else {
      val senses = gs.V.has(label, "word").has("lemma", s).out("synset").toList().map(element => vertexToSynset(element))
      Word(v.head.value[String]("lemma"), senses)
    }
  }


//  private def getSentence(w:Vertex)= {
//    var v = w
//    var words = IndexedSeq.empty[Word]
//
//    while(v.out("next").toList.size() > 0) {
//      if (v.out("synset").toList.size() > 0) {
//        val SK = SenseKey(v.out("synset").toList.get(0).value[String]("senseKey"))
//        val ww = Word(v.value[String]("lemma"),
//          Attribute(WordNetPOS(v.value[String]("pos")) :: Index(v.value[Int]("sIndex")) :: SK :: Nil))
//        words = words :+ ww
//      }
//      else {
//        val ww = Word(v.value[String]("lemma"),
//          Attribute(WordNetPOS(v.value[String]("pos")) :: Index(v.value[Int]("sIndex")) :: Nil))
//        words = words :+ ww
//      }
//      v = v.out("next").toList.get(0)
//    }
//    Sentence(words.map(_.lemma).mkString(" "), words)
//  }

  private def getSentence(w:ScalaVertex)= {
    var v = w
    var words = IndexedSeq.empty[Word]

    while(v.out("next").toList.size > 0) {
      val vvv = v.outE().toList()
      if (v.out("senseTag").toList.size > 0) {
        val SK = SenseKey(v.out("senseTag").head().value[String]("senseKey"))
        val ww = Word(v.value[String]("lemma"),
          Attribute(WordNetPOS(v.value[String]("pos")) :: Index(v.value[Int]("sIndex")) :: SK :: Nil))
        words = words :+ ww
      }
      else {
        val ww = Word(v.value[String]("lemma"),
          Attribute(WordNetPOS(v.value[String]("pos")) :: Index(v.value[Int]("sIndex")) :: Nil))
        words = words :+ ww
      }
      v = v.out("next").head()
    }
    Sentence(words.map(_.lemma).mkString(" "), words)
  }

  private def vertexToSynset(v:ScalaVertex)= {
    def getDefs = {
      val defs = v.out("definition").toList
      defs.map(d => getSentence(d.out("next").toList.get(0))).toIndexedSeq
    }

    def getExmps= {
      val exps = v.out("example").toList
      exps.map(d => getSentence(d.out("next").toList.get(0))).toIndexedSeq
    }

    Synset(v.value[String]("senseKey"),v.value[String]("senseID")
      , v.value[String]("lemma").split(",").map(Word(_,Nil)).toSeq
      , v.value[String]("gloss"),WordNetPOS(v.value[String]("wordnetPos")),getDefs,getExmps)
  }
}
