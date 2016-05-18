package woshilaiceshide.sserver.benchmark.akka

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.actor._

import scala.concurrent._

object AkkaHttpServer extends App {

  import com.typesafe.config._
  val s = """
akka {
  loglevel = INFO
  event-handlers = ["akka.event.slf4j.Slf4jEventHandler"]
  log-config-on-start = on
  actor.default-dispatcher {
    "fork-join-executor" : {
      "parallelism-factor" : 1,
      "parallelism-max" : 1,
      "parallelism-min" : 1,

      # Setting this to LIFO changes the fork-join-executor
      # to use a stack discipline for task scheduling. This usually
      # improves throughput at the cost of possibly increasing
      # latency and risking task starvation (which should be rare).
      # but I choose 'FIFO' here.
      "task-peeking-mode" : "FIFO"
    }
  }
  io.tcp.selector-dispatcher = "akka.io.pinned-dispatcher"
  io.tcp.nr-of-selectors = 1
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
  implicit val materializer = ActorMaterializer()

  val serverSource = Http().bind(interface = "localhost", port = 8080)

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`,
        "<html><body>Hello world!</body></html>"))

    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      HttpResponse(entity = "PONG!")

    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
      sys.error("BOOM!")

    case _: HttpRequest =>
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection handleWithSyncHandler requestHandler
      // this is equivalent to
      // connection handleWith { Flow[HttpRequest] map requestHandler }
    }).run()

}