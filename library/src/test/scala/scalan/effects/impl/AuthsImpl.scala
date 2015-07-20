package scalan.examples
package impl

import scala.reflect.runtime.universe._
import scalan._
import scalan.monads._
import scala.reflect.runtime.universe.{WeakTypeTag, weakTypeTag}

// Abs -----------------------------------
trait AuthenticationsAbs extends Authentications with scalan.Scalan {
  self: AuthenticationsDsl =>

  // single proxy for each type family
  implicit def proxyAuth[A](p: Rep[Auth[A]]): Auth[A] = {
    proxyOps[Auth[A]](p)(scala.reflect.classTag[Auth[A]])
  }

  // familyElem
  class AuthElem[A, To <: Auth[A]](implicit val eA: Elem[A])
    extends EntityElem[To] {
    val parent: Option[Elem[_]] = None
    override def isEntityType = true
    override lazy val tag = {
      implicit val tagA = eA.tag
      weakTypeTag[Auth[A]].asInstanceOf[WeakTypeTag[To]]
    }
    override def convert(x: Rep[Reifiable[_]]) = {
      implicit val eTo: Elem[To] = this
      val conv = fun {x: Rep[Auth[A]] => convertAuth(x) }
      tryConvert(element[Auth[A]], this, x, conv)
    }

    def convertAuth(x : Rep[Auth[A]]): Rep[To] = {
      assert(x.selfType1 match { case _: AuthElem[_, _] => true; case _ => false })
      x.asRep[To]
    }
    override def getDefaultRep: Rep[To] = ???
  }

  implicit def authElement[A](implicit eA: Elem[A]): Elem[Auth[A]] =
    new AuthElem[A, Auth[A]]

  implicit case object AuthCompanionElem extends CompanionElem[AuthCompanionAbs] {
    lazy val tag = weakTypeTag[AuthCompanionAbs]
    protected def getDefaultRep = Auth
  }

  abstract class AuthCompanionAbs extends CompanionBase[AuthCompanionAbs] with AuthCompanion {
    override def toString = "Auth"
  }
  def Auth: Rep[AuthCompanionAbs]
  implicit def proxyAuthCompanion(p: Rep[AuthCompanion]): AuthCompanion = {
    proxyOps[AuthCompanion](p)
  }

  // elem for concrete class
  class LoginElem(val iso: Iso[LoginData, Login])
    extends AuthElem[$bar[Unit,String], Login]
    with ConcreteElem[LoginData, Login] {
    override val parent: Option[Elem[_]] = Some(authElement(sumElement(UnitElement,StringElement)))

    override def convertAuth(x: Rep[Auth[$bar[Unit,String]]]) = // Converter is not generated by meta
!!!("Cannot convert from Auth to Login: missing fields List(user, password)")
    override def getDefaultRep = super[ConcreteElem].getDefaultRep
    override lazy val tag = {
      weakTypeTag[Login]
    }
  }

  // state representation type
  type LoginData = (String, String)

  // 3) Iso for concrete class
  class LoginIso
    extends Iso[LoginData, Login]()(pairElement(implicitly[Elem[String]], implicitly[Elem[String]])) {
    override def from(p: Rep[Login]) =
      (p.user, p.password)
    override def to(p: Rep[(String, String)]) = {
      val Pair(user, password) = p
      Login(user, password)
    }
    lazy val defaultRepTo: Rep[Login] = Login("", "")
    lazy val eTo = new LoginElem(this)
  }
  // 4) constructor and deconstructor
  abstract class LoginCompanionAbs extends CompanionBase[LoginCompanionAbs] with LoginCompanion {
    override def toString = "Login"
    def apply(p: Rep[LoginData]): Rep[Login] =
      isoLogin.to(p)
    def apply(user: Rep[String], password: Rep[String]): Rep[Login] =
      mkLogin(user, password)
  }
  object LoginMatcher {
    def unapply(p: Rep[Auth[$bar[Unit,String]]]) = unmkLogin(p)
  }
  def Login: Rep[LoginCompanionAbs]
  implicit def proxyLoginCompanion(p: Rep[LoginCompanionAbs]): LoginCompanionAbs = {
    proxyOps[LoginCompanionAbs](p)
  }

  implicit case object LoginCompanionElem extends CompanionElem[LoginCompanionAbs] {
    lazy val tag = weakTypeTag[LoginCompanionAbs]
    protected def getDefaultRep = Login
  }

  implicit def proxyLogin(p: Rep[Login]): Login =
    proxyOps[Login](p)

  implicit class ExtendedLogin(p: Rep[Login]) {
    def toData: Rep[LoginData] = isoLogin.from(p)
  }

  // 5) implicit resolution of Iso
  implicit def isoLogin: Iso[LoginData, Login] =
    new LoginIso

  // 6) smart constructor and deconstructor
  def mkLogin(user: Rep[String], password: Rep[String]): Rep[Login]
  def unmkLogin(p: Rep[Auth[$bar[Unit,String]]]): Option[(Rep[String], Rep[String])]

  // elem for concrete class
  class HasPermissionElem(val iso: Iso[HasPermissionData, HasPermission])
    extends AuthElem[Boolean, HasPermission]
    with ConcreteElem[HasPermissionData, HasPermission] {
    override val parent: Option[Elem[_]] = Some(authElement(BooleanElement))

    override def convertAuth(x: Rep[Auth[Boolean]]) = // Converter is not generated by meta
!!!("Cannot convert from Auth to HasPermission: missing fields List(user, password)")
    override def getDefaultRep = super[ConcreteElem].getDefaultRep
    override lazy val tag = {
      weakTypeTag[HasPermission]
    }
  }

  // state representation type
  type HasPermissionData = (String, String)

  // 3) Iso for concrete class
  class HasPermissionIso
    extends Iso[HasPermissionData, HasPermission]()(pairElement(implicitly[Elem[String]], implicitly[Elem[String]])) {
    override def from(p: Rep[HasPermission]) =
      (p.user, p.password)
    override def to(p: Rep[(String, String)]) = {
      val Pair(user, password) = p
      HasPermission(user, password)
    }
    lazy val defaultRepTo: Rep[HasPermission] = HasPermission("", "")
    lazy val eTo = new HasPermissionElem(this)
  }
  // 4) constructor and deconstructor
  abstract class HasPermissionCompanionAbs extends CompanionBase[HasPermissionCompanionAbs] with HasPermissionCompanion {
    override def toString = "HasPermission"
    def apply(p: Rep[HasPermissionData]): Rep[HasPermission] =
      isoHasPermission.to(p)
    def apply(user: Rep[String], password: Rep[String]): Rep[HasPermission] =
      mkHasPermission(user, password)
  }
  object HasPermissionMatcher {
    def unapply(p: Rep[Auth[Boolean]]) = unmkHasPermission(p)
  }
  def HasPermission: Rep[HasPermissionCompanionAbs]
  implicit def proxyHasPermissionCompanion(p: Rep[HasPermissionCompanionAbs]): HasPermissionCompanionAbs = {
    proxyOps[HasPermissionCompanionAbs](p)
  }

  implicit case object HasPermissionCompanionElem extends CompanionElem[HasPermissionCompanionAbs] {
    lazy val tag = weakTypeTag[HasPermissionCompanionAbs]
    protected def getDefaultRep = HasPermission
  }

  implicit def proxyHasPermission(p: Rep[HasPermission]): HasPermission =
    proxyOps[HasPermission](p)

  implicit class ExtendedHasPermission(p: Rep[HasPermission]) {
    def toData: Rep[HasPermissionData] = isoHasPermission.from(p)
  }

  // 5) implicit resolution of Iso
  implicit def isoHasPermission: Iso[HasPermissionData, HasPermission] =
    new HasPermissionIso

  // 6) smart constructor and deconstructor
  def mkHasPermission(user: Rep[String], password: Rep[String]): Rep[HasPermission]
  def unmkHasPermission(p: Rep[Auth[Boolean]]): Option[(Rep[String], Rep[String])]

  registerModule(scalan.meta.ScalanCodegen.loadModule(Authentications_Module.dump))
}

// Seq -----------------------------------
trait AuthenticationsSeq extends AuthenticationsDsl with scalan.ScalanSeq {
  self: AuthenticationsDslSeq =>
  lazy val Auth: Rep[AuthCompanionAbs] = new AuthCompanionAbs with UserTypeSeq[AuthCompanionAbs] {
    lazy val selfType = element[AuthCompanionAbs]
  }

  case class SeqLogin
      (override val user: Rep[String], override val password: Rep[String])

    extends Login(user, password)
        with UserTypeSeq[Login] {
    lazy val selfType = element[Login]
  }
  lazy val Login = new LoginCompanionAbs with UserTypeSeq[LoginCompanionAbs] {
    lazy val selfType = element[LoginCompanionAbs]
  }

  def mkLogin
      (user: Rep[String], password: Rep[String]): Rep[Login] =
      new SeqLogin(user, password)
  def unmkLogin(p: Rep[Auth[$bar[Unit,String]]]) = p match {
    case p: Login @unchecked =>
      Some((p.user, p.password))
    case _ => None
  }

  case class SeqHasPermission
      (override val user: Rep[String], override val password: Rep[String])

    extends HasPermission(user, password)
        with UserTypeSeq[HasPermission] {
    lazy val selfType = element[HasPermission]
  }
  lazy val HasPermission = new HasPermissionCompanionAbs with UserTypeSeq[HasPermissionCompanionAbs] {
    lazy val selfType = element[HasPermissionCompanionAbs]
  }

  def mkHasPermission
      (user: Rep[String], password: Rep[String]): Rep[HasPermission] =
      new SeqHasPermission(user, password)
  def unmkHasPermission(p: Rep[Auth[Boolean]]) = p match {
    case p: HasPermission @unchecked =>
      Some((p.user, p.password))
    case _ => None
  }
}

// Exp -----------------------------------
trait AuthenticationsExp extends AuthenticationsDsl with scalan.ScalanExp {
  self: AuthenticationsDslExp =>
  lazy val Auth: Rep[AuthCompanionAbs] = new AuthCompanionAbs with UserTypeDef[AuthCompanionAbs] {
    lazy val selfType = element[AuthCompanionAbs]
    override def mirror(t: Transformer) = this
  }

  case class ExpLogin
      (override val user: Rep[String], override val password: Rep[String])

    extends Login(user, password) with UserTypeDef[Login] {
    lazy val selfType = element[Login]
    override def mirror(t: Transformer) = ExpLogin(t(user), t(password))
  }

  lazy val Login: Rep[LoginCompanionAbs] = new LoginCompanionAbs with UserTypeDef[LoginCompanionAbs] {
    lazy val selfType = element[LoginCompanionAbs]
    override def mirror(t: Transformer) = this
  }

  object LoginMethods {
    object toOper {
      def unapply(d: Def[_]): Option[Rep[Login]] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[LoginElem] && method.getName == "toOper" =>
          Some(receiver).asInstanceOf[Option[Rep[Login]]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[Login]] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }
  }

  object LoginCompanionMethods {
  }

  def mkLogin
    (user: Rep[String], password: Rep[String]): Rep[Login] =
    new ExpLogin(user, password)
  def unmkLogin(p: Rep[Auth[$bar[Unit,String]]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: LoginElem @unchecked =>
      Some((p.asRep[Login].user, p.asRep[Login].password))
    case _ =>
      None
  }

  case class ExpHasPermission
      (override val user: Rep[String], override val password: Rep[String])

    extends HasPermission(user, password) with UserTypeDef[HasPermission] {
    lazy val selfType = element[HasPermission]
    override def mirror(t: Transformer) = ExpHasPermission(t(user), t(password))
  }

  lazy val HasPermission: Rep[HasPermissionCompanionAbs] = new HasPermissionCompanionAbs with UserTypeDef[HasPermissionCompanionAbs] {
    lazy val selfType = element[HasPermissionCompanionAbs]
    override def mirror(t: Transformer) = this
  }

  object HasPermissionMethods {
    object eA {
      def unapply(d: Def[_]): Option[Rep[HasPermission]] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[HasPermissionElem] && method.getName == "eA" =>
          Some(receiver).asInstanceOf[Option[Rep[HasPermission]]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[HasPermission]] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }

    object toOper {
      def unapply(d: Def[_]): Option[Rep[HasPermission]] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[HasPermissionElem] && method.getName == "toOper" =>
          Some(receiver).asInstanceOf[Option[Rep[HasPermission]]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[HasPermission]] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }
  }

  object HasPermissionCompanionMethods {
  }

  def mkHasPermission
    (user: Rep[String], password: Rep[String]): Rep[HasPermission] =
    new ExpHasPermission(user, password)
  def unmkHasPermission(p: Rep[Auth[Boolean]]) = p.elem.asInstanceOf[Elem[_]] match {
    case _: HasPermissionElem @unchecked =>
      Some((p.asRep[HasPermission].user, p.asRep[HasPermission].password))
    case _ =>
      None
  }

  object AuthMethods {
    object toOper {
      def unapply(d: Def[_]): Option[Rep[Auth[A]] forSome {type A}] = d match {
        case MethodCall(receiver, method, _, _) if receiver.elem.isInstanceOf[AuthElem[_, _]] && method.getName == "toOper" =>
          Some(receiver).asInstanceOf[Option[Rep[Auth[A]] forSome {type A}]]
        case _ => None
      }
      def unapply(exp: Exp[_]): Option[Rep[Auth[A]] forSome {type A}] = exp match {
        case Def(d) => unapply(d)
        case _ => None
      }
    }
  }

  object AuthCompanionMethods {
  }
}

object Authentications_Module {
  val packageName = "scalan.examples"
  val name = "Authentications"
  val dump = "H4sIAAAAAAAAAL1WPYwbRRQe753PZ/vywxWRghRxGF8giNinSCjFSSDn4hAkc2duE4ScCDRej51JZmc2M+NjTZEe6CI6hFD6FJEoKJBoEBKioEKARE0VQEkEpALxZvbHP5e9XEO2GO28ffN+vu+9t3P7d5RXEh1XHmaY13yicc217w2lq26Ta6pHb4jekJGzpH/nizvvnnReGTpoqYWKmHtEaSGVRs+2rIG6JxgjnqaC16nvDzXuMlJvUaXXW2i+K3qj6+gGyrXQYU9wTxJN3A2GlSIqli8S45Cm+6Ldj7aCsQ9eN0HWJ4K8IDHVEB34OBzpb5PAHXHBR75GB+PQtgITFugUqB8IqRMXBTB3RfSS7TzHIEDLrat4B9fBxaDuakn5AE6WA+xdwwOyCSpGfR4CVoT1L4wCu59roZIi188q9rofMCsJA0D3lI2gNganloJTM+BUXSIpZvR9bD62pQhHKHpycwhZEy89xkRigTR5r/rhZe/SQ7fsO+ZwaOIo2PQWwNAzGUxbHgDEb7dvqgev3TrtoFIHlahqdJWW2NOTfMdQlTHnQtuYU/SwHABVlSyqrJcG6MzUQ9ETfoA5WIpxXAKSGPWoNspGthRTk4F7QQckUc2FQS7NdyUjX1s0G5ix9t2jJ1d/a77tIGfaRRFMus0wkIlRjeYbQ30lNm3WQyCqdrFMvT2X5S0gbUl9KO0d8vLXX16899Vm3jpc7pE+HjL9FmZDEhVa7H4civHsvHACfF3kNKrbIyCpVDRaGB8phrNrYY/0UyKev/tH75s1dNlJ6Yuz3V/FgIm8+vnH8g8nXnXQYsc21zmGBx1gUDUZ8bfkhuC6gxbFDpHRl8IOZubtkRVUiAGJeZ0kZA4I0WglcwwExLC1HgamcRIAylHjbApOqufa1b/d7z6+bfpCoqXoSzQX/qWn//nlYF/blgGkh4rIhOI5GCcRGmZ5OgLYClZST2ZZ1WgxAEzeE7K359lpikpRHK7wyVOVB/SdWx9pS0YunB5DW92r0Prr9lx1D16ScfhXZ8358+hPnzmoCPB3qfZxUF3bZx//j72JUsDGy2pgBrUYUL4x6Wx1PLuO2VeN8lZr5mM5N9OUM30K7ZLvY6agfApnhGAE891MWC8Th3aR++SKwqx1u57KQuvIeazaRPpUKQDrcagdmNIeK02EvRD7m0ayuE1on5rfzN4IgyjXmDC5K6d9J3bAWH5EPsn4tZJ0OB3LHrhQcjcvHT8v73/ygWPKMt8VQ95LahguIJqE+kwiy03XMNQslthPanYcRmaeb06jCoqHTLjmQuJFvQWSOFoSYmghomI8JKpkpOHGnQTtfOPhp5svfv/5r/a3UTI9CSONp9eYyd/FNC/LM2HA9WQiAUDVtKsN/j8YxgfODQoAAA=="
}

