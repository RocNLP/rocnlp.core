package rocnlp.structures.common

/**
 * Created by Omid on 12/8/2014.
 */

trait IWord


case class Word(lemma: String,attributes:Attribute) extends IWord


