object HospitalChooser {

  def chooseHospitalForEvents(events: List[Event], hospitals: List[Hospital]): List[Hospital] =
    for {
      event: Event <- events
      hospitalNearby = getHospitalNearby(hospitals, event)
      newHospital = hospitalNearby.addEvent(event)
      _ = println(s"Event is added to hospital ${newHospital.hospital_name} \n Event: $event \n Hospital: $newHospital")
    } yield newHospital

  private def getHospitalNearby(hospital: List[Hospital], event: Event): Hospital = {
    val hospitalWithDistance = for {
      h <- hospital.filter(_.hasEnoughPlace)
      distance = h.getDistance(event)
      if h.hasEnoughPlace 
      result = h
    } yield (result, distance)
    hospitalWithDistance.minBy(_._2)._1
  }
}