package rocnlp.core.structures.trees.Syntax.stanford

import rocnlp.core.structures.common.{Word, Attribute}
import rocnlp.core.structures.trees.Syntax.DependencyTree
import scalax.collection.Graph
import scalax.collection.edge.Implicits._
import scalax.collection.edge.LDiEdge

@SerialVersionUID(1L)
case class CollapsedCCDependency(val tree: Graph[Word,LDiEdge]) extends DependencyTree[Word,LDiEdge] with Serializable

//object scratch{
//
//  val t = Graph(((Word("A",Attribute(Nil)))~+>(Word("B",Attribute(Nil))))("a"))
//  val a = CollapsedCCDependency(t)
//}