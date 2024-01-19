/**
 * The `HospitalChooser` object provides functionality to choose hospitals for events based on pain levels and distances.
 */
object HospitalChooser {

  /**
   * Chooses hospitals for events based on pain levels and distances.
   *
   * @param events    The list of events for which hospitals need to be chosen.
   * @param hospitals The list of hospitals available for selection.
   * @return A list of hospitals with updated event associations.
   */
  def chooseHospitalForEvents(events: List[Event], hospitals: List[Hospital]): List[Hospital] =
    for {
      event: Event <- events
      hospitalNearby = getHospitalNearby(hospitals, event)
      newHospital = hospitalNearby.addEvent(event)

      result =
        if (event.pain > 7) {
          println(s"PAIN LEVEL : ${event.pain}. \nDo not move ! We will send an ambulance to the following position: ${event.longitude} ; ${event.latitude}." +
            s"\nThe nearest hospital is : ${newHospital.hospital_name} at this position : (${newHospital.longitude} ; ${newHospital.longitude})." +
            s"\nThis hospital is specialized in ${newHospital.specialty} and ${newHospital.available_beds} bed(s) is/are available. " +
            s"\nYour profile is : $event. \n")
        }
        else {
          println(s"PAIN LEVEL : ${event.pain}. \nThe nearest hospital is : ${newHospital.hospital_name}.\nYour profile is : $event." +
            s"\nThe nearest hospital is : ${newHospital.hospital_name} and is located at this position : (${newHospital.longitude} ; ${newHospital.longitude})." +
            s"\nThis hospital is specialized in ${newHospital.specialty} and ${newHospital.available_beds} bed(s) is/are available. \n")
        }
    } yield newHospital

  /**
   * Finds the nearest hospital with enough available beds for a given event.
   *
   * @param hospitals The list of hospitals available for selection.
   * @param event     The event for which to find the nearest hospital.
   * @return The nearest hospital with enough available beds for the given event.
   */
  private def getHospitalNearby(hospitals: List[Hospital], event: Event): Hospital = {
    val hospitalWithDistance = for {
      h <- hospitals.filter(_.hasEnoughPlace)
      distance = h.getDistance(event)
      if h.hasEnoughPlace 
      result = h
    } yield (result, distance)
    hospitalWithDistance.minBy(_._2)._1
  }
}