package woshilaiceshide.sserver.benchmark.jetty

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.AbstractHandlerContainer;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.http.PreEncodedHttpField;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.BufferUtil;

class PlainTextHandler extends AbstractHandler {

  val helloWorld = BufferUtil.toBuffer("Hello, World!");
  val contentType = new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, MimeTypes.Type.TEXT_PLAIN.asString());
  override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
    if ("/ping".equals(target)) {
      baseRequest.setHandled(true);
      baseRequest.getResponse().getHttpFields().add(contentType);
      baseRequest.getResponse().getHttpOutput().sendContent(helloWorld.slice());
    } else {
      baseRequest.setHandled(true)
      baseRequest.getResponse.setStatus(404)
    }

  }

}

object JettyHttpServer extends App {

  // Insufficient threads: max=2 < needed(acceptors=1 + selectors=2 + request=1)
  val threadPool = new org.eclipse.jetty.util.thread.QueuedThreadPool(4, 2);

  val server = new Server(threadPool)

  val connector = new ServerConnector(server);
  connector.setHost("0.0.0.0");
  connector.setPort(8080);
  server.setConnectors(Array[Connector](connector));

  val config = connector.getBean(classOf[HttpConnectionFactory]).getHttpConfiguration();
  connector.setReuseAddress(true)
  config.setSendDateHeader(true);
  config.setSendServerVersion(true);

  val handler = new PlainTextHandler();
  server.setHandler(handler);

  server.start();
  server.join();

}