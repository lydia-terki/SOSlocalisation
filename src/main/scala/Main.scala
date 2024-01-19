import zio.{ExitCode, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault {
  
  override val run: ZIO[ZIOAppArgs & Scope, Throwable, ExitCode] = for {
      hospitals: List[Hospital] <- HospitalFileStream.generateHospitals()
      events: List[Event] <- EventFileStream.generateEvents()
      hospitalsWithEvent: List[Hospital] = HospitalChooser.chooseHospitalForEvents(events, hospitals)
    } yield ExitCode.success

}