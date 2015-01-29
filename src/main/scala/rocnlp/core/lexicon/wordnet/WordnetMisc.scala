package rocnlp.core.lexicon.wordnet

import rocnlp.core.structures.common._

/**
 * Created by Omid on 12/13/2014.
 */
@SerialVersionUID(1L)
case class PenPOS(value:String) extends POS with Serializable

@SerialVersionUID(1L)
case class WordNetPOS(value:String) extends POS with Serializable

@SerialVersionUID(1L)
object WordNetPOS extends Serializable{
  val a = WordNetPOS("a")
  val n = WordNetPOS("n")
  val v = WordNetPOS("v")
  val r = WordNetPOS("r")
  val s = WordNetPOS("s")
  //  Adjective.value
  val allPOS = Seq(a,n,v,r,s)
}

@SerialVersionUID(1L)
case class WordNetRelation(value:String) extends IRelation with Serializable

@SerialVersionUID(1L)
case class WordNetGlossRelation(value:String) extends IRelation with Serializable
//
@SerialVersionUID(1L)
object WordNetRelation extends Serializable{
//
    // All categories
    val ANTONYM = WordNetRelation("antonym")
    val CATEGORY = WordNetRelation("region_domain")
    val USAGE = WordNetRelation("usage_domain")

    // Nouns and Verbs
    val HYPERNYM = WordNetRelation("hypernym")
    val HYPONYM = WordNetRelation("hyponym")
    val NOMINALIZATION = WordNetRelation("nominalization")
    val INSTANCE_HYPERNYM = WordNetRelation("instance_hypernym")
    val INSTANCES_HYPONYM = WordNetRelation("instances_hyponym")

    // Nouns and Adjectives
    val ATTRIBUTE = WordNetRelation("attribute")
    val SEE_ALSO = WordNetRelation("see_also")



    // Nouns
    val MEMBER_HOLONYM = WordNetRelation("member_holonym")
    val SUBSTANCE_HOLONYM = WordNetRelation("substance_holonym")
    val PART_HOLONYM = WordNetRelation("part_holonym")
    val MEMBER_MERONYM = WordNetRelation("member_meronym")
    val SUBSTANCE_MERONYM = WordNetRelation("substance_meronym")
    val PART_MERONYM = WordNetRelation("part_meronym")
    val CATEGORY_MEMBER = WordNetRelation("category_member")
    val REGION_MEMBER = WordNetRelation("region_member")
    val USAGE_MEMBER = WordNetRelation("usage_member")

    // Verbs

    val ENTAILMENT = WordNetRelation("entailment")
    val ENTAILED_BY = WordNetRelation("entailed_by")
    val CAUSE = WordNetRelation("cause")
    val VERB_GROUP = WordNetRelation("verb_group")

    // Adjectives
    val SIMILAR_TO = WordNetRelation("similar")
    val PARTICIPLE_OF = WordNetRelation("participle_of")
    val PERTAINYM = WordNetRelation("pertainym")

    // Adverbs
    val DERIVED = WordNetRelation("derived")

  val allRelations=Seq(ANTONYM,CATEGORY,USAGE,HYPERNYM,HYPONYM ,NOMINALIZATION,INSTANCE_HYPERNYM,
    INSTANCES_HYPONYM,ATTRIBUTE,SEE_ALSO,MEMBER_HOLONYM,SUBSTANCE_HOLONYM,PART_HOLONYM,MEMBER_MERONYM,
    SUBSTANCE_MERONYM,PART_MERONYM,CATEGORY_MEMBER,REGION_MEMBER,USAGE_MEMBER,ENTAILMENT,ENTAILED_BY,
    CAUSE,VERB_GROUP,SIMILAR_TO,PARTICIPLE_OF,PERTAINYM,DERIVED)

}

case class Synset(senseKey:String, senseId:String, lemmas: Seq[Word], gloss:String, wordnetPos: WordNetPOS
                  , definitions: IndexedSeq[Sentence]
                  , examples: IndexedSeq[Sentence]) extends ILexicalEntry {

}

case class SenseTag(value:String) extends IAttribute