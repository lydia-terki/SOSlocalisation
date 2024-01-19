object HospitalChooser {

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