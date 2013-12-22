/**
 * Shamelessly taken from https://github.com/namin/lms-sandbox
 */
package scalan

import java.lang.{reflect => jreflect}

import scalan.staged.{BaseExp}

trait ProxyBase { self: Scalan =>
  def proxyOps[T,Ops<:AnyRef](x: Rep[T])(implicit m: Manifest[Ops]): Ops = x.asInstanceOf[Ops]
}

trait ProxyExp extends ProxyBase with BaseExp { self: ScalanStaged =>

  case class MethodCall[T](receiver: Exp[Any], method: jreflect.Method, args: List[AnyRef])(implicit val elem: Elem[T]) extends Def[T] {
    override def mirror(t: Transformer) =
      MethodCall[T](t(receiver), method, args map { case a: Exp[_] => t(a) case a => a })
    override def thisSymbol: Rep[T] = { this }
  }

//TODO
//  case class MethodCallLifted[T](receiver: PA[Any], method: jreflect.Method, args: List[AnyRef])
//                                (implicit val elem: Elem[T]) extends StagedArrayBase[T] {
//    override def mirror(t: Transformer) =
//      MethodCallLifted[T](t(receiver), method, args map { case a: Exp[_] => t(a) case a => a })
//  }

  override def proxyOps[T,Ops<:AnyRef](x: Rep[T])(implicit m: Manifest[Ops]): Ops = {
    val clazz = m.runtimeClass
    val handler = new InvocationHandler(x)
    val proxy = jreflect.Proxy.newProxyInstance(clazz.getClassLoader(), Array(clazz), handler)
    proxy.asInstanceOf[Ops]
  }

  var invokeEnabled = false

  private def hasFuncArg(args: Array[AnyRef]):Boolean = {
    val res = args.foldLeft(0) {
      case (cnt, arg) => arg match {
        case af: Function1[_,_] => cnt+1
        case _ => cnt
      }
    }
    (res > 0)
  }

  // stack of receivers for which MethodCall nodes should be created by InvocationHandler
  var methodCallReceivers = List.empty[Exp[Any]]

  class InvocationHandler(receiver: Exp[Any]) extends jreflect.InvocationHandler {

    def invoke(proxy: AnyRef, m: jreflect.Method, _args: Array[AnyRef]) = {
      val args = _args == null match { case true => Array.empty[AnyRef] case _ => _args }
      receiver match {
        case Def(d) => {  // call method of the node
          val nodeClazz = d.getClass
          m.getDeclaringClass.isAssignableFrom(nodeClazz) && invokeEnabled && (!hasFuncArg(args)) match {
            case true =>
              val res = m.invoke(d, args: _*)
              res
            case _ => invokeMethodOfVar(m, args)
          }
        }
        case _ => invokeMethodOfVar(m, args)
      }
    }


    def invokeMethodOfVar(m: jreflect.Method, args: Array[AnyRef]) = {
      /* If invoke is enabled or current method has arg of type <function> - do not create methodCall */
      methodCallReceivers.contains(receiver) || (!(invokeEnabled || hasFuncArg(args))) match {
        case true =>
          createMethodCall(m, args)
        case _ =>
          val e = getRecieverElem
          val iso = e.iso
          methodCallReceivers = methodCallReceivers :+ receiver
          val wrapper = iso.toStaged(iso.fromStaged(receiver))
          methodCallReceivers = methodCallReceivers.tail
          val Def(d) = wrapper
          val res = m.invoke(d, args: _*)
          res
      }
    }

    def createMethodCall(m: jreflect.Method, args: Array[AnyRef]): Exp[Any] = {
      val resultElem = getResultElem(m, args)
      reifyObject(MethodCall[AnyRef](
              receiver, m, args.toList)(resultElem))(resultElem)
    }

    def getRecieverElem: ViewElem[Any,Any] = receiver.Elem match {
      case e: ViewElem[_,_] => e.asInstanceOf[ViewElem[Any,Any]]
      case _ =>
        !!!("Receiver with ViewElem expected", receiver)
    }

    def getResultElem(m: jreflect.Method, args: Array[AnyRef]): Elem[AnyRef] = {
      val e = getRecieverElem
      val zero = e.iso.defaultOf.value
      val Def(zeroNode) = zero
      val res = m.invoke(zeroNode, args: _*)
      res.asInstanceOf[Exp[AnyRef]].Elem
    }

  }

  override def formatDef(d: Def[_]) = d match {
    case MethodCall(obj, method, args) => {
      val className = method.getDeclaringClass.getName()
      "%s.%s(%s)".format(obj, className.substring(className.lastIndexOf("$")+1) + "." + method.getName(), args.mkString("", ",", ""))
    }
    case _ => super.formatDef(d)
  }

}