package woshilaiceshide.sserver.benchmark.undertow

import io.undertow.server._

object UndertowHttpServer extends App with woshilaiceshide.sserver.benchmark.ServerProperty {

  val handler = new io.undertow.server.HttpHandler() {
    override def handleRequest(exchange: HttpServerExchange) {
      exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "text/plain");
      exchange.getResponseSender().send("Hello World");
    }
  }

  val server = io.undertow.Undertow
    .builder()
    .addHttpListener(port, interface)
    .setHandler(handler)
    //all the incoming requests' hashcodos maybe even sometimes.
    //for more inforamtion, see 'org.xnio.nio.QueuedNioTcpServer.handleReady()'
    .setIoThreads(4)
    .setWorkerThreads(2)
    .setServerOption[java.lang.Boolean](org.xnio.Options.TCP_NODELAY, true)
    .build();

  server.start();

}