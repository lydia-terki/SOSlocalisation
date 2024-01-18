
import GameDates.*
import SeasonYears.*
import java.time.LocalDate
import zio._
import zio.stream.ZStream
import com.github.tototoshi.csv._

object EventFileStream extends ZIOAppDefault {

  override val run: ZIO[Any & ZIOAppArgs & Scope, Throwable, List[Event]] =
    for {
      url <- ZIO.succeed(getClass().getClassLoader().getResource("patient.csv"))
      source <- ZIO.succeed(CSVReader.open(url.getFile()))
      events <- ZStream
        .fromIterator[Seq[String]](source.iterator)
        .take(10)
        .map[Option[Event]] {line =>
            line match {
              case line if line.head == "date" => None
              case line =>
                Some(Event(
                  date = EventDates.GameDate(LocalDate.parse(line.head)),
                  lastname = line(1),
                  firstname = line(2),
                  age = line(3).toInt,
                  latitude = line(4).toInt,
                  longitude = line(5).toInt,
                  pain = line(6).toInt,
                  cause = line(7)
                ))
            }
        }
        .collectSome[Event]
        .runCollect
      _ <- ZIO.succeed(source.close())
      eventsList <- ZIO.succeed(events.toList)
    } yield eventsList
}
