/**
* Created by Omid on 12/9/2014.
*/
import org.scalatest.matchers._
import org.scalatest.FunSpec

import rocnlp.core.lexicon.wordnet._

import rocnlp.core.structures.common._
import rocnlp.core.structures.trees.Syntax.stanford.CollapsedCCDependency


class WordnetTest extends FunSpec with ShouldMatchers   {


  WordNet("E:/databases/wordnet")

//  WordNet.getSynsets("tall",WordNetPOS.a)


  describe("an apple") {

    val apple:Word = WordNet.getWord("apple")

    it("should exist in wordnet") {
//      println(WordNet.getWord("apple"))

      apple.attributes.size > 0 should be(true)
    }

    val senses = apple.attributes.get[Synset]

    it("have two synsets"){
      senses.size should be(2)
    }

    it ("first sense should have these properties") {
      val apple1 = senses.head
      apple1.definitions.size should be(1)
      apple1.examples.size should be(0)
    }

    it("definition has these properties"){
      val d1 = senses.head.definitions.head
      d1.words.size should be(15)
      d1.words(0).lemma should be("fruit")
      val word4 = d1.words(4)
      word4.attributes.get[Index].head.value should be(4)
      d1.words(0).attributes.get[SenseTag].size should be(1)

    }

    it("it has some relations"){
      val appleSynset = apple.attributes.get[Synset].head
      import rocnlp.core.lexicon.wordnet.WordNet.SynsetImplicits
      val appleHypers = appleSynset ==>(WordNetRelation.HYPERNYM)
      appleHypers.get.size should be(2)
    }
  }

  describe("tall definition dependency tree"){
    it("has two definition with specific tree"){
      val tall = WordNet.getSynsets("tall",WordNetPOS.a).get(0)
      val dep = tall.definitions.head.attributes.get[CollapsedCCDependency].head
      dep.tree.nodes.size should be(4)
      dep.tree.edges.size should be(2)
      dep.tree.edges.filter(_.label == "prep_in").size should be(1)
      dep.tree.edges.filter(_.label == "prep_in").head.from.lemma should be("great")
      dep.tree.edges.filter(_.label == "prep_in").head.to.lemma should be("dimension")


    }
  }

  //todo: this sense key is not correct, tall(a) or tall ? add more tests for sense key
  describe("All sense numbers fot tall should be in correct order"){
    val tallSenses = WordNet.getSynsets("tall",WordNetPOS.a).get
    it("has match all sense keys") {
      tallSenses(0).senseKey should be("tall%3:00:00::")
      //tallSenses(3).senseKey should be("tall%3:00:00:incredible:00")
    }
  }

  describe("test for part of speech in gloss"){
    val tallSenses = WordNet.getSynsets("tall",WordNetPOS.a).get
    it("checkin pos for tall, second definition, second word") {
      tallSenses(0).definitions(1).words(2).attributes.get[PenPOS].head.value should be("NN")
    }
  }




}
