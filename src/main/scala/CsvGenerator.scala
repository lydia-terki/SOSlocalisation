import zio._
import zio.Console
import zio.stream.ZStream
import com.github.tototoshi.csv.CSVWriter
import java.io.File
import scala.util.Random
import com.github.tototoshi.csv.defaultCSVFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val NB_PATIENTS = 15;

object CsvGenerator extends ZIOAppDefault {

  private def generateString: String = {
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    val nameLength = Random.between(2,15)
    alphabet.map(x => alphabet.charAt(Random.between(0, alphabet.length))).substring(0, nameLength)
  }

  private def generateEventsData: ZIO[Any, Nothing, List[Event]] =
    ZIO.succeed{
      val distanceFactor = Random.nextDouble() * 0.2 + 0.8
      List.fill(NB_PATIENTS) {
        val now = LocalDate.now()
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val listSpec = List("generalist", "cardiologist", "neurologist", "pediatrician", "dermatologist", "gynecologist", "ophthalmologist")
        Event(
          lastname = generateString,
          firstname = generateString,
          age = Random.between(0, 100),
          latitude = Random.between(1, 100),
          longitude = Random.between(1, 100),
          pain = Random.between(1, 10),
          need = listSpec(Random.between(0, listSpec.length)),
          date = EventDates.EventDates(LocalDate.parse(now))
        )
      }
    }
  
  private def writeCsv(events: List[Event]): ZIO[Any, Nothing, Unit] = ZIO.succeed {
    val header = List("Date", "FirstName", "LastName", "Age", "Latitude", "Longitude", "Pain", "Need")
    val body = events.map(event => List(event.date, event.firstname, event.lastname, event.age, event.latitude, event.longitude, event.pain, event.need))
    val outputFile = new File(s"src/main/resources/events.csv")

    if (!outputFile.exists()) {
      val csvWriter = CSVWriter.open(outputFile)
      try {
        csvWriter.writeAll(header :: body)
      } finally {
        csvWriter.close()
      }
      println(s"New file at : ${outputFile.getAbsolutePath}")
    } else {
      println(s"The file ${outputFile.getAbsolutePath} already exists.")
    }
  }

  override val run: ZIO[Any, Throwable, ExitCode] =
    for {
      events <- generateEventsData
      _      <- writeCsv(events)
    } yield ExitCode.success
}