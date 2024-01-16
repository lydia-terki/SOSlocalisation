import zio._
import zio.stream._

object ParserStream extends ZIOAppDefault {
  val inputStream: ZStream[Any, Nothing, String] =
    ZStream.fromIterable(List("Header", "Request1", "Request2", "Request3"))

  case class Header(str: String)

  case class Body(str: String)

  case class Request(header: Header, body: Body)

  val requestHeaderSink =
    ZSink
      .head[String]
      .map(line => Header(line.get))

  val requestBodySink: ZSink[Any, Nothing, String, Nothing, Body] =
    ZSink.collectAll[String].map(chunk => Body(chunk.mkString))

  val run: ZIO[Any, java.io.IOException, Option[Unit]] = inputStream
    .run(requestHeaderSink zip requestBodySink)
    .map(Request.apply)
    .forEachZIO(Console.printLine(_))
}
