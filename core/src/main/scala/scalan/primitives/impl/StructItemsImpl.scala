package scalan.primitives

import scala.annotation.unchecked.uncheckedVariance
import scalan._
import scala.reflect.runtime.universe._
import OverloadHack.{Overloaded2, Overloaded1}
import scala.reflect.runtime.universe.{WeakTypeTag, weakTypeTag}
import scalan.meta.ScalanAst._

package impl {
// Abs -----------------------------------
trait StructItemsDefs extends StructItems {
  self: Structs with Scalan =>
import IsoUR._
import Converter._
import StructKey._
import StructItem._
import StructItemBase._

object StructItem extends EntityObject("StructItem") {
  // entityProxy: single proxy for each type family
  implicit def proxyStructItem[Val, Schema <: Struct](p: Rep[StructItem[Val, Schema]]): StructItem[Val, Schema] = {
    if (p.rhs.isInstanceOf[StructItem[Val, Schema]@unchecked]) p.rhs.asInstanceOf[StructItem[Val, Schema]]
    else
      proxyOps[StructItem[Val, Schema]](p)(scala.reflect.classTag[StructItem[Val, Schema]])
  }

  // familyElem
  class StructItemElem[Val, Schema <: Struct, To <: StructItem[Val, Schema]](implicit _eVal: Elem[Val], _eSchema: Elem[Schema])
    extends EntityElem[To] {
    def eVal = _eVal
    def eSchema = _eSchema

    lazy val parent: Option[Elem[_]] = None
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("Val" -> (eVal -> scalan.util.Covariant), "Schema" -> (eSchema -> scalan.util.Invariant))
    override lazy val tag = {
      implicit val tagVal = eVal.tag
      implicit val tagSchema = eSchema.tag
      weakTypeTag[StructItem[Val, Schema]].asInstanceOf[WeakTypeTag[To]]
    }
    override def convert(x: Rep[Def[_]]) = {
      val conv = fun {x: Rep[StructItem[Val, Schema]] => convertStructItem(x) }
      tryConvert(element[StructItem[Val, Schema]], this, x, conv)
    }

    def convertStructItem(x: Rep[StructItem[Val, Schema]]): Rep[To] = {
      x.elem match {
        case _: StructItemElem[_, _, _] => asRep[To](x)
        case e => !!!(s"Expected $x to have StructItemElem[_, _, _], but got $e", x)
      }
    }
    override def getDefaultRep: Rep[To] = ???
  }

  implicit def structItemElement[Val, Schema <: Struct](implicit eVal: Elem[Val], eSchema: Elem[Schema]): Elem[StructItem[Val, Schema]] =
    cachedElem[StructItemElem[Val, Schema, StructItem[Val, Schema]]](eVal, eSchema)

  implicit case object StructItemCompanionElem extends CompanionElem[StructItemCompanionCtor] {
    lazy val tag = weakTypeTag[StructItemCompanionCtor]
    protected def getDefaultRep = RStructItem
  }

  abstract class StructItemCompanionCtor extends CompanionDef[StructItemCompanionCtor] {
    def selfType = StructItemCompanionElem
    override def toString = "StructItem"
  }
  implicit def proxyStructItemCompanionCtor(p: Rep[StructItemCompanionCtor]): StructItemCompanionCtor =
    proxyOps[StructItemCompanionCtor](p)

  lazy val RStructItem: Rep[StructItemCompanionCtor] = new StructItemCompanionCtor {
  }

  object StructItemMethods {
    object key {
      def unapply(d: Def[_]): Nullable[Rep[StructItem[Val, Schema]] forSome {type Val; type Schema <: Struct}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[StructItemElem[_, _, _]] && method.getName == "key" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[StructItem[Val, Schema]] forSome {type Val; type Schema <: Struct}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[StructItem[Val, Schema]] forSome {type Val; type Schema <: Struct}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }

    object value {
      def unapply(d: Def[_]): Nullable[Rep[StructItem[Val, Schema]] forSome {type Val; type Schema <: Struct}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[StructItemElem[_, _, _]] && method.getName == "value" =>
          val res = receiver
          Nullable(res).asInstanceOf[Nullable[Rep[StructItem[Val, Schema]] forSome {type Val; type Schema <: Struct}]]
        case _ => Nullable.None
      }
      def unapply(exp: Sym): Nullable[Rep[StructItem[Val, Schema]] forSome {type Val; type Schema <: Struct}] = exp match {
        case Def(d) => unapply(d)
        case _ => Nullable.None
      }
    }
  }
} // of object StructItem
  registerEntityObject("StructItem", StructItem)

object StructItemBase extends EntityObject("StructItemBase") {
  case class StructItemBaseCtor[Val, Schema <: Struct]
      (override val key: Rep[StructKey[Schema]], override val value: Rep[Val])
    extends StructItemBase[Val, Schema](key, value) with Def[StructItemBase[Val, Schema]] {
    implicit lazy val eVal = value.elem;
implicit lazy val eSchema = key.eSchema

    lazy val selfType = element[StructItemBase[Val, Schema]]
  }
  // elem for concrete class
  class StructItemBaseElem[Val, Schema <: Struct](val iso: Iso[StructItemBaseData[Val, Schema], StructItemBase[Val, Schema]])(implicit override val eVal: Elem[Val], override val eSchema: Elem[Schema])
    extends StructItemElem[Val, Schema, StructItemBase[Val, Schema]]
    with ConcreteElem[StructItemBaseData[Val, Schema], StructItemBase[Val, Schema]] {
    override lazy val parent: Option[Elem[_]] = Some(structItemElement(element[Val], element[Schema]))
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("Val" -> (eVal -> scalan.util.Invariant), "Schema" -> (eSchema -> scalan.util.Invariant))
    override def convertStructItem(x: Rep[StructItem[Val, Schema]]) = RStructItemBase(x.key, x.value)
    override def getDefaultRep = RStructItemBase(element[StructKey[Schema]].defaultRepValue, element[Val].defaultRepValue)
    override lazy val tag = {
      implicit val tagVal = eVal.tag
      implicit val tagSchema = eSchema.tag
      weakTypeTag[StructItemBase[Val, Schema]]
    }
  }

  // state representation type
  type StructItemBaseData[Val, Schema <: Struct] = (StructKey[Schema], Val)

  // 3) Iso for concrete class
  class StructItemBaseIso[Val, Schema <: Struct](implicit eVal: Elem[Val], eSchema: Elem[Schema])
    extends EntityIso[StructItemBaseData[Val, Schema], StructItemBase[Val, Schema]] with Def[StructItemBaseIso[Val, Schema]] {
    private lazy val _safeFrom = fun { p: Rep[StructItemBase[Val, Schema]] => (p.key, p.value) }
    override def from(p: Rep[StructItemBase[Val, Schema]]) =
      tryConvert[StructItemBase[Val, Schema], (StructKey[Schema], Val)](eTo, eFrom, p, _safeFrom)
    override def to(p: Rep[(StructKey[Schema], Val)]) = {
      val Pair(key, value) = p
      RStructItemBase(key, value)
    }
    lazy val eFrom = pairElement(element[StructKey[Schema]], element[Val])
    lazy val eTo = new StructItemBaseElem[Val, Schema](self)
    lazy val selfType = new StructItemBaseIsoElem[Val, Schema](eVal, eSchema)
    def productArity = 2
    def productElement(n: Int) = n match {
      case 0 => eVal
      case 1 => eSchema
    }
  }
  case class StructItemBaseIsoElem[Val, Schema <: Struct](eVal: Elem[Val], eSchema: Elem[Schema]) extends Elem[StructItemBaseIso[Val, Schema]] {
    def getDefaultRep = reifyObject(new StructItemBaseIso[Val, Schema]()(eVal, eSchema))
    lazy val tag = {
      implicit val tagVal = eVal.tag
      implicit val tagSchema = eSchema.tag
      weakTypeTag[StructItemBaseIso[Val, Schema]]
    }
    override def buildTypeArgs = super.buildTypeArgs ++ TypeArgs("Val" -> (eVal -> scalan.util.Invariant), "Schema" -> (eSchema -> scalan.util.Invariant))
  }
  // 4) constructor and deconstructor
  class StructItemBaseCompanionCtor extends CompanionDef[StructItemBaseCompanionCtor] {
    def selfType = StructItemBaseCompanionElem
    override def toString = "StructItemBaseCompanion"
    @scalan.OverloadId("fromData")
    def apply[Val, Schema <: Struct](p: Rep[StructItemBaseData[Val, Schema]]): Rep[StructItemBase[Val, Schema]] = {
      implicit val eVal = p._2.elem;
implicit val eSchema = p._1.eSchema
      isoStructItemBase[Val, Schema].to(p)
    }

    @scalan.OverloadId("fromFields")
    def apply[Val, Schema <: Struct](key: Rep[StructKey[Schema]], value: Rep[Val]): Rep[StructItemBase[Val, Schema]] =
      mkStructItemBase(key, value)

    def unapply[Val, Schema <: Struct](p: Rep[StructItem[Val, Schema]]) = unmkStructItemBase(p)
  }
  lazy val StructItemBaseRep: Rep[StructItemBaseCompanionCtor] = new StructItemBaseCompanionCtor
  lazy val RStructItemBase: StructItemBaseCompanionCtor = proxyStructItemBaseCompanion(StructItemBaseRep)
  implicit def proxyStructItemBaseCompanion(p: Rep[StructItemBaseCompanionCtor]): StructItemBaseCompanionCtor = {
    if (p.rhs.isInstanceOf[StructItemBaseCompanionCtor])
      p.rhs.asInstanceOf[StructItemBaseCompanionCtor]
    else
      proxyOps[StructItemBaseCompanionCtor](p)
  }

  implicit case object StructItemBaseCompanionElem extends CompanionElem[StructItemBaseCompanionCtor] {
    lazy val tag = weakTypeTag[StructItemBaseCompanionCtor]
    protected def getDefaultRep = StructItemBaseRep
  }

  implicit def proxyStructItemBase[Val, Schema <: Struct](p: Rep[StructItemBase[Val, Schema]]): StructItemBase[Val, Schema] =
    proxyOps[StructItemBase[Val, Schema]](p)

  implicit class ExtendedStructItemBase[Val, Schema <: Struct](p: Rep[StructItemBase[Val, Schema]]) {
    def toData: Rep[StructItemBaseData[Val, Schema]] = {
      implicit val eVal = p.value.elem;
implicit val eSchema = p.key.eSchema
      isoStructItemBase(eVal, eSchema).from(p)
    }
  }

  // 5) implicit resolution of Iso
  implicit def isoStructItemBase[Val, Schema <: Struct](implicit eVal: Elem[Val], eSchema: Elem[Schema]): Iso[StructItemBaseData[Val, Schema], StructItemBase[Val, Schema]] =
    reifyObject(new StructItemBaseIso[Val, Schema]()(eVal, eSchema))

  def mkStructItemBase[Val, Schema <: Struct]
    (key: Rep[StructKey[Schema]], value: Rep[Val]): Rep[StructItemBase[Val, Schema]] = {
    new StructItemBaseCtor[Val, Schema](key, value)
  }
  def unmkStructItemBase[Val, Schema <: Struct](p: Rep[StructItem[Val, Schema]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: StructItemBaseElem[Val, Schema] @unchecked =>
      Some((asRep[StructItemBase[Val, Schema]](p).key, asRep[StructItemBase[Val, Schema]](p).value))
    case _ =>
      None
  }

    object StructItemBaseMethods {
  }
} // of object StructItemBase
  registerEntityObject("StructItemBase", StructItemBase)

  registerModule(StructItemsModule)
}

object StructItemsModule extends scalan.ModuleInfo("scalan.primitives", "StructItems")
}

