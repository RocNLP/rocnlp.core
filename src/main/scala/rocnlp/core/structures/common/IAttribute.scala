package rocnlp.core.structures.common

/**
 * Created by Omid on 12/9/2014.
 */
trait IAttribute

object AttributeImplicit{
  implicit def listToAtr(from:List[IAttribute]) = Attribute(from)
}


import scala.reflect._
case class Attribute(values:List[IAttribute]){
  def this() = this(Nil)

  def get[Q](implicit q:ClassTag[Q]) =
    values.filter(e => q.runtimeClass.getName.contains(e.getClass.getName)).map(q => q.asInstanceOf[Q])

  def size = values.size
}

case class Index(value:Int) extends IAttribute

abstract class POS(value:String) extends IAttribute


