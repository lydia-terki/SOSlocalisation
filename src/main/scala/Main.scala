import zio.{ExitCode, Scope, ZIO, ZIOApp, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault {
  
  override val run: ZIO[Any, Throwable, ExitCode] =
    for {
      x <- ZIO.succeed()
      hospitals <- // generateHospital 
      events <- // generateEvents
    } yield ExitCode.success

}