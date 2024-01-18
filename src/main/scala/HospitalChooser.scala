object HospitalChooser {

  def chooseHospitalForEvents(events: List[Event], hospital: List[Hospital]): List[Hospital] = {
    val hospitalsWithPlace = hospital.filter(_.hasEnoughPlace)
    val hospitalsWithEvents = for {
      event: Event <- events
      hospital: Hospital <- hospitalsWithPlace
      hospitalNearby = getHospitalNearby(hospitalsWithPlace, event)
      newHospital = hospital.addEvent(event)
    } yield newHospital
    hospitalsWithEvents
  }

  private def getHospitalNearby(hospital: List[Hospital], event: Event): Hospital = {
    val hospitalWithDistance = for {
      h <- hospital
      distance = h.getDistance(event)
    } yield (h, distance)
    hospitalWithDistance.minBy(_._2)._1
  }
}