import GameDates.*
import PlayoffRounds.*
import SeasonYears.*
import java.time.LocalDate
import zio._
import zio.stream.ZStream
import com.github.tototoshi.csv._

object FileStream extends ZIOAppDefault {

  override val run: ZIO[Any & ZIOAppArgs & Scope, Throwable, Unit] =
    for {
      url <- ZIO.succeed(getClass().getClassLoader().getResource("mlb_elo.csv"))
      source <- ZIO.succeed(CSVReader.open(url.getFile()))
      stream <- ZStream
        .fromIterator[Seq[String]](source.iterator)
        .take(10)
        .map[Option[Game]](line =>
          line match
            case line if line.head == "date" => None
            case line =>
              Some(
                Game(
                  GameDate(LocalDate.parse(line.head)),
                  season = SeasonYear(line.tail.head.toInt),
                  None
                )
              )
        )
        .collectSome[Game]
        .grouped(5)
        .foreach(Console.printLine(_))
      _ <- ZIO.succeed(source.close())
    } yield ()
}
