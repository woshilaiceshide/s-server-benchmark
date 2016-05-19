package woshilaiceshide.sserver.benchmark.spray

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import akka.actor._
import spray.can.Http
import spray.can.server.Stats
import spray.util._
import spray.http._
import HttpMethods._
import MediaTypes._

class Handler extends Actor with ActorLogging {

  def receive = {
    case _: Http.Connected => sender() ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      sender() ! new HttpResponse(200, HttpEntity(ContentTypes.`text/plain`, "Hello World"))
  }

}

/**
 * spray is dead. akka-http is rising!
 */
object SprayHttpServer extends App with woshilaiceshide.sserver.benchmark.ServerProperty {

  import com.typesafe.config._
  val s = """
akka {
  loglevel = INFO
  event-handlers = ["akka.event.slf4j.Slf4jEventHandler"]
  log-config-on-start = on
  actor.default-dispatcher {
    "fork-join-executor" : {
      "parallelism-factor" : 1,
      "parallelism-max" : 2,
      "parallelism-min" : 2,

      # Setting this to LIFO changes the fork-join-executor
      # to use a stack discipline for task scheduling. This usually
      # improves throughput at the cost of possibly increasing
      # latency and risking task starvation (which should be rare).
      # but I choose 'FIFO' here.
      "task-peeking-mode" : "FIFO"
    }
  }
  io.tcp.selector-dispatcher = "akka.io.pinned-dispatcher"
  io.tcp.nr-of-selectors = 3
}

# check the reference.conf in spray-can/src/main/resources for all defined settings
spray.can.server {
  # uncomment the next line for making this an HTTPS example
  # ssl-encryption = on
  idle-timeout = 30 s
  request-timeout = 10 s

  request-chunk-aggregation-limit = 0

  parsing.max-content-length = 5g
  parsing.incoming-auto-chunking-threshold-size = 45k
}
    """
  val options = ConfigParseOptions.defaults().setSyntax(ConfigSyntax.CONF)
  val config = ConfigFactory.parseString(s, options)

  implicit val system = ActorSystem("http-server", config)

  val handler = system.actorOf(Props[Handler], name = "handler")

  IO(Http) ! Http.Bind(handler, interface = interface, port = port)

}