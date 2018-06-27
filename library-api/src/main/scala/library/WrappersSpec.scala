package library

import scala.reflect.ClassTag
import scalan.SpecialPredef

trait WrapSpec {
}
/** NOTES:
  * 1) to avoid fallbackCanBuildFrom to pop up to wrappers add ClassTag context bound */
class ArrayWrapSpec extends WrapSpec {
  def zip[A,B](xs: Array[A], ys: Array[B]): Array[(A,B)] = xs.zip(ys)
  def map[A,B:ClassTag](xs: Array[A], f: A => B) = xs.map(f)
  def length[A](xs: Array[A]) = xs.length
  def fill[A:ClassTag](n: Int, elem: A): Array[A] = Array.fill(n)(elem)
  def slice[A](xs: Array[A], from: Int, until: Int): Array[A] = xs.slice(from, until)
  def foldLeft[A, B](xs: Array[A], zero: B, op: (B, A) => B): B = xs.foldLeft(zero)(op)
  def filter[A](xs: Array[A], p: A => Boolean): Array[A] = xs.filter(p)
  def forall[A](xs: Array[A], p: A => Boolean): Boolean = xs.forall(p)
  def exists[A](xs: Array[A], p: A => Boolean): Boolean = xs.exists(p)
  def foreach[A](xs: Array[A], p: A => Unit): Unit = xs.foreach(p)
  def apply[A](xs: Array[A], i: Int): A = xs.apply(i)
};

/** Wrappers spec for Option */
class OptionWrapSpec extends WrapSpec {
  def get[A](xs: Option[A]): A = xs.get
  def map[A,B](xs: Option[A], f: A => B): Option[B] = xs.map(f)
  def flatMat[A,B](xs: Option[A], f: A => Option[B]): Option[B] = xs.flatMap(f)
  def filter[A](xs: Option[A], f: A => Boolean): Option[A] = xs.filter(f)
  def getOrElse[A](xs: Option[A], default: A): A = xs.getOrElse(default)
  def isDefined[A](xs: Option[A]): Boolean  = xs.isDefined
  def isEmpty[A](xs: Option[A]): Boolean  = xs.isEmpty
  def fold[A,B](xs: Option[A], ifEmpty: B, f: A => B): B = xs.fold(ifEmpty)(f)
};

/** Wrappers spec for Either */
class EitherWrapSpec extends WrapSpec {
  def fold[A,B,C](xs: Either[A,B], fa: A => C, fb: B => C): C = xs.fold(fa, fb)
  def cond[A,B](c: Boolean, a: A, b: B): Either[A,B] = scala.util.Either.cond(c, b, a)
};

class SpecialPredefWrapSpec extends WrapSpec {
  def loopUntil[A](s1: A, isMatch: A => Boolean, step: A => A): A = SpecialPredef.loopUntil(s1, isMatch, step)
  def cast[A:ClassTag](v: Any): Option[A] = SpecialPredef.cast[A](v)
  def mapSum[A,B,C,D](e: Either[A,B], fa: A => C, fb: B => D): Either[C,D] = SpecialPredef.mapSum(e, fa, fb)
  def some[A](x: A): Option[A] = SpecialPredef.some(x)
  def none[A:ClassTag]: Option[A] = SpecialPredef.none[A]
  def left[A,B](a: A): Either[A,B] = SpecialPredef.left(a)
  def right[A,B](b: B): Either[A,B] = SpecialPredef.right(b)
}
