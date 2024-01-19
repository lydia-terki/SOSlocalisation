import zio.json.{DeriveJsonEncoder, JsonEncoder}

/**
 * Represents a hospital with information such as name, location, specialty, available beds, and associated events.
 *
 * @param hospital_name The name of the hospital.
 * @param longitude The longitude of the hospital's location.
 * @param latitude The latitude of the hospital's location.
 * @param specialty The specialty of the hospital.
 * @param available_beds The number of available beds in the hospital.
 * @param events The list of events associated with the hospital (default is an empty list).
 */
case class Hospital(
                  hospital_name: String,
                  longitude: Double,
                  latitude: Double,
                  specialty: String,
                  var available_beds: Int,
                  events: List[Event] = List.empty
               ){

  /**
   * Calculates the distance between the hospital and a given event using Euclidean distance.
   *
   * @param event The event for which to calculate the distance.
   * @return The Euclidean distance between the hospital and the event.
   */
  def getDistance(event: Event): Double =
    Math.sqrt(Math.pow(event.longitude - longitude, 2) + Math.pow(event.latitude - latitude, 2))

  /**
   * Checks if the hospital has enough available beds.
   *
   * @return `true` if there are available beds, `false` otherwise.
   */
  def hasEnoughPlace: Boolean = available_beds > 0

  /**
   * Adds an event to the hospital and reduces the number of available beds.
   *
   * @param event The event to be added to the hospital.
   * @return A new Hospital instance with the added event and updated available beds.
   */
  def addEvent(event: Event): Hospital = {
    available_beds -= 1
    val newEvents = event :: events
    Hospital(hospital_name, longitude, latitude, specialty, available_beds, newEvents)
  }
}

/**
 * Companion object for the `Hospital` class, providing implicit JSON encoding for hospitals.
 */
object Hospital {
  implicit val eventEncoder: JsonEncoder[Hospital] = DeriveJsonEncoder.gen[Hospital]
}