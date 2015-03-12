package scalan.graphs

import scala.annotation.unchecked.uncheckedVariance
import scalan.collections.CollectionsDsl
import scalan.common.Default
import scalan.{Scalan, ScalanExp, ScalanSeq}
import scalan.ScalanCommunityDsl

/**
 * Created by afilippov on 2/16/15.
 */
trait Vertices extends ScalanCommunityDsl with CollectionsDsl { self: GraphsDsl =>
  trait Vertex[V, E] extends Reifiable[Vertex[V @uncheckedVariance, E  @uncheckedVariance]]{
    implicit def eV: Elem[V]
    implicit def eE: Elem[E]
    implicit def graph: PG[V, E]  // ?

    def id: Rep[Int]
    def value: Rep[V] = this.graph.vertexValues(id)
    def outNbrs: Coll[Vertex[V,E]] = ???
    def outEdges: Coll[AdjEdge[V,E]] = ???
    def hasEdgeTo(v: Rep[Vertex[V,E]]):Rep[Boolean] = graph.hasEdgeTo(id, v.id)
    def numOutNbrs: Rep[Int] = graph.outDegrees(id)
    def numInNbrs: Rep[Int] = graph.inDegrees(id)
    def commonNbrs(v: Rep[Vertex[V,E]]): Coll[Vertex[V,E]] = ??? //mkView(graph.commonNbrs(id, v.id))
    def commonNbrsNum(v: Rep[Vertex[V,E]]): Rep[Int] = graph.commonNbrsNum(id, v.id)
  }
  trait VertexCompanion extends TypeFamily2[Vertex] {
    def defaultOf[T: Elem, V:Elem] = SVertex.defaultOf[T,V]
  }

  abstract class SVertex[V,E] (val id: Rep[Int], val graph: PG[V, E]) (implicit val eV: Elem[V], val eE: Elem[E]) extends Vertex[V,E]{}
  trait SVertexCompanion extends ConcreteClass2[SVertex] {
    def defaultOf[T:Elem, V:Elem] = Default.defaultVal(SVertex(-1, element[Graph[T,V]].defaultRepValue))
  }
}

trait VerticesDsl extends impl.VerticesAbs { self: GraphsDsl => }
trait VerticesDslSeq extends impl.VerticesSeq { self: GraphsDslSeq => }
trait VerticesDslExp extends impl.VerticesExp { self: GraphsDslExp => }
