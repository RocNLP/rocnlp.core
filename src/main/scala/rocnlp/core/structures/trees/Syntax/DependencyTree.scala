package rocnlp.core.structures.trees.Syntax

import rocnlp.core.structures.common.{IAttribute, IWord}

import scalax.collection.Graph

import scalax.collection.GraphPredef.EdgeLikeIn


/**
 * Created by Omid on 12/27/2014.
 */
@SerialVersionUID(1L)
trait DependencyTree[N <: IWord,E[X] <: EdgeLikeIn[X]] extends IAttribute with Serializable{
  val tree:Graph[N,E]
}


