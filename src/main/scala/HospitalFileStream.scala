import com.github.tototoshi.csv.*
import zio.*
import zio.stream.ZStream

import scala.io.Source

object HospitalFileStream extends ZIOAppDefault {

  def generateHospitals : ZIO[Any & ZIOAppArgs & Scope, Throwable, List[Hospital]] =
    for {
      source <- ZIO.succeed(CSVReader.open(Source.fromResource("hospitals.csv")))
      hospitals <- ZStream
        .fromIterator[Seq[String]](source.iterator)
        .drop(1)
        .map[Option[Hospital]] {
          line =>
            Some(Hospital(
              hospital_name = line.head,
              longitude = line(1).toDouble,
              latitude = line(2).toDouble,
              specialty = line(3),
              available_beds = line(4).toInt
            ))
        }
        .collectSome[Hospital]
        .runCollect
      _ <- ZIO.succeed(source.close())
      hospitalsList <- ZIO.succeed(hospitals.toList)
    } yield hospitalsList

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = generateHospitals
}
