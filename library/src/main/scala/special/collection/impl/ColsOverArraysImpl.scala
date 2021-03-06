package special.collection

import scalan._
import scala.reflect.runtime.universe._
import scala.reflect._

package impl {
// Abs -----------------------------------
trait ColsOverArraysDefs extends scalan.Scalan with ColsOverArrays {
  self: Library =>
import IsoUR._
import Converter._
import PairCol._
import PairOfCols._
import Col._
import ColOverArray._
import ColBuilder._
import ColOverArrayBuilder._
import BaseColBuilder._
import WArray._
import ReplColBuilder._
import ReplCol._

object BaseColBuilder extends EntityObject("BaseColBuilder") {
  // entityProxy: single proxy for each type family
  implicit def proxyBaseColBuilder(p: Rep[BaseColBuilder]): BaseColBuilder = {
    if (p.rhs.isInstanceOf[BaseColBuilder@unchecked]) p.rhs.asInstanceOf[BaseColBuilder]
    else
      proxyOps[BaseColBuilder](p)(scala.reflect.classTag[BaseColBuilder])
  }

  // familyElem
  class BaseColBuilderElem[To <: BaseColBuilder]
    extends ColBuilderElem[To] {
    override lazy val parent: Option[Elem[_]] = Some(colBuilderElement)
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs()
    override lazy val tag = {
      weakTypeTag[BaseColBuilder].asInstanceOf[WeakTypeTag[To]]
    }
    override def convert(x: Rep[Def[_]]) = {
      val conv = fun {x: Rep[BaseColBuilder] => convertBaseColBuilder(x) }
      tryConvert(element[BaseColBuilder], this, x, conv)
    }

    def convertBaseColBuilder(x: Rep[BaseColBuilder]): Rep[To] = {
      x.elem match {
        case _: BaseColBuilderElem[_] => asRep[To](x)
        case e => !!!(s"Expected $x to have BaseColBuilderElem[_], but got $e", x)
      }
    }
    override def getDefaultRep: Rep[To] = ???
  }

  implicit def baseColBuilderElement: Elem[BaseColBuilder] =
    cachedElem[BaseColBuilderElem[BaseColBuilder]]()

  implicit case object BaseColBuilderCompanionElem extends CompanionElem[BaseColBuilderCompanionCtor] {
    lazy val tag = weakTypeTag[BaseColBuilderCompanionCtor]
    protected def getDefaultRep = RBaseColBuilder
  }

  abstract class BaseColBuilderCompanionCtor extends CompanionDef[BaseColBuilderCompanionCtor] with BaseColBuilderCompanion {
    def selfType = BaseColBuilderCompanionElem
    override def toString = "BaseColBuilder"
  }
  implicit def proxyBaseColBuilderCompanionCtor(p: Rep[BaseColBuilderCompanionCtor]): BaseColBuilderCompanionCtor =
    proxyOps[BaseColBuilderCompanionCtor](p)

  lazy val RBaseColBuilder: Rep[BaseColBuilderCompanionCtor] = new BaseColBuilderCompanionCtor {
    private val thisClass = classOf[BaseColBuilderCompanion]
  }

  object BaseColBuilderMethods {
    object apply_apply {
      def unapply(d: Def[_]): Nullable[(Rep[BaseColBuilder], Rep[Col[A]], Rep[Col[B]]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[BaseColBuilderElem[_]] && method.getName == "apply" && { val ann = method.getAnnotation(classOf[scalan.OverloadId]); ann != null && ann.value == "apply" } =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[BaseColBuilder], Rep[Col[A]], Rep[Col[B]]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[BaseColBuilder], Rep[Col[A]], Rep[Col[B]]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    // manual fix (remove unnecessary elems)
    object apply_apply_items {
      def unapply(d: Def[_]): Nullable[(Rep[BaseColBuilder], Seq[Rep[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[BaseColBuilderElem[_]] && method.getName == "apply" && { val ann = method.getAnnotation(classOf[scalan.OverloadId]); ann != null && ann.value == "apply_items" } =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[BaseColBuilder], Seq[Rep[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[BaseColBuilder], Seq[Rep[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fromArray {
      def unapply(d: Def[_]): Nullable[(Rep[BaseColBuilder], Rep[WArray[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[BaseColBuilderElem[_]] && method.getName == "fromArray" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[BaseColBuilder], Rep[WArray[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[BaseColBuilder], Rep[WArray[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object replicate {
      def unapply(d: Def[_]): Nullable[(Rep[BaseColBuilder], Rep[Int], Rep[T]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[BaseColBuilderElem[_]] && method.getName == "replicate" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[BaseColBuilder], Rep[Int], Rep[T]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[BaseColBuilder], Rep[Int], Rep[T]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object xor {
      def unapply(d: Def[_]): Nullable[(Rep[BaseColBuilder], Rep[Col[Byte]], Rep[Col[Byte]])] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[BaseColBuilderElem[_]] && method.getName == "xor" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[BaseColBuilder], Rep[Col[Byte]], Rep[Col[Byte]])]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[BaseColBuilder], Rep[Col[Byte]], Rep[Col[Byte]])] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object BaseColBuilderCompanionMethods {
  }
} // of object BaseColBuilder
  registerEntityObject("BaseColBuilder", BaseColBuilder)

object ColOverArray extends EntityObject("ColOverArray") {
  case class ColOverArrayCtor[A]
      (override val arr: Rep[WArray[A]])
    extends ColOverArray[A](arr) with Def[ColOverArray[A]] {
    implicit lazy val eA = arr.eT

    lazy val selfType = element[ColOverArray[A]]
    private val thisClass = classOf[ColOverArray[A]]

    override def getOrElse(i: Rep[Int], default: Rep[Thunk[A]]): Rep[A] = {
      asRep[A](mkMethodCall(self,
        thisClass.getMethod("getOrElse", classOf[Sym], classOf[Sym]),
        List(i, default),
        true, element[A]))
    }

    override def fold[B](zero: Rep[B])(op: Rep[((B, A)) => B]): Rep[B] = {
      implicit val eB = zero.elem
      asRep[B](mkMethodCall(self,
        thisClass.getMethod("fold", classOf[Sym], classOf[Sym]),
        List(zero, op),
        true, element[B]))
    }

    override def append(other: Rep[Col[A]]): Rep[Col[A]] = {
      asRep[Col[A]](mkMethodCall(self,
        thisClass.getMethod("append", classOf[Sym]),
        List(other),
        true, element[Col[A]]))
    }
  }
  // elem for concrete class
  class ColOverArrayElem[A](val iso: Iso[ColOverArrayData[A], ColOverArray[A]])(implicit override val eA: Elem[A])
    extends ColElem[A, ColOverArray[A]]
    with ConcreteElem1[A, ColOverArrayData[A], ColOverArray[A], Col] {
    override lazy val parent: Option[Elem[_]] = Some(colElement(element[A]))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("A" -> (eA -> scalan.util.Invariant))
    override def convertCol(x: Rep[Col[A]]) = RColOverArray(x.arr)
    override def getDefaultRep = RColOverArray(element[WArray[A]].defaultRepValue)
    override lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[ColOverArray[A]]
    }
  }

  // state representation type
  type ColOverArrayData[A] = WArray[A]

  // 3) Iso for concrete class
  class ColOverArrayIso[A](implicit eA: Elem[A])
    extends EntityIso[ColOverArrayData[A], ColOverArray[A]] with Def[ColOverArrayIso[A]] {
    private lazy val _safeFrom = fun { p: Rep[ColOverArray[A]] => p.arr }
    override def from(p: Rep[ColOverArray[A]]) =
      tryConvert[ColOverArray[A], WArray[A]](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[WArray[A]]) = {
      val arr = p
      RColOverArray(arr)
    }
    lazy val eFrom = element[WArray[A]]
    lazy val eTo = new ColOverArrayElem[A](self)
    lazy val selfType = new ColOverArrayIsoElem[A](eA)
    def productArity = 1
    def productElement(n: Int) = eA
  }
  case class ColOverArrayIsoElem[A](eA: Elem[A]) extends Elem[ColOverArrayIso[A]] {
    def getDefaultRep = reifyObject(new ColOverArrayIso[A]()(eA))
    lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[ColOverArrayIso[A]]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("A" -> (eA -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class ColOverArrayCompanionCtor extends CompanionDef[ColOverArrayCompanionCtor] with ColOverArrayCompanion {
    def selfType = ColOverArrayCompanionElem
    override def toString = "ColOverArrayCompanion"

    @scalan.OverloadId("fromFields")
    def apply[A](arr: Rep[WArray[A]]): Rep[ColOverArray[A]] =
      mkColOverArray(arr)

    def unapply[A](p: Rep[Col[A]]) = unmkColOverArray(p)
  }
  lazy val ColOverArrayRep: Rep[ColOverArrayCompanionCtor] = new ColOverArrayCompanionCtor
  lazy val RColOverArray: ColOverArrayCompanionCtor = proxyColOverArrayCompanion(ColOverArrayRep)
  implicit def proxyColOverArrayCompanion(p: Rep[ColOverArrayCompanionCtor]): ColOverArrayCompanionCtor = {
    if (p.rhs.isInstanceOf[ColOverArrayCompanionCtor])
      p.rhs.asInstanceOf[ColOverArrayCompanionCtor]
    else
      proxyOps[ColOverArrayCompanionCtor](p)
  }

  implicit case object ColOverArrayCompanionElem extends CompanionElem[ColOverArrayCompanionCtor] {
    lazy val tag = weakTypeTag[ColOverArrayCompanionCtor]
    protected def getDefaultRep = ColOverArrayRep
  }

  implicit def proxyColOverArray[A](p: Rep[ColOverArray[A]]): ColOverArray[A] =
    proxyOps[ColOverArray[A]](p)

  implicit class ExtendedColOverArray[A](p: Rep[ColOverArray[A]]) {
    def toData: Rep[ColOverArrayData[A]] = {
      implicit val eA = p.arr.eT
      isoColOverArray(eA).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoColOverArray[A](implicit eA: Elem[A]): Iso[ColOverArrayData[A], ColOverArray[A]] =
    reifyObject(new ColOverArrayIso[A]()(eA))

  def mkColOverArray[A]
    (arr: Rep[WArray[A]]): Rep[ColOverArray[A]] = {
    new ColOverArrayCtor[A](arr)
  }
  def unmkColOverArray[A](p: Rep[Col[A]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: ColOverArrayElem[A] @unchecked =>
      Some((asRep[ColOverArray[A]](p).arr))
    case _ =>
      None
  }

    object ColOverArrayMethods {
    object builder {
      def unapply(d: Def[_]): Nullable[Rep[ColOverArray[A]] forSome {type A}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "builder" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[ColOverArray[A]] forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[ColOverArray[A]] forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object length {
      def unapply(d: Def[_]): Nullable[Rep[ColOverArray[A]] forSome {type A}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "length" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[ColOverArray[A]] forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[ColOverArray[A]] forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object apply {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[Int]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "apply" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[Int]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[Int]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object getOrElse {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[Int], Rep[Thunk[A]]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "getOrElse" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[Int], Rep[Thunk[A]]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[Int], Rep[Thunk[A]]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object map {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[A => B]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "map" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[A => B]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[A => B]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object foreach {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[A => Unit]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "foreach" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[A => Unit]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[A => Unit]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object exists {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "exists" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object forall {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "forall" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object filter {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "filter" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[A => Boolean]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fold {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[B], Rep[((B, A)) => B]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "fold" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[B], Rep[((B, A)) => B]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[B], Rep[((B, A)) => B]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object slice {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[Int], Rep[Int]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "slice" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[Int], Rep[Int]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[Int], Rep[Int]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object sum {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[Monoid[A]]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "sum" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[Monoid[A]]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[Monoid[A]]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object zip {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[Col[B]]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "zip" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[Col[B]]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[Col[B]]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object append {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArray[A]], Rep[Col[A]]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayElem[_]] && method.getName == "append" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArray[A]], Rep[Col[A]]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArray[A]], Rep[Col[A]]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object ColOverArrayCompanionMethods {
  }
} // of object ColOverArray
  registerEntityObject("ColOverArray", ColOverArray)

object ColOverArrayBuilder extends EntityObject("ColOverArrayBuilder") {
  case class ColOverArrayBuilderCtor
      ()
    extends ColOverArrayBuilder() with Def[ColOverArrayBuilder] {
    lazy val selfType = element[ColOverArrayBuilder]
    private val thisClass = classOf[ColBuilder] // manual fix

    override def fromArray[T](arr: Rep[WArray[T]]): Rep[Col[T]] = {
      implicit val eT = arr.eT
      asRep[Col[T]](mkMethodCall(self,
        thisClass.getMethod("fromArray", classOf[Sym]),
        List(arr),
        true, element[Col[T]]))
    }

    override def apply[A, B](as: Rep[Col[A]], bs: Rep[Col[B]]): Rep[PairCol[A, B]] = {
      implicit val eA = as.eA
implicit val eB = bs.eA
      asRep[PairCol[A, B]](mkMethodCall(self,
        thisClass.getMethod("apply", classOf[Sym], classOf[Sym]),
        List(as, bs),
        true, element[PairCol[A, B]]))
    }

    // manual fix
    override def apply[T](items: Rep[T]*): Rep[Col[T]] = {
      implicit val eA = items(0).elem
      asRep[Col[T]](mkMethodCall(self,
        thisClass.getMethod("apply", classOf[Seq[_]]),
        List(items),
        true, element[Col[T]]))
    }

    override def replicate[T](n: Rep[Int], v: Rep[T]): Rep[Col[T]] = {
      implicit val eT = v.elem
      asRep[Col[T]](mkMethodCall(self,
        thisClass.getMethod("replicate", classOf[Sym], classOf[Sym]),
        List(n, v),
        true, element[Col[T]]))
    }

    override def xor(left: Rep[Col[Byte]], right: Rep[Col[Byte]]): Rep[Col[Byte]] = {
      asRep[Col[Byte]](mkMethodCall(self,
        thisClass.getMethod("xor", classOf[Sym], classOf[Sym]),
        List(left, right),
        true, element[Col[Byte]]))
    }
  }
  // elem for concrete class
  class ColOverArrayBuilderElem(val iso: Iso[ColOverArrayBuilderData, ColOverArrayBuilder])
    extends BaseColBuilderElem[ColOverArrayBuilder]
    with ConcreteElem[ColOverArrayBuilderData, ColOverArrayBuilder] {
    override lazy val parent: Option[Elem[_]] = Some(baseColBuilderElement)
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs()
    override def convertBaseColBuilder(x: Rep[BaseColBuilder]) = RColOverArrayBuilder()
    override def getDefaultRep = RColOverArrayBuilder()
    override lazy val tag = {
      weakTypeTag[ColOverArrayBuilder]
    }
  }

  // state representation type
  type ColOverArrayBuilderData = Unit

  // 3) Iso for concrete class
  class ColOverArrayBuilderIso
    extends EntityIso[ColOverArrayBuilderData, ColOverArrayBuilder] with Def[ColOverArrayBuilderIso] {
    private lazy val _safeFrom = fun { p: Rep[ColOverArrayBuilder] => () }
    override def from(p: Rep[ColOverArrayBuilder]) =
      tryConvert[ColOverArrayBuilder, Unit](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[Unit]) = {
      val unit = p
      RColOverArrayBuilder()
    }
    lazy val eFrom = UnitElement
    lazy val eTo = new ColOverArrayBuilderElem(self)
    lazy val selfType = new ColOverArrayBuilderIsoElem
    def productArity = 0
    def productElement(n: Int) = ???
  }
  case class ColOverArrayBuilderIsoElem() extends Elem[ColOverArrayBuilderIso] {
    def getDefaultRep = reifyObject(new ColOverArrayBuilderIso())
    lazy val tag = {
      weakTypeTag[ColOverArrayBuilderIso]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs()
  }
  // 4) constructor and deconstructor
  class ColOverArrayBuilderCompanionCtor extends CompanionDef[ColOverArrayBuilderCompanionCtor] with ColOverArrayBuilderCompanion {
    def selfType = ColOverArrayBuilderCompanionElem
    override def toString = "ColOverArrayBuilderCompanion"
    @scalan.OverloadId("fromData")
    def apply(p: Rep[ColOverArrayBuilderData]): Rep[ColOverArrayBuilder] = {
      isoColOverArrayBuilder.to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply(): Rep[ColOverArrayBuilder] =
      mkColOverArrayBuilder()

    def unapply(p: Rep[BaseColBuilder]) = unmkColOverArrayBuilder(p)
  }
  lazy val ColOverArrayBuilderRep: Rep[ColOverArrayBuilderCompanionCtor] = new ColOverArrayBuilderCompanionCtor
  lazy val RColOverArrayBuilder: ColOverArrayBuilderCompanionCtor = proxyColOverArrayBuilderCompanion(ColOverArrayBuilderRep)
  implicit def proxyColOverArrayBuilderCompanion(p: Rep[ColOverArrayBuilderCompanionCtor]): ColOverArrayBuilderCompanionCtor = {
    if (p.rhs.isInstanceOf[ColOverArrayBuilderCompanionCtor])
      p.rhs.asInstanceOf[ColOverArrayBuilderCompanionCtor]
    else
      proxyOps[ColOverArrayBuilderCompanionCtor](p)
  }

  implicit case object ColOverArrayBuilderCompanionElem extends CompanionElem[ColOverArrayBuilderCompanionCtor] {
    lazy val tag = weakTypeTag[ColOverArrayBuilderCompanionCtor]
    protected def getDefaultRep = ColOverArrayBuilderRep
  }

  implicit def proxyColOverArrayBuilder(p: Rep[ColOverArrayBuilder]): ColOverArrayBuilder =
    proxyOps[ColOverArrayBuilder](p)

  implicit class ExtendedColOverArrayBuilder(p: Rep[ColOverArrayBuilder]) {
    def toData: Rep[ColOverArrayBuilderData] = {
      isoColOverArrayBuilder.from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoColOverArrayBuilder: Iso[ColOverArrayBuilderData, ColOverArrayBuilder] =
    reifyObject(new ColOverArrayBuilderIso())

  def mkColOverArrayBuilder
    (): Rep[ColOverArrayBuilder] = {
    new ColOverArrayBuilderCtor()
  }
  def unmkColOverArrayBuilder(p: Rep[BaseColBuilder]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: ColOverArrayBuilderElem @unchecked =>
      Some(())
    case _ =>
      None
  }

    object ColOverArrayBuilderMethods {
    object fromArray {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArrayBuilder], Rep[WArray[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayBuilderElem] && method.getName == "fromArray" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArrayBuilder], Rep[WArray[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArrayBuilder], Rep[WArray[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object apply_apply {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArrayBuilder], Rep[Col[A]], Rep[Col[B]]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayBuilderElem] && method.getName == "apply" && { val ann = method.getAnnotation(classOf[scalan.OverloadId]); ann != null && ann.value == "apply" } =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArrayBuilder], Rep[Col[A]], Rep[Col[B]]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArrayBuilder], Rep[Col[A]], Rep[Col[B]]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

      // manual fix (remove unnecessary elems)
    object apply_apply_items {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArrayBuilder], Seq[Rep[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayBuilderElem] && method.getName == "apply" && { val ann = method.getAnnotation(classOf[scalan.OverloadId]); ann != null && ann.value == "apply_items" } =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArrayBuilder], Seq[Rep[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArrayBuilder], Seq[Rep[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object replicate {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArrayBuilder], Rep[Int], Rep[T]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayBuilderElem] && method.getName == "replicate" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArrayBuilder], Rep[Int], Rep[T]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArrayBuilder], Rep[Int], Rep[T]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object xor {
      def unapply(d: Def[_]): Nullable[(Rep[ColOverArrayBuilder], Rep[Col[Byte]], Rep[Col[Byte]])] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ColOverArrayBuilderElem] && method.getName == "xor" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ColOverArrayBuilder], Rep[Col[Byte]], Rep[Col[Byte]])]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ColOverArrayBuilder], Rep[Col[Byte]], Rep[Col[Byte]])] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object ColOverArrayBuilderCompanionMethods {
  }
} // of object ColOverArrayBuilder
  registerEntityObject("ColOverArrayBuilder", ColOverArrayBuilder)

object PairOfCols extends EntityObject("PairOfCols") {
  case class PairOfColsCtor[L, R]
      (override val ls: Rep[Col[L]], override val rs: Rep[Col[R]])
    extends PairOfCols[L, R](ls, rs) with Def[PairOfCols[L, R]] {
    implicit lazy val eL = ls.eA;
implicit lazy val eR = rs.eA
    override lazy val eA: Elem[(L, R)] = implicitly[Elem[(L, R)]]
    lazy val selfType = element[PairOfCols[L, R]]
    private val thisClass = classOf[PairOfCols[L, R]]

    override def getOrElse(i: Rep[Int], default: Rep[Thunk[(L, R)]]): Rep[(L, R)] = {
      asRep[(L, R)](mkMethodCall(self,
        thisClass.getMethod("getOrElse", classOf[Sym], classOf[Sym]),
        List(i, default),
        true, element[(L, R)]))
    }

    override def fold[B](zero: Rep[B])(op: Rep[((B, (L, R))) => B]): Rep[B] = {
      implicit val eB = zero.elem
      asRep[B](mkMethodCall(self,
        thisClass.getMethod("fold", classOf[Sym], classOf[Sym]),
        List(zero, op),
        true, element[B]))
    }

    override def sum(m: Rep[Monoid[(L, R)]]): Rep[(L, R)] = {
      asRep[(L, R)](mkMethodCall(self,
        thisClass.getMethod("sum", classOf[Sym]),
        List(m),
        true, element[(L, R)]))
    }
  }
  // elem for concrete class
  class PairOfColsElem[L, R](val iso: Iso[PairOfColsData[L, R], PairOfCols[L, R]])(implicit override val eL: Elem[L], override val eR: Elem[R])
    extends PairColElem[L, R, PairOfCols[L, R]]
    with ConcreteElem[PairOfColsData[L, R], PairOfCols[L, R]] {
    override lazy val parent: Option[Elem[_]] = Some(pairColElement(element[L], element[R]))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("L" -> (eL -> scalan.util.Invariant), "R" -> (eR -> scalan.util.Invariant))
    override def convertPairCol(x: Rep[PairCol[L, R]]) = RPairOfCols(x.ls, x.rs)
    override def getDefaultRep = RPairOfCols(element[Col[L]].defaultRepValue, element[Col[R]].defaultRepValue)
    override lazy val tag = {
      implicit val tagL = eL.tag
      implicit val tagR = eR.tag
      weakTypeTag[PairOfCols[L, R]]
    }
  }

  // state representation type
  type PairOfColsData[L, R] = (Col[L], Col[R])

  // 3) Iso for concrete class
  class PairOfColsIso[L, R](implicit eL: Elem[L], eR: Elem[R])
    extends EntityIso[PairOfColsData[L, R], PairOfCols[L, R]] with Def[PairOfColsIso[L, R]] {
    private lazy val _safeFrom = fun { p: Rep[PairOfCols[L, R]] => (p.ls, p.rs) }
    override def from(p: Rep[PairOfCols[L, R]]) =
      tryConvert[PairOfCols[L, R], (Col[L], Col[R])](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[(Col[L], Col[R])]) = {
      val Pair(ls, rs) = p
      RPairOfCols(ls, rs)
    }
    lazy val eFrom = pairElement(element[Col[L]], element[Col[R]])
    lazy val eTo = new PairOfColsElem[L, R](self)
    lazy val selfType = new PairOfColsIsoElem[L, R](eL, eR)
    def productArity = 2
    def productElement(n: Int) = n match {
      case 0 => eL
      case 1 => eR
    }
  }
  case class PairOfColsIsoElem[L, R](eL: Elem[L], eR: Elem[R]) extends Elem[PairOfColsIso[L, R]] {
    def getDefaultRep = reifyObject(new PairOfColsIso[L, R]()(eL, eR))
    lazy val tag = {
      implicit val tagL = eL.tag
      implicit val tagR = eR.tag
      weakTypeTag[PairOfColsIso[L, R]]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("L" -> (eL -> scalan.util.Invariant), "R" -> (eR -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class PairOfColsCompanionCtor extends CompanionDef[PairOfColsCompanionCtor] with PairOfColsCompanion {
    def selfType = PairOfColsCompanionElem
    override def toString = "PairOfColsCompanion"
    @scalan.OverloadId("fromData")
    def apply[L, R](p: Rep[PairOfColsData[L, R]]): Rep[PairOfCols[L, R]] = {
      implicit val eL = p._1.eA;
implicit val eR = p._2.eA
      isoPairOfCols[L, R].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[L, R](ls: Rep[Col[L]], rs: Rep[Col[R]]): Rep[PairOfCols[L, R]] =
      mkPairOfCols(ls, rs)

    def unapply[L, R](p: Rep[PairCol[L, R]]) = unmkPairOfCols(p)
  }
  lazy val PairOfColsRep: Rep[PairOfColsCompanionCtor] = new PairOfColsCompanionCtor
  lazy val RPairOfCols: PairOfColsCompanionCtor = proxyPairOfColsCompanion(PairOfColsRep)
  implicit def proxyPairOfColsCompanion(p: Rep[PairOfColsCompanionCtor]): PairOfColsCompanionCtor = {
    if (p.rhs.isInstanceOf[PairOfColsCompanionCtor])
      p.rhs.asInstanceOf[PairOfColsCompanionCtor]
    else
      proxyOps[PairOfColsCompanionCtor](p)
  }

  implicit case object PairOfColsCompanionElem extends CompanionElem[PairOfColsCompanionCtor] {
    lazy val tag = weakTypeTag[PairOfColsCompanionCtor]
    protected def getDefaultRep = PairOfColsRep
  }

  implicit def proxyPairOfCols[L, R](p: Rep[PairOfCols[L, R]]): PairOfCols[L, R] =
    proxyOps[PairOfCols[L, R]](p)

  implicit class ExtendedPairOfCols[L, R](p: Rep[PairOfCols[L, R]]) {
    def toData: Rep[PairOfColsData[L, R]] = {
      implicit val eL = p.ls.eA;
implicit val eR = p.rs.eA
      isoPairOfCols(eL, eR).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoPairOfCols[L, R](implicit eL: Elem[L], eR: Elem[R]): Iso[PairOfColsData[L, R], PairOfCols[L, R]] =
    reifyObject(new PairOfColsIso[L, R]()(eL, eR))

  def mkPairOfCols[L, R]
    (ls: Rep[Col[L]], rs: Rep[Col[R]]): Rep[PairOfCols[L, R]] = {
    new PairOfColsCtor[L, R](ls, rs)
  }
  def unmkPairOfCols[L, R](p: Rep[PairCol[L, R]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: PairOfColsElem[L, R] @unchecked =>
      Some((asRep[PairOfCols[L, R]](p).ls, asRep[PairOfCols[L, R]](p).rs))
    case _ =>
      None
  }

    object PairOfColsMethods {
    object builder {
      def unapply(d: Def[_]): Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "builder" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object arr {
      def unapply(d: Def[_]): Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "arr" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object length {
      def unapply(d: Def[_]): Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "length" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[PairOfCols[L, R]] forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object apply {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[Int]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "apply" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[Int]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[Int]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object getOrElse {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[Int], Rep[Thunk[(L, R)]]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "getOrElse" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[Int], Rep[Thunk[(L, R)]]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[Int], Rep[Thunk[(L, R)]]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object map {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => V]) forSome {type L; type R; type V}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "map" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => V]) forSome {type L; type R; type V}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => V]) forSome {type L; type R; type V}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object foreach {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Unit]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "foreach" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Unit]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Unit]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object exists {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "exists" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object forall {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "forall" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object filter {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "filter" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[((L, R)) => Boolean]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fold {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[B], Rep[((B, (L, R))) => B]) forSome {type L; type R; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "fold" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[B], Rep[((B, (L, R))) => B]) forSome {type L; type R; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[B], Rep[((B, (L, R))) => B]) forSome {type L; type R; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object slice {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[Int], Rep[Int]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "slice" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[Int], Rep[Int]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[Int], Rep[Int]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object append {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[Col[(L, R)]]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "append" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[Col[(L, R)]]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[Col[(L, R)]]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object sum {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[Monoid[(L, R)]]) forSome {type L; type R}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "sum" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[Monoid[(L, R)]]) forSome {type L; type R}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[Monoid[(L, R)]]) forSome {type L; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object zip {
      def unapply(d: Def[_]): Nullable[(Rep[PairOfCols[L, R]], Rep[Col[B]]) forSome {type L; type R; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[PairOfColsElem[_, _]] && method.getName == "zip" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[PairOfCols[L, R]], Rep[Col[B]]) forSome {type L; type R; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[PairOfCols[L, R]], Rep[Col[B]]) forSome {type L; type R; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object PairOfColsCompanionMethods {
  }
} // of object PairOfCols
  registerEntityObject("PairOfCols", PairOfCols)

object ReplCol extends EntityObject("ReplCol") {
  case class ReplColCtor[A]
      (override val value: Rep[A], override val length: Rep[Int])
    extends ReplCol[A](value, length) with Def[ReplCol[A]] {
    implicit lazy val eA = value.elem

    lazy val selfType = element[ReplCol[A]]
    private val thisClass = classOf[ReplCol[A]]

    override def getOrElse(i: Rep[Int], default: Rep[Thunk[A]]): Rep[A] = {
      asRep[A](mkMethodCall(self,
        thisClass.getMethod("getOrElse", classOf[Sym], classOf[Sym]),
        List(i, default),
        true, element[A]))
    }

    override def foreach(f: Rep[A => Unit]): Rep[Unit] = {
      asRep[Unit](mkMethodCall(self,
        thisClass.getMethod("foreach", classOf[Sym]),
        List(f),
        true, element[Unit]))
    }

    override def fold[B](zero: Rep[B])(op: Rep[((B, A)) => B]): Rep[B] = {
      implicit val eB = zero.elem
      asRep[B](mkMethodCall(self,
        thisClass.getMethod("fold", classOf[Sym], classOf[Sym]),
        List(zero, op),
        true, element[B]))
    }

    override def append(other: Rep[Col[A]]): Rep[Col[A]] = {
      asRep[Col[A]](mkMethodCall(self,
        thisClass.getMethod("append", classOf[Sym]),
        List(other),
        true, element[Col[A]]))
    }
  }
  // elem for concrete class
  class ReplColElem[A](val iso: Iso[ReplColData[A], ReplCol[A]])(implicit override val eA: Elem[A])
    extends ColElem[A, ReplCol[A]]
    with ConcreteElem1[A, ReplColData[A], ReplCol[A], Col] {
    override lazy val parent: Option[Elem[_]] = Some(colElement(element[A]))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("A" -> (eA -> scalan.util.Invariant))
    override def convertCol(x: Rep[Col[A]]) = // Converter is not generated by meta
!!!("Cannot convert from Col to ReplCol: missing fields List(value)")
    override def getDefaultRep = RReplCol(element[A].defaultRepValue, 0)
    override lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[ReplCol[A]]
    }
  }

  // state representation type
  type ReplColData[A] = (A, Int)

  // 3) Iso for concrete class
  class ReplColIso[A](implicit eA: Elem[A])
    extends EntityIso[ReplColData[A], ReplCol[A]] with Def[ReplColIso[A]] {
    private lazy val _safeFrom = fun { p: Rep[ReplCol[A]] => (p.value, p.length) }
    override def from(p: Rep[ReplCol[A]]) =
      tryConvert[ReplCol[A], (A, Int)](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[(A, Int)]) = {
      val Pair(value, length) = p
      RReplCol(value, length)
    }
    lazy val eFrom = pairElement(element[A], element[Int])
    lazy val eTo = new ReplColElem[A](self)
    lazy val selfType = new ReplColIsoElem[A](eA)
    def productArity = 1
    def productElement(n: Int) = eA
  }
  case class ReplColIsoElem[A](eA: Elem[A]) extends Elem[ReplColIso[A]] {
    def getDefaultRep = reifyObject(new ReplColIso[A]()(eA))
    lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[ReplColIso[A]]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("A" -> (eA -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class ReplColCompanionCtor extends CompanionDef[ReplColCompanionCtor] with ReplColCompanion {
    def selfType = ReplColCompanionElem
    override def toString = "ReplColCompanion"
    @scalan.OverloadId("fromData")
    def apply[A](p: Rep[ReplColData[A]]): Rep[ReplCol[A]] = {
      implicit val eA = p._1.elem
      isoReplCol[A].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[A](value: Rep[A], length: Rep[Int]): Rep[ReplCol[A]] =
      mkReplCol(value, length)

    def unapply[A](p: Rep[Col[A]]) = unmkReplCol(p)
  }
  lazy val ReplColRep: Rep[ReplColCompanionCtor] = new ReplColCompanionCtor
  lazy val RReplCol: ReplColCompanionCtor = proxyReplColCompanion(ReplColRep)
  implicit def proxyReplColCompanion(p: Rep[ReplColCompanionCtor]): ReplColCompanionCtor = {
    if (p.rhs.isInstanceOf[ReplColCompanionCtor])
      p.rhs.asInstanceOf[ReplColCompanionCtor]
    else
      proxyOps[ReplColCompanionCtor](p)
  }

  implicit case object ReplColCompanionElem extends CompanionElem[ReplColCompanionCtor] {
    lazy val tag = weakTypeTag[ReplColCompanionCtor]
    protected def getDefaultRep = ReplColRep
  }

  implicit def proxyReplCol[A](p: Rep[ReplCol[A]]): ReplCol[A] =
    proxyOps[ReplCol[A]](p)

  implicit class ExtendedReplCol[A](p: Rep[ReplCol[A]]) {
    def toData: Rep[ReplColData[A]] = {
      implicit val eA = p.value.elem
      isoReplCol(eA).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoReplCol[A](implicit eA: Elem[A]): Iso[ReplColData[A], ReplCol[A]] =
    reifyObject(new ReplColIso[A]()(eA))

  def mkReplCol[A]
    (value: Rep[A], length: Rep[Int]): Rep[ReplCol[A]] = {
    new ReplColCtor[A](value, length)
  }
  def unmkReplCol[A](p: Rep[Col[A]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: ReplColElem[A] @unchecked =>
      Some((asRep[ReplCol[A]](p).value, asRep[ReplCol[A]](p).length))
    case _ =>
      None
  }

    object ReplColMethods {
    object builder {
      def unapply(d: Def[_]): Nullable[Rep[ReplCol[A]] forSome {type A}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "builder" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[ReplCol[A]] forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[ReplCol[A]] forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object arr {
      def unapply(d: Def[_]): Nullable[Rep[ReplCol[A]] forSome {type A}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "arr" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[ReplCol[A]] forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[ReplCol[A]] forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object apply {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[Int]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "apply" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[Int]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[Int]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object getOrElse {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[Int], Rep[Thunk[A]]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "getOrElse" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[Int], Rep[Thunk[A]]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[Int], Rep[Thunk[A]]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object map {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[A => B]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "map" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[A => B]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[A => B]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object foreach {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[A => Unit]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "foreach" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[A => Unit]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[A => Unit]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object exists {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "exists" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object forall {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "forall" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object filter {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "filter" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[A => Boolean]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fold {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[B], Rep[((B, A)) => B]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "fold" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[B], Rep[((B, A)) => B]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[B], Rep[((B, A)) => B]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object zip {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[Col[B]]) forSome {type A; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "zip" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[Col[B]]) forSome {type A; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[Col[B]]) forSome {type A; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object slice {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[Int], Rep[Int]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "slice" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[Int], Rep[Int]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[Int], Rep[Int]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object append {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[Col[A]]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "append" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[Col[A]]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[Col[A]]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object sum {
      def unapply(d: Def[_]): Nullable[(Rep[ReplCol[A]], Rep[Monoid[A]]) forSome {type A}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[ReplColElem[_]] && method.getName == "sum" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[ReplCol[A]], Rep[Monoid[A]]) forSome {type A}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[ReplCol[A]], Rep[Monoid[A]]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object ReplColCompanionMethods {
  }
} // of object ReplCol
  registerEntityObject("ReplCol", ReplCol)

object ReplColBuilder extends EntityObject("ReplColBuilder") {
  case class ReplColBuilderCtor
      ()
    extends ReplColBuilder() with Def[ReplColBuilder] {
    lazy val selfType = element[ReplColBuilder]
  }
  // elem for concrete class
  class ReplColBuilderElem(val iso: Iso[ReplColBuilderData, ReplColBuilder])
    extends BaseColBuilderElem[ReplColBuilder]
    with ConcreteElem[ReplColBuilderData, ReplColBuilder] {
    override lazy val parent: Option[Elem[_]] = Some(baseColBuilderElement)
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs()
    override def convertBaseColBuilder(x: Rep[BaseColBuilder]) = RReplColBuilder()
    override def getDefaultRep = RReplColBuilder()
    override lazy val tag = {
      weakTypeTag[ReplColBuilder]
    }
  }

  // state representation type
  type ReplColBuilderData = Unit

  // 3) Iso for concrete class
  class ReplColBuilderIso
    extends EntityIso[ReplColBuilderData, ReplColBuilder] with Def[ReplColBuilderIso] {
    private lazy val _safeFrom = fun { p: Rep[ReplColBuilder] => () }
    override def from(p: Rep[ReplColBuilder]) =
      tryConvert[ReplColBuilder, Unit](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[Unit]) = {
      val unit = p
      RReplColBuilder()
    }
    lazy val eFrom = UnitElement
    lazy val eTo = new ReplColBuilderElem(self)
    lazy val selfType = new ReplColBuilderIsoElem
    def productArity = 0
    def productElement(n: Int) = ???
  }
  case class ReplColBuilderIsoElem() extends Elem[ReplColBuilderIso] {
    def getDefaultRep = reifyObject(new ReplColBuilderIso())
    lazy val tag = {
      weakTypeTag[ReplColBuilderIso]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs()
  }
  // 4) constructor and deconstructor
  class ReplColBuilderCompanionCtor extends CompanionDef[ReplColBuilderCompanionCtor] with ReplColBuilderCompanion {
    def selfType = ReplColBuilderCompanionElem
    override def toString = "ReplColBuilderCompanion"
    @scalan.OverloadId("fromData")
    def apply(p: Rep[ReplColBuilderData]): Rep[ReplColBuilder] = {
      isoReplColBuilder.to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply(): Rep[ReplColBuilder] =
      mkReplColBuilder()

    def unapply(p: Rep[BaseColBuilder]) = unmkReplColBuilder(p)
  }
  lazy val ReplColBuilderRep: Rep[ReplColBuilderCompanionCtor] = new ReplColBuilderCompanionCtor
  lazy val RReplColBuilder: ReplColBuilderCompanionCtor = proxyReplColBuilderCompanion(ReplColBuilderRep)
  implicit def proxyReplColBuilderCompanion(p: Rep[ReplColBuilderCompanionCtor]): ReplColBuilderCompanionCtor = {
    if (p.rhs.isInstanceOf[ReplColBuilderCompanionCtor])
      p.rhs.asInstanceOf[ReplColBuilderCompanionCtor]
    else
      proxyOps[ReplColBuilderCompanionCtor](p)
  }

  implicit case object ReplColBuilderCompanionElem extends CompanionElem[ReplColBuilderCompanionCtor] {
    lazy val tag = weakTypeTag[ReplColBuilderCompanionCtor]
    protected def getDefaultRep = ReplColBuilderRep
  }

  implicit def proxyReplColBuilder(p: Rep[ReplColBuilder]): ReplColBuilder =
    proxyOps[ReplColBuilder](p)

  implicit class ExtendedReplColBuilder(p: Rep[ReplColBuilder]) {
    def toData: Rep[ReplColBuilderData] = {
      isoReplColBuilder.from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoReplColBuilder: Iso[ReplColBuilderData, ReplColBuilder] =
    reifyObject(new ReplColBuilderIso())

  def mkReplColBuilder
    (): Rep[ReplColBuilder] = {
    new ReplColBuilderCtor()
  }
  def unmkReplColBuilder(p: Rep[BaseColBuilder]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: ReplColBuilderElem @unchecked =>
      Some(())
    case _ =>
      None
  }

    object ReplColBuilderMethods {
  }

  object ReplColBuilderCompanionMethods {
  }
} // of object ReplColBuilder
  registerEntityObject("ReplColBuilder", ReplColBuilder)

  registerModule(ColsOverArraysModule)
}

object ColsOverArraysModule extends scalan.ModuleInfo("special.collection", "ColsOverArrays")
}

trait ColsOverArraysModule extends special.collection.impl.ColsOverArraysDefs {self: Library =>}
