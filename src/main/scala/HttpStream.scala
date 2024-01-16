import zio._
import zio.http._
import zio.stream._
import zio.Duration._

object HttpStream extends ZIOAppDefault {

  def fetchData() = {
    val url = URL
      .decode(
        "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m"
      )
      .toOption
      .get // unsafe

    for {
      client <- ZIO.service[Client]
      res <- client.url(url).get("/")
    } yield res
  }

  override def run: ZIO[Any, Any, Unit] =
    val appLogic = for {
      _ <- ZStream(fetchData())
        .repeat(Schedule.spaced(10.seconds))
        // .groupedWithin(30, 10.seconds)
        .mapZIO { z =>
          for {
            res <- z
            body <- res.body.asString
            _ <- Console.printLine(s"body size is: ${body.length}")
          } yield body
        }
        .foreach(Console.printLine(_))
    } yield ()

    appLogic.provide(Client.default, Scope.default)

}
