package special.collection

import scalan._
import scala.reflect.runtime.universe._
import scala.reflect._

package impl {
// Abs -----------------------------------
trait CostedOptionsDefs extends scalan.Scalan with CostedOptions {
  self: Library =>
import IsoUR._
import Converter._
import Costed._
import WOption._
import ConcreteCosted._
import CostedPrim._
import CostedOption._
import CostedSome._
import CostedNone._

object CostedOption extends EntityObject("CostedOption") {

  case class CostedOptionAdapter[A](source: Rep[CostedOption[A]])
    extends CostedOption[A] with Def[CostedOption[A]] {
    implicit val eT: Elem[A] = source.elem.eT
    val selfType: Elem[CostedOption[A]] = costedOptionElement(eT)
    private val thisClass = classOf[CostedOption[A]]
    implicit val eVal: Elem[WOption[A]] = wOptionElement(eT)

    def get: Rep[Costed[A]] = {
      asRep[Costed[A]](mkMethodCall(source,
        thisClass.getMethod("get"),
        List(),
        true, element[Costed[A]]))
    }

    def getOrElse(default: Rep[Costed[A]]): Rep[Costed[A]] = {
      asRep[Costed[A]](mkMethodCall(source,
        thisClass.getMethod("getOrElse", classOf[Sym]),
        List(default),
        true, element[Costed[A]]))
    }

    def fold[B](ifEmpty: Rep[Costed[() => B]], f: Rep[Costed[A => B]]): Rep[Costed[B]] = {
      implicit val eB = f.elem.eVal.eRange
      asRep[Costed[B]](mkMethodCall(source,
        thisClass.getMethod("fold", classOf[Sym], classOf[Sym]),
        List(ifEmpty, f),
        true, element[Costed[B]]))
    }

    def isEmpty: Rep[Costed[Boolean]] = {
      asRep[Costed[Boolean]](mkMethodCall(source,
        thisClass.getMethod("isEmpty"),
        List(),
        true, element[Costed[Boolean]]))
    }

    def isDefined: Rep[Costed[Boolean]] = {
      asRep[Costed[Boolean]](mkMethodCall(source,
        thisClass.getMethod("isDefined"),
        List(),
        true, element[Costed[Boolean]]))
    }

    def filter(p: Rep[Costed[A => Boolean]]): Rep[Costed[WOption[A]]] = {
      asRep[Costed[WOption[A]]](mkMethodCall(source,
        thisClass.getMethod("filter", classOf[Sym]),
        List(p),
        true, element[Costed[WOption[A]]]))
    }

    def flatMap[B](f: Rep[Costed[A => WOption[B]]]): Rep[Costed[WOption[B]]] = {
      implicit val eB = f.elem.eVal.eRange.eItem
      asRep[Costed[WOption[B]]](mkMethodCall(source,
        thisClass.getMethod("flatMap", classOf[Sym]),
        List(f),
        true, element[Costed[WOption[B]]]))
    }

    def map[B](f: Rep[Costed[A => B]]): Rep[Costed[WOption[B]]] = {
      implicit val eB = f.elem.eVal.eRange
      asRep[Costed[WOption[B]]](mkMethodCall(source,
        thisClass.getMethod("map", classOf[Sym]),
        List(f),
        true, element[Costed[WOption[B]]]))
    }

    def value: Rep[WOption[A]] = {
      asRep[WOption[A]](mkMethodCall(source,
        thisClass.getMethod("value"),
        List(),
        true, element[WOption[A]]))
    }


    def cost: Rep[Int] = {
      asRep[Int](mkMethodCall(source,
        thisClass.getMethod("cost"),
        List(),
        true, element[Int]))
    }

    def dataSize: Rep[Long] = {
      asRep[Long](mkMethodCall(source,
        thisClass.getMethod("dataSize"),
        List(),
        true, element[Long]))
    }
  }

  // entityProxy: single proxy for each type family
  implicit def proxyCostedOption[T](p: Rep[CostedOption[T]]): CostedOption[T] = {
    if (p.rhs.isInstanceOf[CostedOption[T]@unchecked]) p.rhs.asInstanceOf[CostedOption[T]]
    else
      CostedOptionAdapter(p)
//      proxyOps[CostedOption[T]](p)(scala.reflect.classTag[CostedOption[T]])
  }

  implicit def castCostedOptionElement[A](elem: Elem[CostedOption[A]]): CostedOptionElem[A, CostedOption[A]] =
    elem.asInstanceOf[CostedOptionElem[A, CostedOption[A]]]

  // familyElem
  class CostedOptionElem[T, To <: CostedOption[T]](implicit _eT: Elem[T])
    extends ConcreteCostedElem[WOption[T], To] {
    def eT = _eT

    override lazy val parent: Option[Elem[_]] = Some(concreteCostedElement(wOptionElement(element[T])))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("T" -> (eT -> scalan.util.Invariant))
    override lazy val tag = {
      implicit val tagT = eT.tag
      weakTypeTag[CostedOption[T]].asInstanceOf[WeakTypeTag[To]]
    }
    override def convert(x: Rep[Def[_]]) = {
      val conv = fun {x: Rep[CostedOption[T]] => convertCostedOption(x) }
      tryConvert(element[CostedOption[T]], this, x, conv)
    }

    def convertCostedOption(x: Rep[CostedOption[T]]): Rep[To] = {
      x.elem match {
        case _: CostedOptionElem[_, _] => asRep[To](x)
        case e => !!!(s"Expected $x to have CostedOptionElem[_, _], but got $e", x)
      }
    }
    override def getDefaultRep: Rep[To] = ???
  }

  implicit def costedOptionElement[T](implicit eT: Elem[T]): Elem[CostedOption[T]] =
    cachedElem[CostedOptionElem[T, CostedOption[T]]](eT)

  implicit case object CostedOptionCompanionElem extends CompanionElem[CostedOptionCompanionCtor] {
    lazy val tag = weakTypeTag[CostedOptionCompanionCtor]
    protected def getDefaultRep = RCostedOption
  }

  abstract class CostedOptionCompanionCtor extends CompanionDef[CostedOptionCompanionCtor] with CostedOptionCompanion {
    def selfType = CostedOptionCompanionElem
    override def toString = "CostedOption"
  }
  implicit def proxyCostedOptionCompanionCtor(p: Rep[CostedOptionCompanionCtor]): CostedOptionCompanionCtor =
    proxyOps[CostedOptionCompanionCtor](p)

  lazy val RCostedOption: Rep[CostedOptionCompanionCtor] = new CostedOptionCompanionCtor {
    private val thisClass = classOf[CostedOptionCompanion]
  }

  object CostedOptionMethods {
    object get {
      def unapply(d: Def[_]): Nullable[Rep[CostedOption[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "get" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedOption[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedOption[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object getOrElse {
      def unapply(d: Def[_]): Nullable[(Rep[CostedOption[T]], Rep[Costed[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "getOrElse" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedOption[T]], Rep[Costed[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedOption[T]], Rep[Costed[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fold {
      def unapply(d: Def[_]): Nullable[(Rep[CostedOption[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "fold" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedOption[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedOption[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object isEmpty {
      def unapply(d: Def[_]): Nullable[Rep[CostedOption[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "isEmpty" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedOption[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedOption[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object isDefined {
      def unapply(d: Def[_]): Nullable[Rep[CostedOption[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "isDefined" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedOption[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedOption[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object filter {
      def unapply(d: Def[_]): Nullable[(Rep[CostedOption[T]], Rep[Costed[T => Boolean]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "filter" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedOption[T]], Rep[Costed[T => Boolean]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedOption[T]], Rep[Costed[T => Boolean]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object flatMap {
      def unapply(d: Def[_]): Nullable[(Rep[CostedOption[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "flatMap" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedOption[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedOption[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object map {
      def unapply(d: Def[_]): Nullable[(Rep[CostedOption[T]], Rep[Costed[T => B]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedOptionElem[_, _]] && method.getName == "map" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedOption[T]], Rep[Costed[T => B]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedOption[T]], Rep[Costed[T => B]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object CostedOptionCompanionMethods {
  }
} // of object CostedOption
  registerEntityObject("CostedOption", CostedOption)

object CostedSome extends EntityObject("CostedSome") {
  case class CostedSomeCtor[T]
      (override val costedValue: Rep[Costed[T]])
    extends CostedSome[T](costedValue) with Def[CostedSome[T]] {
    implicit lazy val eT = costedValue.eVal
    override lazy val eVal: Elem[WOption[T]] = implicitly[Elem[WOption[T]]]
    lazy val selfType = element[CostedSome[T]]
    private val thisClass = classOf[CostedSome[T]]

    override def fold[B](ifEmpty: Rep[Costed[Function0[B]]], f: Rep[Costed[T => B]]): Rep[Costed[B]] = {
      implicit val eB = f.eVal.eRange
      asRep[Costed[B]](mkMethodCall(self,
        thisClass.getMethod("fold", classOf[Sym], classOf[Sym]),
        List(ifEmpty, f),
        true, element[Costed[B]]))
    }

    override def filter(p: Rep[Costed[T => Boolean]]): Rep[Costed[WOption[T]]] = {
      asRep[Costed[WOption[T]]](mkMethodCall(self,
        thisClass.getMethod("filter", classOf[Sym]),
        List(p),
        true, element[Costed[WOption[T]]]))
    }

    override def flatMap[B](f: Rep[Costed[T => WOption[B]]]): Rep[Costed[WOption[B]]] = {
      implicit val eB = f.eVal.eRange.typeArgs("A")._1.asElem[B]
      asRep[Costed[WOption[B]]](mkMethodCall(self,
        thisClass.getMethod("flatMap", classOf[Sym]),
        List(f),
        true, element[Costed[WOption[B]]]))
    }

    override def map[B](f: Rep[Costed[T => B]]): Rep[Costed[WOption[B]]] = {
      implicit val eB = f.eVal.eRange
      asRep[Costed[WOption[B]]](mkMethodCall(self,
        thisClass.getMethod("map", classOf[Sym]),
        List(f),
        true, element[Costed[WOption[B]]]))
    }
  }
  // elem for concrete class
  class CostedSomeElem[T](val iso: Iso[CostedSomeData[T], CostedSome[T]])(implicit override val eT: Elem[T])
    extends CostedOptionElem[T, CostedSome[T]]
    with ConcreteElem[CostedSomeData[T], CostedSome[T]] {
    override lazy val parent: Option[Elem[_]] = Some(costedOptionElement(element[T]))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("T" -> (eT -> scalan.util.Invariant))
    override def convertCostedOption(x: Rep[CostedOption[T]]) = // Converter is not generated by meta
!!!("Cannot convert from CostedOption to CostedSome: missing fields List(costedValue)")
    override def getDefaultRep = RCostedSome(element[Costed[T]].defaultRepValue)
    override lazy val tag = {
      implicit val tagT = eT.tag
      weakTypeTag[CostedSome[T]]
    }
  }

  // state representation type
  type CostedSomeData[T] = Costed[T]

  // 3) Iso for concrete class
  class CostedSomeIso[T](implicit eT: Elem[T])
    extends EntityIso[CostedSomeData[T], CostedSome[T]] with Def[CostedSomeIso[T]] {
    private lazy val _safeFrom = fun { p: Rep[CostedSome[T]] => p.costedValue }
    override def from(p: Rep[CostedSome[T]]) =
      tryConvert[CostedSome[T], Costed[T]](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[Costed[T]]) = {
      val costedValue = p
      RCostedSome(costedValue)
    }
    lazy val eFrom = element[Costed[T]]
    lazy val eTo = new CostedSomeElem[T](self)
    lazy val selfType = new CostedSomeIsoElem[T](eT)
    def productArity = 1
    def productElement(n: Int) = eT
  }
  case class CostedSomeIsoElem[T](eT: Elem[T]) extends Elem[CostedSomeIso[T]] {
    def getDefaultRep = reifyObject(new CostedSomeIso[T]()(eT))
    lazy val tag = {
      implicit val tagT = eT.tag
      weakTypeTag[CostedSomeIso[T]]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("T" -> (eT -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class CostedSomeCompanionCtor extends CompanionDef[CostedSomeCompanionCtor] with CostedSomeCompanion {
    def selfType = CostedSomeCompanionElem
    override def toString = "CostedSomeCompanion"

    @scalan.OverloadId("fromFields")
    def apply[T](costedValue: Rep[Costed[T]]): Rep[CostedSome[T]] =
      mkCostedSome(costedValue)

    def unapply[T](p: Rep[CostedOption[T]]) = unmkCostedSome(p)
  }
  lazy val CostedSomeRep: Rep[CostedSomeCompanionCtor] = new CostedSomeCompanionCtor
  lazy val RCostedSome: CostedSomeCompanionCtor = proxyCostedSomeCompanion(CostedSomeRep)
  implicit def proxyCostedSomeCompanion(p: Rep[CostedSomeCompanionCtor]): CostedSomeCompanionCtor = {
    if (p.rhs.isInstanceOf[CostedSomeCompanionCtor])
      p.rhs.asInstanceOf[CostedSomeCompanionCtor]
    else
      proxyOps[CostedSomeCompanionCtor](p)
  }

  implicit case object CostedSomeCompanionElem extends CompanionElem[CostedSomeCompanionCtor] {
    lazy val tag = weakTypeTag[CostedSomeCompanionCtor]
    protected def getDefaultRep = CostedSomeRep
  }

  implicit def proxyCostedSome[T](p: Rep[CostedSome[T]]): CostedSome[T] =
    proxyOps[CostedSome[T]](p)

  implicit class ExtendedCostedSome[T](p: Rep[CostedSome[T]]) {
    def toData: Rep[CostedSomeData[T]] = {
      implicit val eT = p.costedValue.eVal
      isoCostedSome(eT).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoCostedSome[T](implicit eT: Elem[T]): Iso[CostedSomeData[T], CostedSome[T]] =
    reifyObject(new CostedSomeIso[T]()(eT))

  def mkCostedSome[T]
    (costedValue: Rep[Costed[T]]): Rep[CostedSome[T]] = {
    new CostedSomeCtor[T](costedValue)
  }
  def unmkCostedSome[T](p: Rep[CostedOption[T]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: CostedSomeElem[T] @unchecked =>
      Some((asRep[CostedSome[T]](p).costedValue))
    case _ =>
      None
  }

    object CostedSomeMethods {
    object value {
      def unapply(d: Def[_]): Nullable[Rep[CostedSome[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "value" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedSome[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedSome[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object dataSize {
      def unapply(d: Def[_]): Nullable[Rep[CostedSome[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "dataSize" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedSome[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedSome[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object cost {
      def unapply(d: Def[_]): Nullable[Rep[CostedSome[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "cost" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedSome[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedSome[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object get {
      def unapply(d: Def[_]): Nullable[Rep[CostedSome[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "get" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedSome[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedSome[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object getOrElse {
      def unapply(d: Def[_]): Nullable[(Rep[CostedSome[T]], Rep[Costed[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "getOrElse" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedSome[T]], Rep[Costed[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedSome[T]], Rep[Costed[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fold {
      def unapply(d: Def[_]): Nullable[(Rep[CostedSome[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "fold" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedSome[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedSome[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object isEmpty {
      def unapply(d: Def[_]): Nullable[Rep[CostedSome[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "isEmpty" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedSome[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedSome[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object isDefined {
      def unapply(d: Def[_]): Nullable[Rep[CostedSome[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "isDefined" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedSome[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedSome[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object filter {
      def unapply(d: Def[_]): Nullable[(Rep[CostedSome[T]], Rep[Costed[T => Boolean]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "filter" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedSome[T]], Rep[Costed[T => Boolean]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedSome[T]], Rep[Costed[T => Boolean]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object flatMap {
      def unapply(d: Def[_]): Nullable[(Rep[CostedSome[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "flatMap" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedSome[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedSome[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object map {
      def unapply(d: Def[_]): Nullable[(Rep[CostedSome[T]], Rep[Costed[T => B]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedSomeElem[_]] && method.getName == "map" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedSome[T]], Rep[Costed[T => B]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedSome[T]], Rep[Costed[T => B]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object CostedSomeCompanionMethods {
  }
} // of object CostedSome
  registerEntityObject("CostedSome", CostedSome)

object CostedNone extends EntityObject("CostedNone") {
  case class CostedNoneCtor[T]
      (override val cost: Rep[Int])(implicit eT: Elem[T])
    extends CostedNone[T](cost) with Def[CostedNone[T]] {
    override lazy val eVal: Elem[WOption[T]] = implicitly[Elem[WOption[T]]]
    lazy val selfType = element[CostedNone[T]]
    private val thisClass = classOf[CostedNone[T]]

    override def getOrElse(default: Rep[Costed[T]]): Rep[Costed[T]] = {
      asRep[Costed[T]](mkMethodCall(self,
        thisClass.getMethod("getOrElse", classOf[Sym]),
        List(default),
        true, element[Costed[T]]))
    }

    override def fold[B](ifEmpty: Rep[Costed[Function0[B]]], f: Rep[Costed[T => B]]): Rep[Costed[B]] = {
      implicit val eB = f.eVal.eRange
      asRep[Costed[B]](mkMethodCall(self,
        thisClass.getMethod("fold", classOf[Sym], classOf[Sym]),
        List(ifEmpty, f),
        true, element[Costed[B]]))
    }

    override def filter(p: Rep[Costed[T => Boolean]]): Rep[Costed[WOption[T]]] = {
      asRep[Costed[WOption[T]]](mkMethodCall(self,
        thisClass.getMethod("filter", classOf[Sym]),
        List(p),
        true, element[Costed[WOption[T]]]))
    }

    override def flatMap[B](f: Rep[Costed[T => WOption[B]]]): Rep[Costed[WOption[B]]] = {
      implicit val eB = f.eVal.eRange.typeArgs("A")._1.asElem[B]
      asRep[Costed[WOption[B]]](mkMethodCall(self,
        thisClass.getMethod("flatMap", classOf[Sym]),
        List(f),
        true, element[Costed[WOption[B]]]))
    }

    override def map[B](f: Rep[Costed[T => B]]): Rep[Costed[WOption[B]]] = {
      implicit val eB = f.eVal.eRange
      asRep[Costed[WOption[B]]](mkMethodCall(self,
        thisClass.getMethod("map", classOf[Sym]),
        List(f),
        true, element[Costed[WOption[B]]]))
    }
  }
  // elem for concrete class
  class CostedNoneElem[T](val iso: Iso[CostedNoneData[T], CostedNone[T]])(implicit override val eT: Elem[T])
    extends CostedOptionElem[T, CostedNone[T]]
    with ConcreteElem[CostedNoneData[T], CostedNone[T]] {
    override lazy val parent: Option[Elem[_]] = Some(costedOptionElement(element[T]))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("T" -> (eT -> scalan.util.Invariant))
    override def convertCostedOption(x: Rep[CostedOption[T]]) = RCostedNone(x.cost)
    override def getDefaultRep = RCostedNone(0)
    override lazy val tag = {
      implicit val tagT = eT.tag
      weakTypeTag[CostedNone[T]]
    }
  }

  // state representation type
  type CostedNoneData[T] = Int

  // 3) Iso for concrete class
  class CostedNoneIso[T](implicit eT: Elem[T])
    extends EntityIso[CostedNoneData[T], CostedNone[T]] with Def[CostedNoneIso[T]] {
    private lazy val _safeFrom = fun { p: Rep[CostedNone[T]] => p.cost }
    override def from(p: Rep[CostedNone[T]]) =
      tryConvert[CostedNone[T], Int](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[Int]) = {
      val cost = p
      RCostedNone(cost)
    }
    lazy val eFrom = element[Int]
    lazy val eTo = new CostedNoneElem[T](self)
    lazy val selfType = new CostedNoneIsoElem[T](eT)
    def productArity = 1
    def productElement(n: Int) = eT
  }
  case class CostedNoneIsoElem[T](eT: Elem[T]) extends Elem[CostedNoneIso[T]] {
    def getDefaultRep = reifyObject(new CostedNoneIso[T]()(eT))
    lazy val tag = {
      implicit val tagT = eT.tag
      weakTypeTag[CostedNoneIso[T]]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("T" -> (eT -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class CostedNoneCompanionCtor extends CompanionDef[CostedNoneCompanionCtor] with CostedNoneCompanion {
    def selfType = CostedNoneCompanionElem
    override def toString = "CostedNoneCompanion"

    @scalan.OverloadId("fromFields")
    def apply[T](cost: Rep[Int])(implicit eT: Elem[T]): Rep[CostedNone[T]] =
      mkCostedNone(cost)

    def unapply[T](p: Rep[CostedOption[T]]) = unmkCostedNone(p)
  }
  lazy val CostedNoneRep: Rep[CostedNoneCompanionCtor] = new CostedNoneCompanionCtor
  lazy val RCostedNone: CostedNoneCompanionCtor = proxyCostedNoneCompanion(CostedNoneRep)
  implicit def proxyCostedNoneCompanion(p: Rep[CostedNoneCompanionCtor]): CostedNoneCompanionCtor = {
    if (p.rhs.isInstanceOf[CostedNoneCompanionCtor])
      p.rhs.asInstanceOf[CostedNoneCompanionCtor]
    else
      proxyOps[CostedNoneCompanionCtor](p)
  }

  implicit case object CostedNoneCompanionElem extends CompanionElem[CostedNoneCompanionCtor] {
    lazy val tag = weakTypeTag[CostedNoneCompanionCtor]
    protected def getDefaultRep = CostedNoneRep
  }

  implicit def proxyCostedNone[T](p: Rep[CostedNone[T]]): CostedNone[T] =
    proxyOps[CostedNone[T]](p)

  implicit class ExtendedCostedNone[T](p: Rep[CostedNone[T]])(implicit eT: Elem[T]) {
    def toData: Rep[CostedNoneData[T]] = {
      isoCostedNone(eT).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoCostedNone[T](implicit eT: Elem[T]): Iso[CostedNoneData[T], CostedNone[T]] =
    reifyObject(new CostedNoneIso[T]()(eT))

  def mkCostedNone[T]
    (cost: Rep[Int])(implicit eT: Elem[T]): Rep[CostedNone[T]] = {
    new CostedNoneCtor[T](cost)
  }
  def unmkCostedNone[T](p: Rep[CostedOption[T]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: CostedNoneElem[T] @unchecked =>
      Some((asRep[CostedNone[T]](p).cost))
    case _ =>
      None
  }

    object CostedNoneMethods {
    object value {
      def unapply(d: Def[_]): Nullable[Rep[CostedNone[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "value" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedNone[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedNone[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object dataSize {
      def unapply(d: Def[_]): Nullable[Rep[CostedNone[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "dataSize" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedNone[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedNone[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object get {
      def unapply(d: Def[_]): Nullable[Rep[CostedNone[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "get" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedNone[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedNone[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object getOrElse {
      def unapply(d: Def[_]): Nullable[(Rep[CostedNone[T]], Rep[Costed[T]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "getOrElse" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedNone[T]], Rep[Costed[T]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedNone[T]], Rep[Costed[T]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object fold {
      def unapply(d: Def[_]): Nullable[(Rep[CostedNone[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "fold" =>
          val res = (receiver, args(0), args(1))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedNone[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedNone[T]], Rep[Costed[Function0[B]]], Rep[Costed[T => B]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object isEmpty {
      def unapply(d: Def[_]): Nullable[Rep[CostedNone[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "isEmpty" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedNone[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedNone[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object isDefined {
      def unapply(d: Def[_]): Nullable[Rep[CostedNone[T]] forSome {type T}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "isDefined" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[CostedNone[T]] forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[CostedNone[T]] forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object filter {
      def unapply(d: Def[_]): Nullable[(Rep[CostedNone[T]], Rep[Costed[T => Boolean]]) forSome {type T}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "filter" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedNone[T]], Rep[Costed[T => Boolean]]) forSome {type T}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedNone[T]], Rep[Costed[T => Boolean]]) forSome {type T}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object flatMap {
      def unapply(d: Def[_]): Nullable[(Rep[CostedNone[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "flatMap" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedNone[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedNone[T]], Rep[Costed[T => WOption[B]]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object map {
      def unapply(d: Def[_]): Nullable[(Rep[CostedNone[T]], Rep[Costed[T => B]]) forSome {type T; type B}] = d match {
        case MethodCall(receiver, method, args, _) if receiver.elem.isInstanceOf[CostedNoneElem[_]] && method.getName == "map" =>
          val res = (receiver, args(0))
          Nullable(res).asInstanceOf[Nullable[(Rep[CostedNone[T]], Rep[Costed[T => B]]) forSome {type T; type B}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[(Rep[CostedNone[T]], Rep[Costed[T => B]]) forSome {type T; type B}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }

  object CostedNoneCompanionMethods {
  }
} // of object CostedNone
  registerEntityObject("CostedNone", CostedNone)

  registerModule(CostedOptionsModule)
}

object CostedOptionsModule extends scalan.ModuleInfo("special.collection", "CostedOptions")
}

trait CostedOptionsModule extends special.collection.impl.CostedOptionsDefs {self: Library =>}
