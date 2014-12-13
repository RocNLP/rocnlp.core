package rocnlp.structures.common

/**
 * Created by Omid on 12/9/2014.
 */
trait IAttribute

object AttributeImplicit{
  implicit def listToAtr(from:List[IAttribute]) = Attribute(from)
}

case class Attribute(values:List[IAttribute]){

  def get[Q](className:Class[_]) =
    values.filter(e => className.getName.contains(e.getClass.getName)).map(q => q.asInstanceOf[Q])

  def size = values.size
}

case class Index(value:Int) extends IAttribute

abstract class POS(value:String) extends IAttribute


