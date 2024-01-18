import zio.json.{DeriveJsonEncoder, JsonEncoder}

import java.time.LocalDate

case class Hospital(
                  hospital_name: String,
                  longitude: String,
                  latitude: String,
                  specialty: String,
                  available_beds: Int,
                  events: List[Event] = List.empty
               )

object Hospital {
  implicit val eventEncoder: JsonEncoder[Hospital] = DeriveJsonEncoder.gen[Hospital]
}