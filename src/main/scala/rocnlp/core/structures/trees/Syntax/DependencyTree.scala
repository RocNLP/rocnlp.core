package rocnlp.core.structures.trees.Syntax

import rocnlp.core.structures.common.{IAttribute, IWord}

import scalax.collection.Graph

import scalax.collection.GraphPredef.EdgeLikeIn


/**
 * Created by Omid on 12/27/2014.
 */
trait DependencyTree[N <: IWord,E[X] <: EdgeLikeIn[X]] extends IAttribute{
  val tree:Graph[N,E]
}


