import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class HospitalTest extends AnyFunSuite with Matchers {

  test("Hospital.getDistance") {
    val hospital = Hospital("Test Hospital", 20.0, 20.0, "General", 10)
    val eventDate = EventDates.EventDates(LocalDate.parse("2024-01-19"))
    val event = Event(eventDate, "Doe", "John", 18, 10.0, 10.0, 5, "Emergency")
    hospital.getDistance(event).round shouldEqual 14
  }

  test("Hospital.hasEnoughPlace") {
    val hospitalWithPlace = Hospital("Test Hospital", 1.0, 2.0, "General", 5)
    val hospitalWithoutPlace = Hospital("Test Hospital", 1.0, 2.0, "General", 0)

    hospitalWithPlace.hasEnoughPlace shouldEqual true
    hospitalWithoutPlace.hasEnoughPlace shouldEqual false
  }

  test("Hospital.addEvent") {
    val hospital = Hospital("Test Hospital", 1.0, 2.0, "General", 3)
    val eventDate = EventDates.EventDates(LocalDate.parse("2024-01-19"))
    val event = Event(eventDate, "Doe", "John", 30, 3, 4, 5, "Emergency")

    val updatedHospital = hospital.addEvent(event)

    updatedHospital.available_beds shouldEqual 2
    updatedHospital.events shouldEqual List(event)
  }

}
