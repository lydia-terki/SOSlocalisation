import zio.{Scope, ZIO, ZIOApp, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ??? /*{
    // val hospital = HospitalFileStream.callMethod("hopitaux.csv")
    // val events = EventFileStream.parse("events.csv")
    // val hospitalWithEvents = HospitalChooser.affect(hospital, events)
  }*/

}