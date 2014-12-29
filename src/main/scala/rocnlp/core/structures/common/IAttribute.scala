package rocnlp.core.structures.common

/**
 * Created by Omid on 12/9/2014.
 */

@SerialVersionUID(1L)
trait IAttribute extends Serializable

object AttributeImplicit{
  implicit def listToAtr(from:List[IAttribute]) = Attribute(from)
}


import scala.reflect._
@SerialVersionUID(1L)
case class Attribute(values:List[IAttribute]) extends Serializable{
  def this() = this(Nil)

  def get[Q](implicit q:ClassTag[Q]) =
    values.filter(e => q.runtimeClass.getName.contains(e.getClass.getName)).map(q => q.asInstanceOf[Q])

  def size = values.size
}

@SerialVersionUID(1L)
case class Index(value:Int) extends IAttribute with Serializable

@SerialVersionUID(1L)
trait POS extends IAttribute with Serializable




