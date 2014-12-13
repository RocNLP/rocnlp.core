package rocnlp.core.structures.common

/**
 * Created by Omid on 12/8/2014.
 */
trait ISentence{}

/**
 * Sentence contains all properties of a sentence
 * @param rawSentence string format of sentence
 * @param words all words in a sentence
 * @param attributes attributes which can be assign to a sentence like SpeechAct
 */
case class Sentence(rawSentence:String, words: IndexedSeq[Word], attributes:Attribute) extends ISentence{
  def this(sentenseRaw:String, allWords: IndexedSeq[Word]) = this(sentenseRaw,allWords,new Attribute())
  def this(sentenseRaw:String) = this(sentenseRaw,IndexedSeq.empty[Word],new Attribute())
}
