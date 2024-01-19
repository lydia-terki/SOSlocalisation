import zio._
import com.github.tototoshi.csv.CSVWriter
import java.io.File
import scala.util.Random
import com.github.tototoshi.csv.defaultCSVFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val NB_PATIENTS = 15
val NB_HOSPITALS = 5
val alphabet = "abcdefghijklmnopqrstuvwxyz"

/**
 * Writes data to a CSV file.
 *
 * @param header The header row of the CSV file.
 * @param body The body of the CSV file, representing rows of data.
 * @param outputFile The File object representing the output CSV file.
 */
def writeCsv(header: List[String], body: List[List[Any]], outputFile: File): Unit = {
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

object CsvGenerator extends ZIOAppDefault {

  private val listSpec = List("generalist", "cardiologist", "neurologist", "pediatrician", "dermatologist", "gynecologist", "ophthalmologist")
  private val listSpecHospitals = List("general", "cardiology", "neurology", "pediatrics", "dermatology", "gynecology", "ophthalmology")
  private val nowString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
  val distanceFactor: Double = Random.nextDouble() * 0.2 + 0.8

  /**
   * Generates a random string of lowercase letters.
   *
   * The length of the generated string is randomly chosen between 2 and 15 characters.
   *
   * @return A randomly generated string.
   */
  private def generateString: String = {
    val nameLength = Random.between(2,15)
    alphabet.map(x => alphabet.charAt(Random.between(0, alphabet.length))).substring(0, nameLength)
  }

  /**
   * Generates a list of synthetic events.
   *
   * Each event in the list is randomly generated with the following properties:
   * - Last name (random string in lower case)
   * - First name (random string in lower case)
   * - Age (random value between 0 and 100)
   * - Latitude (random value between 1 and 100)
   * - Longitude (random value between 1 and 100)
   * - Pain level (random value between 1 and 10)
   * - Need (randomly selected from a predefined list)
   * - Date of the event (current date)
   *
   * @return A ZIO effect producing a list of synthetic events.
   */
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
          date = EventDates.EventDates(LocalDate.parse(nowString))
        )
      }
    }

  /**
   * Generates a list of synthetic hospital data.
   *
   * Each hospital in the list is randomly generated with the following properties:
   * - Hospital name
   * - Latitude (random value between 1 and 100)
   * - Longitude (random value between 1 and 100)
   * - Specialty (randomly selected from a predefined list see [[listSpecHospitals]])
   * - Available beds (random value between 1 and 250)
   *
   * @return A ZIO effect producing a list of synthetic hospitals.
   */
  private def generateHospitalsData: ZIO[Any, Nothing, List[Hospital]] =
    ZIO.succeed {
      List.fill(NB_HOSPITALS) {

        Hospital(
          hospital_name = generateString,
          latitude = Random.between(1, 100),
          longitude = Random.between(1, 100),
          specialty = listSpecHospitals(Random.between(0, listSpecHospitals.length)),
          available_beds = Random.between(1, 250)
        )
      }
    }

  /**
   * Writes a list of events to a CSV file.
   *
   * @param events List of events to be written to the CSV file.
   * @return A ZIO effect representing the success or failure of the operation.
   */
  private def writeCsvEvents(events: List[Event]): ZIO[Any, Nothing, Unit] = ZIO.succeed {
    val header = List("Date", "FirstName", "LastName", "Age", "Latitude", "Longitude", "Pain", "Need")
    val body = events.map(event => List(event.date, event.firstname, event.lastname, event.age, event.latitude, event.longitude, event.pain, event.need))
    val outputFile = new File(s"src/main/resources/events.csv")
    writeCsv(header, body, outputFile)
  }

  /**
   * Writes a list of hospitals to a CSV file.
   *
   * @param hospitals List of hospitals to be written to the CSV file.
   * @return A ZIO effect representing the success or failure of the operation.
   */
  private def writeCsvHospitals(hospitals: List[Hospital]): ZIO[Any, Nothing, Unit] = ZIO.succeed {
    val header = List("HospitalName", "Latitude", "Longitude", "Specialty", "AvailableBeds")
    val body = hospitals.map(hospital => List(hospital.hospital_name, hospital.latitude, hospital.longitude, hospital.specialty, hospital.available_beds))
    val outputFile = new File(s"src/main/resources/hospitals.csv")
    writeCsv(header, body, outputFile)
  }

  /**
   * The main ZIO program that generates synthetic data for events and hospitals,
   * and writes the data to corresponding CSV files.
   *
   * @return A ZIO effect representing the success or failure of the program.
   */
  override val run: ZIO[Any, Throwable, ExitCode] =
    for {
      events <- generateEventsData
      _      <- writeCsvEvents(events)
      hospitals <- generateHospitalsData
      _      <- writeCsvHospitals(hospitals)
    } yield ExitCode.success
}