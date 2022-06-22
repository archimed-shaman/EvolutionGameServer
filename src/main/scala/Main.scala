package evolution

import cats.data.Validated.*
import cats.data.ValidatedNel
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.implicits.given
import cats.syntax.all.given
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import evolution.usecase.server.Server
import org.typelevel.log4cats.*
import org.typelevel.log4cats.slf4j.*

case class RunArgs(host: Host, port: Port)

object App extends IOApp {

  given LoggerFactory[IO]             = Slf4jFactory[IO]
  given SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  def run(args: List[String]): IO[ExitCode] = {

    val runArgs: ValidatedNel[String, RunArgs] = (
      Host.fromString("0.0.0.0").toValidNel("bad host"),
      Port.fromInt(8080).toValidNel("bad port")
    ).mapN(RunArgs.apply)

    runArgs match
      case Valid(args) =>
        val server = new Server[IO]
        server
          .listenAndServe(args.host, args.port, 10)
          .as(ExitCode.Success)

      case Invalid(errs) =>
        IO(System.err.println(s"""Wrong args: ${errs.toList.mkString(", ")}""")).as(ExitCode.Error)

  }
}
