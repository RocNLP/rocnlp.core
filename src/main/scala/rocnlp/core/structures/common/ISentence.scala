package rocnlp.structures.common

/**
 * Created by Omid on 12/8/2014.
 */
trait ISentence

case class Sentence(rawSentence:String, words: IndexedSeq[Word]) extends ISentence
