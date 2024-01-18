import zio.json.{DeriveJsonEncoder, JsonEncoder}

import java.time.LocalDate

case class Hospital(
                  hospital_name: String,
                  longitude: String,
                  latitude: String,
                  specialty: String,
                  available_beds: Int,
                  events: List[Event] = List.empty
               ){
  
  def getDistance(event: Event): Double = {
    val patientLatitude = event.latitude.toDouble
    val patientLongitude = event.longitude.toDouble
    val hospitalLongitude = longitude.toDouble
    val hospitalLatitude = longitude.toDouble
    val distance = Math.sqrt(Math.pow(patientLatitude - hospitalLongitude, 2) + Math.pow(patientLongitude - hospitalLatitude, 2))
    distance
  }
  def hasEnoughPlace: Boolean = available_beds > 0
  
  def addEvent(event: Event): Hospital = {
    val newEvents = event :: events
    Hospital(hospital_name, longitude, latitude, specialty, available_beds - 1, newEvents)
  }
}

object Hospital {
  implicit val eventEncoder: JsonEncoder[Hospital] = DeriveJsonEncoder.gen[Hospital]
}