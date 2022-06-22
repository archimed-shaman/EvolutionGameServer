package evolution.usecase.server

import java.util.UUID
import fs2.io.net.Socket

private[server] case class Client[F[_]](id: UUID, socket: Socket[F])
