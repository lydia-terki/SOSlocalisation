import zio.json.{DeriveJsonEncoder, JsonEncoder}

import java.time.LocalDate

object EventDates {

  opaque type EventDates = LocalDate

  object EventDates {

    def apply(value: LocalDate): EventDates = value
  }

  implicit val gameDateEncoder: JsonEncoder[EventDates] = JsonEncoder.localDate
}


import EventDates.EventDates

case class Event(
                  date: EventDates,
                  lastname: String,
                  firstname: String,
                  age: Int,
                  latitude: Double,
                  longitude: Double,
                  pain: Int,
                  need: String,
               )

object Event {
  implicit val eventEncoder: JsonEncoder[Event] = DeriveJsonEncoder.gen[Event]
}
