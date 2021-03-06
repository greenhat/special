package special.wrappers

import scala.language.reflectiveCalls
import scalan.meta.RType
import RType._
import special.collection.Types._

class WRTypeTests extends WrappersTests {

  lazy val ctx = new WrappersCtx
  import ctx._
  import Col._
  import WRType._
  import EnvRep._
  import Liftables._

  test("invokeUnlifted") {
    val ty = RType[Int]
    check(ty, { env: EnvRep[WRType[Int]] => for { xs <- env } yield xs.name }, ty.name)
  }

  test("Implicit conversion from RType to Elem") {
    val eInt: Elem[Int] = RType.IntType
    eInt shouldBe IntElement

    val ePair: Elem[(Int, Col[Byte])] = RType[(Int, SCol[Byte])]
    ePair shouldBe element[(Int, Col[Byte])]
  }
}
