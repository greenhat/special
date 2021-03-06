package scalan.meta

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

class TypeDescTests extends BaseMetaTests {
  import RType._
  describe("implicit resolution") {
    it("resolve RType") {
      def test[T: RType](name: String) = {
        val t= RType[T]
        t.toString shouldBe name
      }
      test[Int]("ConcreteRType<Int>")(RType[Int])
      test[String]("ConcreteRType<String>")(RType[String])
    }

    it("resolve ClassTag") {
      import RType._
      def test[T: RType](name: String) = {
        val ct = implicitly[ClassTag[T]]
        ct.toString shouldBe name
      }
      test[Int]("Int")(RType[Int])
      test[String]("java.lang.String")(RType[String])
    }

  }

}
