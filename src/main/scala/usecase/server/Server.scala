package evolution.usecase.server

import cats.effect.Concurrent
import cats.effect.std.UUIDGen
import cats.syntax.all.given
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import fs2.Stream
import fs2.*
import fs2.io.net.Network
import fs2.io.net.Socket
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class Server[F[_]: Concurrent: Network: UUIDGen](using logger: Logger[F]) {
  def listenAndServe(addr: Host, port: Port, maxActiveClients: Int): F[Unit] =
    Network[F]
      .server(address = addr.some, port = port.some)
      .map(handleConnection)
      .parJoin(maxActiveClients) // or use parJoinUnbounded for unlimited connections
      .compile
      .drain

  private def handleConnection(clientSock: Socket[F]): fs2.Stream[F, Unit] =
    Stream
      .bracket(for {
        client <- mkClient(clientSock)
        addr   <- clientSock.remoteAddress
        _      <- logger.info(s"New client ${client.id} from ${addr}")
      } yield client)(client => logger.info(s"Unregistered client ${client.id}"))
      .flatMap(handleClient)
      .scope

  private def handleClient(client: Client[F]): fs2.Stream[F, Unit] =
    client.socket.reads
      .through(text.utf8.decode)
      .through(text.lines)
      .interleave(Stream.constant("\n"))
      .through(text.utf8.encode)
      .through(client.socket.writes)
      .handleErrorWith(_ => Stream.empty) // handle errors of client sockets

  private def mkClient(socket: Socket[F]): F[Client[F]] = for {
    uuid <- UUIDGen[F].randomUUID
  } yield Client(uuid, socket)
}
