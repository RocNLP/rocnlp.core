/**
* Created by Omid on 12/9/2014.
*/
import org.scalatest.matchers._
import org.scalatest.FunSpec

import rocnlp.core.lexicon.wordnet._

import rocnlp.core.structures.common._


class WordnetTest extends FunSpec with ShouldMatchers   {



  describe("an apple") {
    val apple = WordNet.getWord("apple")

    it("should exist in wordnet") {
      println(WordNet.getWord("apple"))


      apple.attributes.size > 0 should be(true)
    }


    val atr = apple.attributes.get[Synset](Synset.getClass)

    it("have two synsets"){
      atr.size should be(2)
    }

    it ("first sense should have these properties") {
      val apple1 = atr.head
      apple1.definitions.size should be(1)
      apple1.examples.size should be(0)
    }

    it("definition has these properties"){
      val d1 = atr.head.definitions.head
      d1.words.size should be(14)
      d1.words(0).lemma should be("fruit")
      val word4 = d1.words(4)
      word4.attributes.get[Index](Index.getClass).head.value should be(4)
      d1.words(0).attributes.get[SenseKey](SenseKey.getClass).size should be(1)
    }
  }

}
