package rocnlp.core.lexicon.wordnet

import rocnlp.core.structures.common._

/**
 * Created by Omid on 12/13/2014.
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