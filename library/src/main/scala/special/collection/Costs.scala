package special.collection {
  import scalan._

  trait Costs extends Base { self: Library =>
    import CostedBuilder._;
    import Costed._;
    trait Costed[Val] extends Def[Costed[Val]] {
      implicit def eVal: Elem[Val];
      def builder: Rep[CostedBuilder];
      def value: Rep[Val];
      def cost: Rep[Int];
      def dataSize: Rep[Long]
    };
    trait CostedBuilder extends Def[CostedBuilder] {
      def ConstructTupleCost: Rep[Int] = toRep(1.asInstanceOf[Int]);
      def ConstructSumCost: Rep[Int] = toRep(1.asInstanceOf[Int]);
      def SumTagSize: Rep[Int] = toRep(1.asInstanceOf[Int])
    };
    trait CostedCompanion;
    trait CostedBuilderCompanion
  }
}