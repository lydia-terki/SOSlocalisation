import zio._
import zio.stream._
import java.io.IOException

// Chunks
val emptyChunk = Chunk.empty
val fromIterableChunl = Chunk.fromIterable(List(1, 2, 3))
val filledChunck = Chunk.fill(3)(0)
val collectChunk =
  Chunk("Hello ZIO", 1.5, "Hello ZIO NIO", 2.0, "Some string", 2.5)
collectChunk.collect { case string: String => string }

// Effects
val failedEffect = ZIO.fail("fail!")
val oneIntValue = ZIO.succeed(3)
val oneListValue = ZIO.succeed(List(1, 2, 3))
val oneOption = ZIO.succeed(None)

// ZStreams
val emptyStream = ZStream.empty
val oneIntValueStream = ZStream.succeed(4)
val oneListValueStream = ZStream.succeed(List(1, 2, 3))
val finiteIntStream = ZStream.range(1, 10)
val infiniteIntStream = ZStream.iterate(1)(_ + 1)

// Type Aliases
type Stream[+E, +A] = ZStream[Any, E, A]
type UStream[+A] = ZStream[Any, Nothing, A]

// Operations
val s0: Stream[IOException, String] =
  ZStream
    .fromIterable((1 to 10).toList)
    .take(5)
    .tap(x => Console.printLine(s"before mapping: $x"))
    .map(_ * 2)
    .tap(x => Console.printLine(s"after mapping: $x"))
    .map(_.toString)

val s1: UStream[(Int, String)] =
  ZStream(1, 2, 3, 4, 5, 6).zipWith(ZStream("a", "b", "c"))((a, b) => (a, b))

val s2: UStream[(Int, String)] =
  ZStream(1, 2, 3, 4, 5, 6).zip(ZStream("a", "b", "c"))

s0.foreach(Console.printLine(_))

val s3 = ZStream.fromIterable(1 to 1000)
val sink = ZSink.sum[Int]
val sum = s3.run(sink)

val s4: UStream[Int] = ZStream(1, 2, 3, 4, 5)
val collection: UIO[Chunk[Int]] = s4.run(ZSink.collectAll[Int])