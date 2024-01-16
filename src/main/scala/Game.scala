import zio.json._

import java.time.LocalDate

object GameDates {

  opaque type GameDate = LocalDate

  object GameDate {

    def apply(value: LocalDate): GameDate = value
  }

  implicit val gameDateEncoder: JsonEncoder[GameDate] = JsonEncoder.localDate
}

object SeasonYears {

  opaque type SeasonYear <: Int = Int

  object SeasonYear {

    def apply(year: Int): SeasonYear = year

    def safe(value: Int): Option[SeasonYear] =
      Option.when(value >= 1876 && value <= LocalDate.now.getYear)(value)
  }

  implicit val seasonYearEncoder: JsonEncoder[SeasonYear] = JsonEncoder.int
}

object PlayoffRounds {

  opaque type PlayoffRound <: Int = Int

  object PlayoffRound {

    def apply(round: Int): PlayoffRound = round

    def safe(value: Int): Option[PlayoffRound] =
      Option.when(value >= 1 && value <= 4)(value)
  }

  implicit val playoffRoundEncoder: JsonEncoder[PlayoffRound] = JsonEncoder.int
}

import GameDates.*
import PlayoffRounds.*
import SeasonYears.*

case class Game(
    date: GameDate,
    season: SeasonYear,
    playoffRound: Option[PlayoffRound]
)

object Game {
  implicit val gameEncoder: JsonEncoder[Game] = DeriveJsonEncoder.gen[Game]
}
