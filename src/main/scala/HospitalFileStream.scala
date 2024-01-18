
import GameDates.*
import com.github.tototoshi.csv.*
import zio.*
import zio.stream.ZStream

object HospitalFileStream extends ZIOAppDefault {

  override val run: ZIO[Any & ZIOAppArgs & Scope, Throwable, List[Hospital]] =
    for {
      url <- ZIO.succeed(getClass().getClassLoader().getResource("hospital.csv"))
      source <- ZIO.succeed(CSVReader.open(url.getFile()))
      hospitals <- ZStream
        .fromIterator[Seq[String]](source.iterator)
        .take(10)
        .drop(1)
        .map[Option[Hospital]] {line =>
            line match {
              case line if line.head == "date" => None
              case line =>
                Some(Hospital(
                  hospital_name = line(0),
                  longitude = line(1),
                  latitude = line(2),
                  specialty = line(3),
                  available_beds = line(4).toInt
                ))
            }
        }
        .collectSome[Hospital]
        .runCollect
      _ <- ZIO.succeed(source.close())
      hospitalsList <- ZIO.succeed(hospitals.toList)
    } yield hospitalsList
}
