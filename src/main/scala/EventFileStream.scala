import java.time.LocalDate
import zio.*
import zio.stream.ZStream
import com.github.tototoshi.csv.*

import scala.io.Source

/**
 * The `EventFileStream` object provides functionality to read events data from a CSV file.
 */
object EventFileStream extends ZIOAppDefault {

  /**
   * Reads events data from the "events.csv" file and returns a list of events.
   *
   * @return A ZIO effect producing a list of events read from the CSV file.
   */
  def generateEvents: ZIO[Any & ZIOAppArgs & Scope, Throwable, List[Event]] =
    for {
      source <- ZIO.succeed(CSVReader.open(Source.fromResource("events.csv")))
      events <- ZStream
        .fromIterator[Seq[String]](source.iterator)
        .drop(1)
        .map[Option[Event]] {
          line =>
            Some(Event(
              date = EventDates.EventDates(LocalDate.parse(line.head)),
              lastname = line(1),
              firstname = line(2),
              age = line(3).toInt,
              latitude = line(4).toInt,
              longitude = line(5).toInt,
              pain = line(6).toInt,
              need = line(7)
            ))
        }
        .collectSome[Event]
        .runCollect
      _ <- ZIO.succeed(source.close())
      eventsList <- ZIO.succeed(events.toList)
    } yield eventsList

  /**
   * The main entry point for the ZIO application. Calls `generateEvents` to read and process events data.
   *
   * @return A ZIO effect representing the result of reading events from the CSV file.
   */
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = generateEvents
}
