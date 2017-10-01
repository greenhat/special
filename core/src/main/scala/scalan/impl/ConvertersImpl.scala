package scalan

import scalan.common.OverloadHack
import scalan.common.OverloadHack.Overloaded2
import scala.reflect.runtime.universe.{WeakTypeTag, weakTypeTag}
import scalan.meta.ScalanAst._

package impl {
// Abs -----------------------------------
trait ConvertersDefs extends Converters {
  self: Scalan =>

  // entityProxy: single proxy for each type family
  implicit def proxyConverter[T, R](p: Rep[Converter[T, R]]): Converter[T, R] = {
    proxyOps[Converter[T, R]](p)(scala.reflect.classTag[Converter[T, R]])
  }

  // familyElem
  class ConverterElem[T, R, To <: Converter[T, R]](implicit _eT: Elem[T], _eR: Elem[R])
    extends EntityElem[To] {
    def eT = _eT
    def eR = _eR
    lazy val parent: Option[Elem[_]] = None
    lazy val typeArgs = TypeArgs("T" -> (eT -> scalan.util.Invariant), "R" -> (eR -> scalan.util.Invariant))
    override lazy val tag = {
      implicit val tagT = eT.tag
      implicit val tagR = eR.tag
      weakTypeTag[Converter[T, R]].asInstanceOf[WeakTypeTag[To]]
    }
    override def convert(x: Rep[Def[_]]) = {
      val conv = fun {x: Rep[Converter[T, R]] => convertConverter(x) }
      tryConvert(element[Converter[T, R]], this, x, conv)
    }

    def convertConverter(x: Rep[Converter[T, R]]): Rep[To] = {
      x.elem match {
        case _: ConverterElem[_, _, _] => x.asRep[To]
        case e => !!!(s"Expected $x to have ConverterElem[_, _, _], but got $e", x)
      }
    }

    override def getDefaultRep: Rep[To] = ???
  }

  implicit def converterElement[T, R](implicit eT: Elem[T], eR: Elem[R]): Elem[Converter[T, R]] =
    cachedElem[ConverterElem[T, R, Converter[T, R]]](eT, eR)

  implicit case object ConverterCompanionElem extends CompanionElem[ConverterCompanionCtor] {
    lazy val tag = weakTypeTag[ConverterCompanionCtor]
    protected def getDefaultRep = Converter
  }

  abstract class ConverterCompanionCtor extends CompanionDef[ConverterCompanionCtor] with ConverterCompanion {
    def selfType = ConverterCompanionElem
    override def toString = "Converter"
  }
  implicit def proxyConverterCompanionCtor(p: Rep[ConverterCompanionCtor]): ConverterCompanionCtor =
    proxyOps[ConverterCompanionCtor](p)

  case class IdentityConvCtor[A]
      ()(implicit eT: Elem[A])
    extends IdentityConv[A]() with Def[IdentityConv[A]] {
    lazy val selfType = element[IdentityConv[A]]
  }
  // elem for concrete class
  class IdentityConvElem[A](val iso: Iso[IdentityConvData[A], IdentityConv[A]])(implicit override val eT: Elem[A])
    extends ConverterElem[A, A, IdentityConv[A]]
    with ConcreteElem[IdentityConvData[A], IdentityConv[A]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(element[A], element[A]))
    override lazy val typeArgs = TypeArgs("A" -> (eT -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[A, A]]) = IdentityConv()
    override def getDefaultRep = IdentityConv()
    override lazy val tag = {
      implicit val tagA = eT.tag
      weakTypeTag[IdentityConv[A]]
    }
  }

  // state representation type
  type IdentityConvData[A] = Unit

  // 3) Iso for concrete class
  class IdentityConvIso[A](implicit eT: Elem[A])
    extends EntityIso[IdentityConvData[A], IdentityConv[A]] with Def[IdentityConvIso[A]] {
    override def from(p: Rep[IdentityConv[A]]) =
      ()
    override def to(p: Rep[Unit]) = {
      val unit = p
      IdentityConv()
    }
    lazy val eFrom = UnitElement
    lazy val eTo = new IdentityConvElem[A](self)
    lazy val selfType = new IdentityConvIsoElem[A](eT)
    def productArity = 1
    def productElement(n: Int) = eT
  }
  case class IdentityConvIsoElem[A](eT: Elem[A]) extends Elem[IdentityConvIso[A]] {
    def getDefaultRep = reifyObject(new IdentityConvIso[A]()(eT))
    lazy val tag = {
      implicit val tagA = eT.tag
      weakTypeTag[IdentityConvIso[A]]
    }
    lazy val typeArgs = TypeArgs("A" -> (eT -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class IdentityConvCompanionCtor extends CompanionDef[IdentityConvCompanionCtor] {
    def selfType = IdentityConvCompanionElem
    override def toString = "IdentityConvCompanion"
    @scalan.OverloadId("fromData")
    def apply[A](p: Rep[IdentityConvData[A]])(implicit eT: Elem[A]): Rep[IdentityConv[A]] = {
      isoIdentityConv[A].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[A]()(implicit eT: Elem[A]): Rep[IdentityConv[A]] =
      mkIdentityConv()

    def unapply[A](p: Rep[Converter[A, A]]) = unmkIdentityConv(p)
  }
  lazy val IdentityConvRep: Rep[IdentityConvCompanionCtor] = new IdentityConvCompanionCtor
  lazy val IdentityConv: IdentityConvCompanionCtor = proxyIdentityConvCompanion(IdentityConvRep)
  implicit def proxyIdentityConvCompanion(p: Rep[IdentityConvCompanionCtor]): IdentityConvCompanionCtor = {
    proxyOps[IdentityConvCompanionCtor](p)
  }

  implicit case object IdentityConvCompanionElem extends CompanionElem[IdentityConvCompanionCtor] {
    lazy val tag = weakTypeTag[IdentityConvCompanionCtor]
    protected def getDefaultRep = IdentityConvRep
  }

  implicit def proxyIdentityConv[A](p: Rep[IdentityConv[A]]): IdentityConv[A] =
    proxyOps[IdentityConv[A]](p)

  implicit class ExtendedIdentityConv[A](p: Rep[IdentityConv[A]])(implicit eT: Elem[A]) {
    def toData: Rep[IdentityConvData[A]] = {
      isoIdentityConv(eT).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoIdentityConv[A](implicit eT: Elem[A]): Iso[IdentityConvData[A], IdentityConv[A]] =
    reifyObject(new IdentityConvIso[A]()(eT))

  case class BaseConverterCtor[T, R]
      (override val convFun: Rep[T => R])
    extends BaseConverter[T, R](convFun) with Def[BaseConverter[T, R]] {
    implicit val eT = convFun.elem.eDom;
implicit val eR = convFun.elem.eRange
    lazy val selfType = element[BaseConverter[T, R]]
  }
  // elem for concrete class
  class BaseConverterElem[T, R](val iso: Iso[BaseConverterData[T, R], BaseConverter[T, R]])(implicit override val eT: Elem[T], override val eR: Elem[R])
    extends ConverterElem[T, R, BaseConverter[T, R]]
    with ConcreteElem[BaseConverterData[T, R], BaseConverter[T, R]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(element[T], element[R]))
    override lazy val typeArgs = TypeArgs("T" -> (eT -> scalan.util.Invariant), "R" -> (eR -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[T, R]]) = BaseConverter(x.convFun)
    override def getDefaultRep = BaseConverter(constFun[T, R](element[R].defaultRepValue))
    override lazy val tag = {
      implicit val tagT = eT.tag
      implicit val tagR = eR.tag
      weakTypeTag[BaseConverter[T, R]]
    }
  }

  // state representation type
  type BaseConverterData[T, R] = T => R

  // 3) Iso for concrete class
  class BaseConverterIso[T, R](implicit eT: Elem[T], eR: Elem[R])
    extends EntityIso[BaseConverterData[T, R], BaseConverter[T, R]] with Def[BaseConverterIso[T, R]] {
    override def from(p: Rep[BaseConverter[T, R]]) =
      p.convFun
    override def to(p: Rep[T => R]) = {
      val convFun = p
      BaseConverter(convFun)
    }
    lazy val eFrom = element[T => R]
    lazy val eTo = new BaseConverterElem[T, R](self)
    lazy val selfType = new BaseConverterIsoElem[T, R](eT, eR)
    def productArity = 2
    def productElement(n: Int) = n match {
      case 0 => eT
      case 1 => eR
    }
  }
  case class BaseConverterIsoElem[T, R](eT: Elem[T], eR: Elem[R]) extends Elem[BaseConverterIso[T, R]] {
    def getDefaultRep = reifyObject(new BaseConverterIso[T, R]()(eT, eR))
    lazy val tag = {
      implicit val tagT = eT.tag
      implicit val tagR = eR.tag
      weakTypeTag[BaseConverterIso[T, R]]
    }
    lazy val typeArgs = TypeArgs("T" -> (eT -> scalan.util.Invariant), "R" -> (eR -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class BaseConverterCompanionCtor extends CompanionDef[BaseConverterCompanionCtor] with BaseConverterCompanion {
    def selfType = BaseConverterCompanionElem
    override def toString = "BaseConverterCompanion"

    @scalan.OverloadId("fromFields")
    def apply[T, R](convFun: Rep[T => R]): Rep[BaseConverter[T, R]] =
      mkBaseConverter(convFun)

    def unapply[T, R](p: Rep[Converter[T, R]]) = unmkBaseConverter(p)
  }
  lazy val BaseConverterRep: Rep[BaseConverterCompanionCtor] = new BaseConverterCompanionCtor
  lazy val BaseConverter: BaseConverterCompanionCtor = proxyBaseConverterCompanion(BaseConverterRep)
  implicit def proxyBaseConverterCompanion(p: Rep[BaseConverterCompanionCtor]): BaseConverterCompanionCtor = {
    proxyOps[BaseConverterCompanionCtor](p)
  }

  implicit case object BaseConverterCompanionElem extends CompanionElem[BaseConverterCompanionCtor] {
    lazy val tag = weakTypeTag[BaseConverterCompanionCtor]
    protected def getDefaultRep = BaseConverterRep
  }

  implicit def proxyBaseConverter[T, R](p: Rep[BaseConverter[T, R]]): BaseConverter[T, R] =
    proxyOps[BaseConverter[T, R]](p)

  implicit class ExtendedBaseConverter[T, R](p: Rep[BaseConverter[T, R]]) {
    def toData: Rep[BaseConverterData[T, R]] = {
      implicit val eT = p.convFun.elem.eDom;
implicit val eR = p.convFun.elem.eRange
      isoBaseConverter(eT, eR).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoBaseConverter[T, R](implicit eT: Elem[T], eR: Elem[R]): Iso[BaseConverterData[T, R], BaseConverter[T, R]] =
    reifyObject(new BaseConverterIso[T, R]()(eT, eR))

  case class PairConverterCtor[A1, A2, B1, B2]
      (override val conv1: Conv[A1, B1], override val conv2: Conv[A2, B2])
    extends PairConverter[A1, A2, B1, B2](conv1, conv2) with Def[PairConverter[A1, A2, B1, B2]] {
    implicit val eA1 = conv1.elem.typeArgs("T")._1.asElem[A1];
implicit val eA2 = conv2.elem.typeArgs("T")._1.asElem[A2];
implicit val eB1 = conv1.elem.typeArgs("R")._1.asElem[B1];
implicit val eB2 = conv2.elem.typeArgs("R")._1.asElem[B2]
    lazy val selfType = element[PairConverter[A1, A2, B1, B2]]
  }
  // elem for concrete class
  class PairConverterElem[A1, A2, B1, B2](val iso: Iso[PairConverterData[A1, A2, B1, B2], PairConverter[A1, A2, B1, B2]])(implicit val eA1: Elem[A1], val eA2: Elem[A2], val eB1: Elem[B1], val eB2: Elem[B2])
    extends ConverterElem[(A1, A2), (B1, B2), PairConverter[A1, A2, B1, B2]]
    with ConcreteElem[PairConverterData[A1, A2, B1, B2], PairConverter[A1, A2, B1, B2]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(pairElement(element[A1],element[A2]), pairElement(element[B1],element[B2])))
    override lazy val typeArgs = TypeArgs("A1" -> (eA1 -> scalan.util.Invariant), "A2" -> (eA2 -> scalan.util.Invariant), "B1" -> (eB1 -> scalan.util.Invariant), "B2" -> (eB2 -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[(A1, A2), (B1, B2)]]) = // Converter is not generated by meta
!!!("Cannot convert from Converter to PairConverter: missing fields List(conv1, conv2)")
    override def getDefaultRep = PairConverter(element[Converter[A1, B1]].defaultRepValue, element[Converter[A2, B2]].defaultRepValue)
    override lazy val tag = {
      implicit val tagA1 = eA1.tag
      implicit val tagA2 = eA2.tag
      implicit val tagB1 = eB1.tag
      implicit val tagB2 = eB2.tag
      weakTypeTag[PairConverter[A1, A2, B1, B2]]
    }
  }

  // state representation type
  type PairConverterData[A1, A2, B1, B2] = (Converter[A1, B1], Converter[A2, B2])

  // 3) Iso for concrete class
  class PairConverterIso[A1, A2, B1, B2](implicit eA1: Elem[A1], eA2: Elem[A2], eB1: Elem[B1], eB2: Elem[B2])
    extends EntityIso[PairConverterData[A1, A2, B1, B2], PairConverter[A1, A2, B1, B2]] with Def[PairConverterIso[A1, A2, B1, B2]] {
    override def from(p: Rep[PairConverter[A1, A2, B1, B2]]) =
      (p.conv1, p.conv2)
    override def to(p: Rep[(Converter[A1, B1], Converter[A2, B2])]) = {
      val Pair(conv1, conv2) = p
      PairConverter(conv1, conv2)
    }
    lazy val eFrom = pairElement(element[Converter[A1, B1]], element[Converter[A2, B2]])
    lazy val eTo = new PairConverterElem[A1, A2, B1, B2](self)
    lazy val selfType = new PairConverterIsoElem[A1, A2, B1, B2](eA1, eA2, eB1, eB2)
    def productArity = 4
    def productElement(n: Int) = n match {
      case 0 => eA1
      case 1 => eA2
      case 2 => eB1
      case 3 => eB2
    }
  }
  case class PairConverterIsoElem[A1, A2, B1, B2](eA1: Elem[A1], eA2: Elem[A2], eB1: Elem[B1], eB2: Elem[B2]) extends Elem[PairConverterIso[A1, A2, B1, B2]] {
    def getDefaultRep = reifyObject(new PairConverterIso[A1, A2, B1, B2]()(eA1, eA2, eB1, eB2))
    lazy val tag = {
      implicit val tagA1 = eA1.tag
      implicit val tagA2 = eA2.tag
      implicit val tagB1 = eB1.tag
      implicit val tagB2 = eB2.tag
      weakTypeTag[PairConverterIso[A1, A2, B1, B2]]
    }
    lazy val typeArgs = TypeArgs("A1" -> (eA1 -> scalan.util.Invariant), "A2" -> (eA2 -> scalan.util.Invariant), "B1" -> (eB1 -> scalan.util.Invariant), "B2" -> (eB2 -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class PairConverterCompanionCtor extends CompanionDef[PairConverterCompanionCtor] with PairConverterCompanion {
    def selfType = PairConverterCompanionElem
    override def toString = "PairConverterCompanion"
    @scalan.OverloadId("fromData")
    def apply[A1, A2, B1, B2](p: Rep[PairConverterData[A1, A2, B1, B2]]): Rep[PairConverter[A1, A2, B1, B2]] = {
      implicit val eA1 = p._1.elem.typeArgs("T")._1.asElem[A1];
implicit val eA2 = p._2.elem.typeArgs("T")._1.asElem[A2];
implicit val eB1 = p._1.elem.typeArgs("R")._1.asElem[B1];
implicit val eB2 = p._2.elem.typeArgs("R")._1.asElem[B2]
      isoPairConverter[A1, A2, B1, B2].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[A1, A2, B1, B2](conv1: Conv[A1, B1], conv2: Conv[A2, B2]): Rep[PairConverter[A1, A2, B1, B2]] =
      mkPairConverter(conv1, conv2)

    def unapply[A1, A2, B1, B2](p: Rep[Converter[(A1, A2), (B1, B2)]]) = unmkPairConverter(p)
  }
  lazy val PairConverterRep: Rep[PairConverterCompanionCtor] = new PairConverterCompanionCtor
  lazy val PairConverter: PairConverterCompanionCtor = proxyPairConverterCompanion(PairConverterRep)
  implicit def proxyPairConverterCompanion(p: Rep[PairConverterCompanionCtor]): PairConverterCompanionCtor = {
    proxyOps[PairConverterCompanionCtor](p)
  }

  implicit case object PairConverterCompanionElem extends CompanionElem[PairConverterCompanionCtor] {
    lazy val tag = weakTypeTag[PairConverterCompanionCtor]
    protected def getDefaultRep = PairConverterRep
  }

  implicit def proxyPairConverter[A1, A2, B1, B2](p: Rep[PairConverter[A1, A2, B1, B2]]): PairConverter[A1, A2, B1, B2] =
    proxyOps[PairConverter[A1, A2, B1, B2]](p)

  implicit class ExtendedPairConverter[A1, A2, B1, B2](p: Rep[PairConverter[A1, A2, B1, B2]]) {
    def toData: Rep[PairConverterData[A1, A2, B1, B2]] = {
      implicit val eA1 = p.conv1.elem.typeArgs("T")._1.asElem[A1];
implicit val eA2 = p.conv2.elem.typeArgs("T")._1.asElem[A2];
implicit val eB1 = p.conv1.elem.typeArgs("R")._1.asElem[B1];
implicit val eB2 = p.conv2.elem.typeArgs("R")._1.asElem[B2]
      isoPairConverter(eA1, eA2, eB1, eB2).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoPairConverter[A1, A2, B1, B2](implicit eA1: Elem[A1], eA2: Elem[A2], eB1: Elem[B1], eB2: Elem[B2]): Iso[PairConverterData[A1, A2, B1, B2], PairConverter[A1, A2, B1, B2]] =
    reifyObject(new PairConverterIso[A1, A2, B1, B2]()(eA1, eA2, eB1, eB2))

  case class SumConverterCtor[A1, A2, B1, B2]
      (override val conv1: Conv[A1, B1], override val conv2: Conv[A2, B2])
    extends SumConverter[A1, A2, B1, B2](conv1, conv2) with Def[SumConverter[A1, A2, B1, B2]] {
    implicit val eA1 = conv1.elem.typeArgs("T")._1.asElem[A1];
implicit val eA2 = conv2.elem.typeArgs("T")._1.asElem[A2];
implicit val eB1 = conv1.elem.typeArgs("R")._1.asElem[B1];
implicit val eB2 = conv2.elem.typeArgs("R")._1.asElem[B2]
    lazy val selfType = element[SumConverter[A1, A2, B1, B2]]
  }
  // elem for concrete class
  class SumConverterElem[A1, A2, B1, B2](val iso: Iso[SumConverterData[A1, A2, B1, B2], SumConverter[A1, A2, B1, B2]])(implicit val eA1: Elem[A1], val eA2: Elem[A2], val eB1: Elem[B1], val eB2: Elem[B2])
    extends ConverterElem[$bar[A1, A2], $bar[B1, B2], SumConverter[A1, A2, B1, B2]]
    with ConcreteElem[SumConverterData[A1, A2, B1, B2], SumConverter[A1, A2, B1, B2]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(sumElement(element[A1],element[A2]), sumElement(element[B1],element[B2])))
    override lazy val typeArgs = TypeArgs("A1" -> (eA1 -> scalan.util.Invariant), "A2" -> (eA2 -> scalan.util.Invariant), "B1" -> (eB1 -> scalan.util.Invariant), "B2" -> (eB2 -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[$bar[A1, A2], $bar[B1, B2]]]) = // Converter is not generated by meta
!!!("Cannot convert from Converter to SumConverter: missing fields List(conv1, conv2)")
    override def getDefaultRep = SumConverter(element[Converter[A1, B1]].defaultRepValue, element[Converter[A2, B2]].defaultRepValue)
    override lazy val tag = {
      implicit val tagA1 = eA1.tag
      implicit val tagA2 = eA2.tag
      implicit val tagB1 = eB1.tag
      implicit val tagB2 = eB2.tag
      weakTypeTag[SumConverter[A1, A2, B1, B2]]
    }
  }

  // state representation type
  type SumConverterData[A1, A2, B1, B2] = (Converter[A1, B1], Converter[A2, B2])

  // 3) Iso for concrete class
  class SumConverterIso[A1, A2, B1, B2](implicit eA1: Elem[A1], eA2: Elem[A2], eB1: Elem[B1], eB2: Elem[B2])
    extends EntityIso[SumConverterData[A1, A2, B1, B2], SumConverter[A1, A2, B1, B2]] with Def[SumConverterIso[A1, A2, B1, B2]] {
    override def from(p: Rep[SumConverter[A1, A2, B1, B2]]) =
      (p.conv1, p.conv2)
    override def to(p: Rep[(Converter[A1, B1], Converter[A2, B2])]) = {
      val Pair(conv1, conv2) = p
      SumConverter(conv1, conv2)
    }
    lazy val eFrom = pairElement(element[Converter[A1, B1]], element[Converter[A2, B2]])
    lazy val eTo = new SumConverterElem[A1, A2, B1, B2](self)
    lazy val selfType = new SumConverterIsoElem[A1, A2, B1, B2](eA1, eA2, eB1, eB2)
    def productArity = 4
    def productElement(n: Int) = n match {
      case 0 => eA1
      case 1 => eA2
      case 2 => eB1
      case 3 => eB2
    }
  }
  case class SumConverterIsoElem[A1, A2, B1, B2](eA1: Elem[A1], eA2: Elem[A2], eB1: Elem[B1], eB2: Elem[B2]) extends Elem[SumConverterIso[A1, A2, B1, B2]] {
    def getDefaultRep = reifyObject(new SumConverterIso[A1, A2, B1, B2]()(eA1, eA2, eB1, eB2))
    lazy val tag = {
      implicit val tagA1 = eA1.tag
      implicit val tagA2 = eA2.tag
      implicit val tagB1 = eB1.tag
      implicit val tagB2 = eB2.tag
      weakTypeTag[SumConverterIso[A1, A2, B1, B2]]
    }
    lazy val typeArgs = TypeArgs("A1" -> (eA1 -> scalan.util.Invariant), "A2" -> (eA2 -> scalan.util.Invariant), "B1" -> (eB1 -> scalan.util.Invariant), "B2" -> (eB2 -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class SumConverterCompanionCtor extends CompanionDef[SumConverterCompanionCtor] with SumConverterCompanion {
    def selfType = SumConverterCompanionElem
    override def toString = "SumConverterCompanion"
    @scalan.OverloadId("fromData")
    def apply[A1, A2, B1, B2](p: Rep[SumConverterData[A1, A2, B1, B2]]): Rep[SumConverter[A1, A2, B1, B2]] = {
      implicit val eA1 = p._1.elem.typeArgs("T")._1.asElem[A1];
implicit val eA2 = p._2.elem.typeArgs("T")._1.asElem[A2];
implicit val eB1 = p._1.elem.typeArgs("R")._1.asElem[B1];
implicit val eB2 = p._2.elem.typeArgs("R")._1.asElem[B2]
      isoSumConverter[A1, A2, B1, B2].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[A1, A2, B1, B2](conv1: Conv[A1, B1], conv2: Conv[A2, B2]): Rep[SumConverter[A1, A2, B1, B2]] =
      mkSumConverter(conv1, conv2)

    def unapply[A1, A2, B1, B2](p: Rep[Converter[$bar[A1, A2], $bar[B1, B2]]]) = unmkSumConverter(p)
  }
  lazy val SumConverterRep: Rep[SumConverterCompanionCtor] = new SumConverterCompanionCtor
  lazy val SumConverter: SumConverterCompanionCtor = proxySumConverterCompanion(SumConverterRep)
  implicit def proxySumConverterCompanion(p: Rep[SumConverterCompanionCtor]): SumConverterCompanionCtor = {
    proxyOps[SumConverterCompanionCtor](p)
  }

  implicit case object SumConverterCompanionElem extends CompanionElem[SumConverterCompanionCtor] {
    lazy val tag = weakTypeTag[SumConverterCompanionCtor]
    protected def getDefaultRep = SumConverterRep
  }

  implicit def proxySumConverter[A1, A2, B1, B2](p: Rep[SumConverter[A1, A2, B1, B2]]): SumConverter[A1, A2, B1, B2] =
    proxyOps[SumConverter[A1, A2, B1, B2]](p)

  implicit class ExtendedSumConverter[A1, A2, B1, B2](p: Rep[SumConverter[A1, A2, B1, B2]]) {
    def toData: Rep[SumConverterData[A1, A2, B1, B2]] = {
      implicit val eA1 = p.conv1.elem.typeArgs("T")._1.asElem[A1];
implicit val eA2 = p.conv2.elem.typeArgs("T")._1.asElem[A2];
implicit val eB1 = p.conv1.elem.typeArgs("R")._1.asElem[B1];
implicit val eB2 = p.conv2.elem.typeArgs("R")._1.asElem[B2]
      isoSumConverter(eA1, eA2, eB1, eB2).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoSumConverter[A1, A2, B1, B2](implicit eA1: Elem[A1], eA2: Elem[A2], eB1: Elem[B1], eB2: Elem[B2]): Iso[SumConverterData[A1, A2, B1, B2], SumConverter[A1, A2, B1, B2]] =
    reifyObject(new SumConverterIso[A1, A2, B1, B2]()(eA1, eA2, eB1, eB2))

  case class ComposeConverterCtor[A, B, C]
      (override val conv2: Conv[B, C], override val conv1: Conv[A, B])
    extends ComposeConverter[A, B, C](conv2, conv1) with Def[ComposeConverter[A, B, C]] {
    implicit val eA = conv1.elem.typeArgs("T")._1.asElem[A];
implicit val eB = conv2.elem.typeArgs("T")._1.asElem[B];
implicit val eC = conv2.elem.typeArgs("R")._1.asElem[C]
    lazy val selfType = element[ComposeConverter[A, B, C]]
  }
  // elem for concrete class
  class ComposeConverterElem[A, B, C](val iso: Iso[ComposeConverterData[A, B, C], ComposeConverter[A, B, C]])(implicit override val eT: Elem[A], val eB: Elem[B], override val eR: Elem[C])
    extends ConverterElem[A, C, ComposeConverter[A, B, C]]
    with ConcreteElem[ComposeConverterData[A, B, C], ComposeConverter[A, B, C]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(element[A], element[C]))
    override lazy val typeArgs = TypeArgs("A" -> (eT -> scalan.util.Invariant), "B" -> (eB -> scalan.util.Invariant), "C" -> (eR -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[A, C]]) = // Converter is not generated by meta
!!!("Cannot convert from Converter to ComposeConverter: missing fields List(conv2, conv1)")
    override def getDefaultRep = ComposeConverter(element[Converter[B, C]].defaultRepValue, element[Converter[A, B]].defaultRepValue)
    override lazy val tag = {
      implicit val tagA = eT.tag
      implicit val tagB = eB.tag
      implicit val tagC = eR.tag
      weakTypeTag[ComposeConverter[A, B, C]]
    }
  }

  // state representation type
  type ComposeConverterData[A, B, C] = (Converter[B, C], Converter[A, B])

  // 3) Iso for concrete class
  class ComposeConverterIso[A, B, C](implicit eT: Elem[A], eB: Elem[B], eR: Elem[C])
    extends EntityIso[ComposeConverterData[A, B, C], ComposeConverter[A, B, C]] with Def[ComposeConverterIso[A, B, C]] {
    override def from(p: Rep[ComposeConverter[A, B, C]]) =
      (p.conv2, p.conv1)
    override def to(p: Rep[(Converter[B, C], Converter[A, B])]) = {
      val Pair(conv2, conv1) = p
      ComposeConverter(conv2, conv1)
    }
    lazy val eFrom = pairElement(element[Converter[B, C]], element[Converter[A, B]])
    lazy val eTo = new ComposeConverterElem[A, B, C](self)
    lazy val selfType = new ComposeConverterIsoElem[A, B, C](eT, eB, eR)
    def productArity = 3
    def productElement(n: Int) = n match {
      case 0 => eT
      case 1 => eB
      case 2 => eR
    }
  }
  case class ComposeConverterIsoElem[A, B, C](eT: Elem[A], eB: Elem[B], eR: Elem[C]) extends Elem[ComposeConverterIso[A, B, C]] {
    def getDefaultRep = reifyObject(new ComposeConverterIso[A, B, C]()(eT, eB, eR))
    lazy val tag = {
      implicit val tagA = eT.tag
      implicit val tagB = eB.tag
      implicit val tagC = eR.tag
      weakTypeTag[ComposeConverterIso[A, B, C]]
    }
    lazy val typeArgs = TypeArgs("A" -> (eT -> scalan.util.Invariant), "B" -> (eB -> scalan.util.Invariant), "C" -> (eR -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class ComposeConverterCompanionCtor extends CompanionDef[ComposeConverterCompanionCtor] {
    def selfType = ComposeConverterCompanionElem
    override def toString = "ComposeConverterCompanion"
    @scalan.OverloadId("fromData")
    def apply[A, B, C](p: Rep[ComposeConverterData[A, B, C]]): Rep[ComposeConverter[A, B, C]] = {
      implicit val eA = p._2.elem.typeArgs("T")._1.asElem[A];
implicit val eB = p._1.elem.typeArgs("T")._1.asElem[B];
implicit val eC = p._1.elem.typeArgs("R")._1.asElem[C]
      isoComposeConverter[A, B, C].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[A, B, C](conv2: Conv[B, C], conv1: Conv[A, B]): Rep[ComposeConverter[A, B, C]] =
      mkComposeConverter(conv2, conv1)

    def unapply[A, B, C](p: Rep[Converter[A, C]]) = unmkComposeConverter(p)
  }
  lazy val ComposeConverterRep: Rep[ComposeConverterCompanionCtor] = new ComposeConverterCompanionCtor
  lazy val ComposeConverter: ComposeConverterCompanionCtor = proxyComposeConverterCompanion(ComposeConverterRep)
  implicit def proxyComposeConverterCompanion(p: Rep[ComposeConverterCompanionCtor]): ComposeConverterCompanionCtor = {
    proxyOps[ComposeConverterCompanionCtor](p)
  }

  implicit case object ComposeConverterCompanionElem extends CompanionElem[ComposeConverterCompanionCtor] {
    lazy val tag = weakTypeTag[ComposeConverterCompanionCtor]
    protected def getDefaultRep = ComposeConverterRep
  }

  implicit def proxyComposeConverter[A, B, C](p: Rep[ComposeConverter[A, B, C]]): ComposeConverter[A, B, C] =
    proxyOps[ComposeConverter[A, B, C]](p)

  implicit class ExtendedComposeConverter[A, B, C](p: Rep[ComposeConverter[A, B, C]]) {
    def toData: Rep[ComposeConverterData[A, B, C]] = {
      implicit val eA = p.conv1.elem.typeArgs("T")._1.asElem[A];
implicit val eB = p.conv2.elem.typeArgs("T")._1.asElem[B];
implicit val eC = p.conv2.elem.typeArgs("R")._1.asElem[C]
      isoComposeConverter(eA, eB, eC).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoComposeConverter[A, B, C](implicit eT: Elem[A], eB: Elem[B], eR: Elem[C]): Iso[ComposeConverterData[A, B, C], ComposeConverter[A, B, C]] =
    reifyObject(new ComposeConverterIso[A, B, C]()(eT, eB, eR))

  case class FunctorConverterCtor[A, B, F[_]]
      (override val itemConv: Conv[A, B])(implicit F: Functor[F])
    extends FunctorConverter[A, B, F](itemConv) with Def[FunctorConverter[A, B, F]] {
    implicit val eA = itemConv.elem.typeArgs("T")._1.asElem[A];
implicit val eB = itemConv.elem.typeArgs("R")._1.asElem[B]
    lazy val selfType = element[FunctorConverter[A, B, F]]
  }
  // elem for concrete class
  class FunctorConverterElem[A, B, F[_]](val iso: Iso[FunctorConverterData[A, B, F], FunctorConverter[A, B, F]])(implicit val F: Functor[F], val eA: Elem[A], val eB: Elem[B])
    extends ConverterElem[F[A], F[B], FunctorConverter[A, B, F]]
    with ConcreteElem[FunctorConverterData[A, B, F], FunctorConverter[A, B, F]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(element[F[A]], element[F[B]]))
    override lazy val typeArgs = TypeArgs("A" -> (eA -> scalan.util.Invariant), "B" -> (eB -> scalan.util.Invariant), "F" -> (F -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[F[A], F[B]]]) = // Converter is not generated by meta
!!!("Cannot convert from Converter to FunctorConverter: missing fields List(itemConv)")
    override def getDefaultRep = FunctorConverter(element[Converter[A, B]].defaultRepValue)
    override lazy val tag = {
      implicit val tagA = eA.tag
      implicit val tagB = eB.tag
      weakTypeTag[FunctorConverter[A, B, F]]
    }
  }

  // state representation type
  type FunctorConverterData[A, B, F[_]] = Converter[A, B]

  // 3) Iso for concrete class
  class FunctorConverterIso[A, B, F[_]](implicit F: Functor[F], eA: Elem[A], eB: Elem[B])
    extends EntityIso[FunctorConverterData[A, B, F], FunctorConverter[A, B, F]] with Def[FunctorConverterIso[A, B, F]] {
    override def from(p: Rep[FunctorConverter[A, B, F]]) =
      p.itemConv
    override def to(p: Rep[Converter[A, B]]) = {
      val itemConv = p
      FunctorConverter(itemConv)
    }
    lazy val eFrom = element[Converter[A, B]]
    lazy val eTo = new FunctorConverterElem[A, B, F](self)
    lazy val selfType = new FunctorConverterIsoElem[A, B, F](F, eA, eB)
    def productArity = 3
    def productElement(n: Int) = n match {
      case 0 => F
      case 1 => eA
      case 2 => eB
    }
  }
  case class FunctorConverterIsoElem[A, B, F[_]](F: Functor[F], eA: Elem[A], eB: Elem[B]) extends Elem[FunctorConverterIso[A, B, F]] {
    def getDefaultRep = reifyObject(new FunctorConverterIso[A, B, F]()(F, eA, eB))
    lazy val tag = {
      implicit val tagA = eA.tag
      implicit val tagB = eB.tag
      weakTypeTag[FunctorConverterIso[A, B, F]]
    }
    lazy val typeArgs = TypeArgs("A" -> (eA -> scalan.util.Invariant), "B" -> (eB -> scalan.util.Invariant), "F" -> (F -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class FunctorConverterCompanionCtor extends CompanionDef[FunctorConverterCompanionCtor] with FunctorConverterCompanion {
    def selfType = FunctorConverterCompanionElem
    override def toString = "FunctorConverterCompanion"

    @scalan.OverloadId("fromFields")
    def apply[A, B, F[_]](itemConv: Conv[A, B])(implicit F: Functor[F]): Rep[FunctorConverter[A, B, F]] =
      mkFunctorConverter(itemConv)

    def unapply[A, B, F[_]](p: Rep[Converter[F[A], F[B]]]) = unmkFunctorConverter(p)
  }
  lazy val FunctorConverterRep: Rep[FunctorConverterCompanionCtor] = new FunctorConverterCompanionCtor
  lazy val FunctorConverter: FunctorConverterCompanionCtor = proxyFunctorConverterCompanion(FunctorConverterRep)
  implicit def proxyFunctorConverterCompanion(p: Rep[FunctorConverterCompanionCtor]): FunctorConverterCompanionCtor = {
    proxyOps[FunctorConverterCompanionCtor](p)
  }

  implicit case object FunctorConverterCompanionElem extends CompanionElem[FunctorConverterCompanionCtor] {
    lazy val tag = weakTypeTag[FunctorConverterCompanionCtor]
    protected def getDefaultRep = FunctorConverterRep
  }

  implicit def proxyFunctorConverter[A, B, F[_]](p: Rep[FunctorConverter[A, B, F]]): FunctorConverter[A, B, F] =
    proxyOps[FunctorConverter[A, B, F]](p)

  implicit class ExtendedFunctorConverter[A, B, F[_]](p: Rep[FunctorConverter[A, B, F]])(implicit F: Functor[F]) {
    def toData: Rep[FunctorConverterData[A, B, F]] = {
      implicit val eA = p.itemConv.elem.typeArgs("T")._1.asElem[A];
implicit val eB = p.itemConv.elem.typeArgs("R")._1.asElem[B]
      isoFunctorConverter(F, eA, eB).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoFunctorConverter[A, B, F[_]](implicit F: Functor[F], eA: Elem[A], eB: Elem[B]): Iso[FunctorConverterData[A, B, F], FunctorConverter[A, B, F]] =
    reifyObject(new FunctorConverterIso[A, B, F]()(F, eA, eB))

  case class NaturalConverterCtor[A, F[_], G[_]]
      (override val convFun: Rep[F[A] => G[A]])(implicit eA: Elem[A], cF: Cont[F], cG: Cont[G])
    extends NaturalConverter[A, F, G](convFun) with Def[NaturalConverter[A, F, G]] {
    lazy val selfType = element[NaturalConverter[A, F, G]]
  }
  // elem for concrete class
  class NaturalConverterElem[A, F[_], G[_]](val iso: Iso[NaturalConverterData[A, F, G], NaturalConverter[A, F, G]])(implicit val eA: Elem[A], val cF: Cont[F], val cG: Cont[G])
    extends ConverterElem[F[A], G[A], NaturalConverter[A, F, G]]
    with ConcreteElem[NaturalConverterData[A, F, G], NaturalConverter[A, F, G]] {
    override lazy val parent: Option[Elem[_]] = Some(converterElement(element[F[A]], element[G[A]]))
    override lazy val typeArgs = TypeArgs("A" -> (eA -> scalan.util.Invariant), "F" -> (cF -> scalan.util.Invariant), "G" -> (cG -> scalan.util.Invariant))

    override def convertConverter(x: Rep[Converter[F[A], G[A]]]) = NaturalConverter(x.convFun)
    override def getDefaultRep = NaturalConverter(constFun[F[A], G[A]](element[G[A]].defaultRepValue))
    override lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[NaturalConverter[A, F, G]]
    }
  }

  // state representation type
  type NaturalConverterData[A, F[_], G[_]] = F[A] => G[A]

  // 3) Iso for concrete class
  class NaturalConverterIso[A, F[_], G[_]](implicit eA: Elem[A], cF: Cont[F], cG: Cont[G])
    extends EntityIso[NaturalConverterData[A, F, G], NaturalConverter[A, F, G]] with Def[NaturalConverterIso[A, F, G]] {
    override def from(p: Rep[NaturalConverter[A, F, G]]) =
      p.convFun
    override def to(p: Rep[F[A] => G[A]]) = {
      val convFun = p
      NaturalConverter(convFun)
    }
    lazy val eFrom = element[F[A] => G[A]]
    lazy val eTo = new NaturalConverterElem[A, F, G](self)
    lazy val selfType = new NaturalConverterIsoElem[A, F, G](eA, cF, cG)
    def productArity = 3
    def productElement(n: Int) = n match {
      case 0 => eA
      case 1 => cF
      case 2 => cG
    }
  }
  case class NaturalConverterIsoElem[A, F[_], G[_]](eA: Elem[A], cF: Cont[F], cG: Cont[G]) extends Elem[NaturalConverterIso[A, F, G]] {
    def getDefaultRep = reifyObject(new NaturalConverterIso[A, F, G]()(eA, cF, cG))
    lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[NaturalConverterIso[A, F, G]]
    }
    lazy val typeArgs = TypeArgs("A" -> (eA -> scalan.util.Invariant), "F" -> (cF -> scalan.util.Invariant), "G" -> (cG -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class NaturalConverterCompanionCtor extends CompanionDef[NaturalConverterCompanionCtor] {
    def selfType = NaturalConverterCompanionElem
    override def toString = "NaturalConverterCompanion"

    @scalan.OverloadId("fromFields")
    def apply[A, F[_], G[_]](convFun: Rep[F[A] => G[A]])(implicit eA: Elem[A], cF: Cont[F], cG: Cont[G]): Rep[NaturalConverter[A, F, G]] =
      mkNaturalConverter(convFun)

    def unapply[A, F[_], G[_]](p: Rep[Converter[F[A], G[A]]]) = unmkNaturalConverter(p)
  }
  lazy val NaturalConverterRep: Rep[NaturalConverterCompanionCtor] = new NaturalConverterCompanionCtor
  lazy val NaturalConverter: NaturalConverterCompanionCtor = proxyNaturalConverterCompanion(NaturalConverterRep)
  implicit def proxyNaturalConverterCompanion(p: Rep[NaturalConverterCompanionCtor]): NaturalConverterCompanionCtor = {
    proxyOps[NaturalConverterCompanionCtor](p)
  }

  implicit case object NaturalConverterCompanionElem extends CompanionElem[NaturalConverterCompanionCtor] {
    lazy val tag = weakTypeTag[NaturalConverterCompanionCtor]
    protected def getDefaultRep = NaturalConverterRep
  }

  implicit def proxyNaturalConverter[A, F[_], G[_]](p: Rep[NaturalConverter[A, F, G]]): NaturalConverter[A, F, G] =
    proxyOps[NaturalConverter[A, F, G]](p)

  implicit class ExtendedNaturalConverter[A, F[_], G[_]](p: Rep[NaturalConverter[A, F, G]])(implicit eA: Elem[A], cF: Cont[F], cG: Cont[G]) {
    def toData: Rep[NaturalConverterData[A, F, G]] = {
      isoNaturalConverter(eA, cF, cG).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoNaturalConverter[A, F[_], G[_]](implicit eA: Elem[A], cF: Cont[F], cG: Cont[G]): Iso[NaturalConverterData[A, F, G], NaturalConverter[A, F, G]] =
    reifyObject(new NaturalConverterIso[A, F, G]()(eA, cF, cG))

  registerModule(ConvertersModule)

  lazy val Converter: Rep[ConverterCompanionCtor] = new ConverterCompanionCtor {
  }

  object IdentityConvMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[IdentityConv[A]], Rep[A]) forSome {type A}] = d match {
        case MethodCall(receiver, method, Seq(x, _*), _) if receiver.elem.isInstanceOf[IdentityConvElem[_]] && method.getName == "apply" =>
          Some((receiver, x)).asInstanceOf[Option[(Rep[IdentityConv[A]], Rep[A]) forSome {type A}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[IdentityConv[A]], Rep[A]) forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object isIdentity {
      def unapply(d: Def[_]): Option[Rep[IdentityConv[A]] forSome {type A}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[IdentityConvElem[_]] && method.getName == "isIdentity" =>
          Some(receiver).asInstanceOf[Option[Rep[IdentityConv[A]] forSome {type A}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[IdentityConv[A]] forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    // WARNING: Cannot generate matcher for method `equals`: Overrides Object method
  }

  def mkIdentityConv[A]
    ()(implicit eT: Elem[A]): Rep[IdentityConv[A]] = {
    new IdentityConvCtor[A]()
  }
  def unmkIdentityConv[A](p: Rep[Converter[A, A]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: IdentityConvElem[A] @unchecked =>
      Some(())
    case _ =>
      None
  }

  object BaseConverterMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[BaseConverter[T, R]], Rep[T]) forSome {type T; type R}] = d match {
        case MethodCall(receiver, method, Seq(x, _*), _) if receiver.elem.isInstanceOf[BaseConverterElem[_, _]] && method.getName == "apply" =>
          Some((receiver, x)).asInstanceOf[Option[(Rep[BaseConverter[T, R]], Rep[T]) forSome {type T; type R}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[BaseConverter[T, R]], Rep[T]) forSome {type T; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    // WARNING: Cannot generate matcher for method `equals`: Overrides Object method
  }

  object BaseConverterCompanionMethods {
  }

  def mkBaseConverter[T, R]
    (convFun: Rep[T => R]): Rep[BaseConverter[T, R]] = {
    new BaseConverterCtor[T, R](convFun)
  }
  def unmkBaseConverter[T, R](p: Rep[Converter[T, R]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: BaseConverterElem[T, R] @unchecked =>
      Some((p.asRep[BaseConverter[T, R]].convFun))
    case _ =>
      None
  }

  object PairConverterMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[PairConverter[A1, A2, B1, B2]], Rep[(A1, A2)]) forSome {type A1; type A2; type B1; type B2}] = d match {
        case MethodCall(receiver, method, Seq(x, _*), _) if receiver.elem.isInstanceOf[PairConverterElem[_, _, _, _]] && method.getName == "apply" =>
          Some((receiver, x)).asInstanceOf[Option[(Rep[PairConverter[A1, A2, B1, B2]], Rep[(A1, A2)]) forSome {type A1; type A2; type B1; type B2}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[PairConverter[A1, A2, B1, B2]], Rep[(A1, A2)]) forSome {type A1; type A2; type B1; type B2}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object isIdentity {
      def unapply(d: Def[_]): Option[Rep[PairConverter[A1, A2, B1, B2]] forSome {type A1; type A2; type B1; type B2}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[PairConverterElem[_, _, _, _]] && method.getName == "isIdentity" =>
          Some(receiver).asInstanceOf[Option[Rep[PairConverter[A1, A2, B1, B2]] forSome {type A1; type A2; type B1; type B2}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[PairConverter[A1, A2, B1, B2]] forSome {type A1; type A2; type B1; type B2}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }
  }

  object PairConverterCompanionMethods {
  }

  def mkPairConverter[A1, A2, B1, B2]
    (conv1: Conv[A1, B1], conv2: Conv[A2, B2]): Rep[PairConverter[A1, A2, B1, B2]] = {
    new PairConverterCtor[A1, A2, B1, B2](conv1, conv2)
  }
  def unmkPairConverter[A1, A2, B1, B2](p: Rep[Converter[(A1, A2), (B1, B2)]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: PairConverterElem[A1, A2, B1, B2] @unchecked =>
      Some((p.asRep[PairConverter[A1, A2, B1, B2]].conv1, p.asRep[PairConverter[A1, A2, B1, B2]].conv2))
    case _ =>
      None
  }

  object SumConverterMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[SumConverter[A1, A2, B1, B2]], Rep[$bar[A1, A2]]) forSome {type A1; type A2; type B1; type B2}] = d match {
        case MethodCall(receiver, method, Seq(x, _*), _) if receiver.elem.isInstanceOf[SumConverterElem[_, _, _, _]] && method.getName == "apply" =>
          Some((receiver, x)).asInstanceOf[Option[(Rep[SumConverter[A1, A2, B1, B2]], Rep[$bar[A1, A2]]) forSome {type A1; type A2; type B1; type B2}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[SumConverter[A1, A2, B1, B2]], Rep[$bar[A1, A2]]) forSome {type A1; type A2; type B1; type B2}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object isIdentity {
      def unapply(d: Def[_]): Option[Rep[SumConverter[A1, A2, B1, B2]] forSome {type A1; type A2; type B1; type B2}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[SumConverterElem[_, _, _, _]] && method.getName == "isIdentity" =>
          Some(receiver).asInstanceOf[Option[Rep[SumConverter[A1, A2, B1, B2]] forSome {type A1; type A2; type B1; type B2}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[SumConverter[A1, A2, B1, B2]] forSome {type A1; type A2; type B1; type B2}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }
  }

  object SumConverterCompanionMethods {
  }

  def mkSumConverter[A1, A2, B1, B2]
    (conv1: Conv[A1, B1], conv2: Conv[A2, B2]): Rep[SumConverter[A1, A2, B1, B2]] = {
    new SumConverterCtor[A1, A2, B1, B2](conv1, conv2)
  }
  def unmkSumConverter[A1, A2, B1, B2](p: Rep[Converter[$bar[A1, A2], $bar[B1, B2]]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: SumConverterElem[A1, A2, B1, B2] @unchecked =>
      Some((p.asRep[SumConverter[A1, A2, B1, B2]].conv1, p.asRep[SumConverter[A1, A2, B1, B2]].conv2))
    case _ =>
      None
  }

  object ComposeConverterMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[ComposeConverter[A, B, C]], Rep[A]) forSome {type A; type B; type C}] = d match {
        case MethodCall(receiver, method, Seq(a, _*), _) if receiver.elem.isInstanceOf[ComposeConverterElem[_, _, _]] && method.getName == "apply" =>
          Some((receiver, a)).asInstanceOf[Option[(Rep[ComposeConverter[A, B, C]], Rep[A]) forSome {type A; type B; type C}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[ComposeConverter[A, B, C]], Rep[A]) forSome {type A; type B; type C}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object isIdentity {
      def unapply(d: Def[_]): Option[Rep[ComposeConverter[A, B, C]] forSome {type A; type B; type C}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[ComposeConverterElem[_, _, _]] && method.getName == "isIdentity" =>
          Some(receiver).asInstanceOf[Option[Rep[ComposeConverter[A, B, C]] forSome {type A; type B; type C}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[ComposeConverter[A, B, C]] forSome {type A; type B; type C}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    // WARNING: Cannot generate matcher for method `equals`: Overrides Object method
  }

  def mkComposeConverter[A, B, C]
    (conv2: Conv[B, C], conv1: Conv[A, B]): Rep[ComposeConverter[A, B, C]] = {
    new ComposeConverterCtor[A, B, C](conv2, conv1)
  }
  def unmkComposeConverter[A, B, C](p: Rep[Converter[A, C]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: ComposeConverterElem[A, B, C] @unchecked =>
      Some((p.asRep[ComposeConverter[A, B, C]].conv2, p.asRep[ComposeConverter[A, B, C]].conv1))
    case _ =>
      None
  }

  object FunctorConverterMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[FunctorConverter[A, B, F]], Rep[F[A]]) forSome {type A; type B; type F[_]}] = d match {
        case MethodCall(receiver, method, Seq(xs, _*), _) if (receiver.elem.asInstanceOf[Elem[_]] match { case _: FunctorConverterElem[_, _, _] => true; case _ => false }) && method.getName == "apply" =>
          Some((receiver, xs)).asInstanceOf[Option[(Rep[FunctorConverter[A, B, F]], Rep[F[A]]) forSome {type A; type B; type F[_]}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[FunctorConverter[A, B, F]], Rep[F[A]]) forSome {type A; type B; type F[_]}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object isIdentity {
      def unapply(d: Def[_]): Option[Rep[FunctorConverter[A, B, F]] forSome {type A; type B; type F[_]}] = d match {
        case MethodCall(receiver, method, _, _) if (receiver.elem.asInstanceOf[Elem[_]] match { case _: FunctorConverterElem[_, _, _] => true; case _ => false }) && method.getName == "isIdentity" =>
          Some(receiver).asInstanceOf[Option[Rep[FunctorConverter[A, B, F]] forSome {type A; type B; type F[_]}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[FunctorConverter[A, B, F]] forSome {type A; type B; type F[_]}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    // WARNING: Cannot generate matcher for method `equals`: Overrides Object method
  }

  object FunctorConverterCompanionMethods {
  }

  def mkFunctorConverter[A, B, F[_]]
    (itemConv: Conv[A, B])(implicit F: Functor[F], cF: Cont[F]): Rep[FunctorConverter[A, B, F]] = {
    new FunctorConverterCtor[A, B, F](itemConv)
  }
  def unmkFunctorConverter[A, B, F[_]](p: Rep[Converter[F[A], F[B]]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: FunctorConverterElem[A, B, F] @unchecked =>
      Some((p.asRep[FunctorConverter[A, B, F]].itemConv))
    case _ =>
      None
  }

  object NaturalConverterMethods {
    object apply {
      def unapply(d: Def[_]): Option[(Rep[NaturalConverter[A, F, G]], Rep[F[A]]) forSome {type A; type F[_]; type G[_]}] = d match {
        case MethodCall(receiver, method, Seq(xs, _*), _) if (receiver.elem.asInstanceOf[Elem[_]] match { case _: NaturalConverterElem[_, _, _] => true; case _ => false }) && method.getName == "apply" =>
          Some((receiver, xs)).asInstanceOf[Option[(Rep[NaturalConverter[A, F, G]], Rep[F[A]]) forSome {type A; type F[_]; type G[_]}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[NaturalConverter[A, F, G]], Rep[F[A]]) forSome {type A; type F[_]; type G[_]}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    // WARNING: Cannot generate matcher for method `equals`: Overrides Object method
  }

  def mkNaturalConverter[A, F[_], G[_]]
    (convFun: Rep[F[A] => G[A]])(implicit eA: Elem[A], cF: Cont[F], cG: Cont[G]): Rep[NaturalConverter[A, F, G]] = {
    new NaturalConverterCtor[A, F, G](convFun)
  }
  def unmkNaturalConverter[A, F[_], G[_]](p: Rep[Converter[F[A], G[A]]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: NaturalConverterElem[A, F, G] @unchecked =>
      Some((p.asRep[NaturalConverter[A, F, G]].convFun))
    case _ =>
      None
  }

  object ConverterMethods {
    object convFun {
      def unapply(d: Def[_]): Option[Rep[Converter[T, R]] forSome {type T; type R}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[ConverterElem[_, _, _]] && method.getName == "convFun" =>
          Some(receiver).asInstanceOf[Option[Rep[Converter[T, R]] forSome {type T; type R}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[Converter[T, R]] forSome {type T; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object apply {
      def unapply(d: Def[_]): Option[(Rep[Converter[T, R]], Rep[T]) forSome {type T; type R}] = d match {
        case MethodCall(receiver, method, Seq(x, _*), _) if receiver.elem.isInstanceOf[ConverterElem[_, _, _]] && method.getName == "apply" =>
          Some((receiver, x)).asInstanceOf[Option[(Rep[Converter[T, R]], Rep[T]) forSome {type T; type R}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[(Rep[Converter[T, R]], Rep[T]) forSome {type T; type R}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    // WARNING: Cannot generate matcher for method `isIdentity`: Method's return type Boolean is not a Rep

    // WARNING: Cannot generate matcher for method `toString`: Overrides Object method
  }

  object ConverterCompanionMethods {
  }
}

object ConvertersModule extends scalan.ModuleInfo {
  val dump = "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"
}
}

