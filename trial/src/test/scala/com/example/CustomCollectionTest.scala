package com.example

import com.example.CustomCollectionTest.{ StringWrapper, Strings, WithList, WithMap, WithSet }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.util.chaining.scalaUtilChainingOps

class CustomCollectionTest extends AnyWordSpec with Matchers {

  "Future.sequence" can {
    "run for WithMap" in {
      val coll = WithMap(Map("a" -> 1, "b" -> 2))

      val fs = coll.map(_._2).map(Future.successful)
      Future.sequence(fs).await() should contain allElementsOf List(1, 2)
    }

    "run for WithList" in {
      val coll = WithList(List(1, 2, 3, 4))

      val fs =
        coll
          .filter(_ % 2 == 0)
          .map(_ * 2)
          .map(Future.successful)
      Future.sequence(fs).await() should contain allElementsOf List(4, 8)
    }

    "run for WithSet" in {
      val coll = WithSet(Set("a", "bb", "ccc"))

      val fs = coll.filter(_.length >= 2).map(Future.successful)
      Future.sequence(fs).await() should contain allElementsOf List("bb", "ccc")
    }

    "run for Strings" in {
      val coll = List(StringWrapper("aaa"), StringWrapper("bbb")) pipe Strings

      val fs = coll.map(Future.successful)
      Future.sequence(fs).await() should contain allElementsOf List(StringWrapper("aaa"), StringWrapper("bbb"))
    }
  }

  implicit class FutureOps[A](self: Future[A]) {
    def await(): A = Await.result(self, Duration("10s"))
  }
}

object CustomCollectionTest {

  trait FCC[+A] extends immutable.Iterable[A] {
    def delegate: immutable.Iterable[A]
    override def iterator: Iterator[A] = delegate.iterator
  }

  case class WithMap(delegate: Map[String, Int]) extends FCC[(String, Int)]

  case class WithList(delegate: List[Int]) extends FCC[Int]

  case class WithSet(delegate: Set[String]) extends FCC[String]

  case class StringWrapper(value: String) extends AnyVal
  case class Strings(delegate: List[StringWrapper]) extends FCC[StringWrapper]
}
