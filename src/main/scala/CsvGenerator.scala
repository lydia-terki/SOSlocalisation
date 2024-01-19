import zio._
import com.github.tototoshi.csv.CSVWriter
import java.io.File
import scala.util.Random
import com.github.tototoshi.csv.defaultCSVFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val NB_PATIENTS = 15
val NB_HOSPITALS = 5
object CsvGenerator extends ZIOAppDefault {

  private def generateString: String = {
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    val nameLength = Random.between(2,15)
    alphabet.map(x => alphabet.charAt(Random.between(0, alphabet.length))).substring(0, nameLength)
  }

  val now: String = LocalDate.now()
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
  private val listSpec = List("generalist", "cardiologist", "neurologist", "pediatrician", "dermatologist", "gynecologist", "ophthalmologist")
  private val listSpecHospitals = List("general", "cardiology", "neurology", "pediatrics", "dermatology", "gynecology", "ophthalmology")
  val distanceFactor: Double = Random.nextDouble() * 0.2 + 0.8

  private def generateEventsData: ZIO[Any, Nothing, List[Event]] =
    ZIO.succeed{
      List.fill(NB_PATIENTS) {
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

  private def generateHospitalsData: ZIO[Any, Nothing, List[Hospital]] =
    ZIO.succeed {
      List.fill(NB_HOSPITALS) {

        Hospital(
          hospital_name = generateString,
          latitude = Random.between(1, 100).toString,
          longitude = Random.between(1, 100).toString,
          specialty = listSpecHospitals(Random.between(0, listSpecHospitals.length)),
          available_beds = Random.between(1, 250)
        )
      }
    }


  private def writeCsvEvents(events: List[Event]): ZIO[Any, Nothing, Unit] = ZIO.succeed {
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

  private def writeCsvHospitals(hospitals: List[Hospital]): ZIO[Any, Nothing, Unit] = ZIO.succeed {
    val header = List("HospitalName", "Latitude", "Longitude", "Specialty", "AvailableBeds")
    val body = hospitals.map(hospital => List(hospital.hospital_name, hospital.latitude, hospital.longitude, hospital.specialty, hospital.available_beds))
    val outputFile = new File(s"src/main/resources/hospitals.csv")

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
      _      <- writeCsvEvents(events)

      hospitals <- generateHospitalsData
      _      <- writeCsvHospitals(hospitals)

    } yield ExitCode.success
}