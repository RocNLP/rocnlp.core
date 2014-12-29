package rocnlp.core.structures.common

/**
 * Created by Omid on 12/8/2014.
 */

@SerialVersionUID(1L)
trait IWord extends Serializable

@SerialVersionUID(1L)
case class Word(lemma: String,attributes:Attribute) extends IWord with Serializable{
  def this(Lemma:String) = this(Lemma,new Attribute())
}


