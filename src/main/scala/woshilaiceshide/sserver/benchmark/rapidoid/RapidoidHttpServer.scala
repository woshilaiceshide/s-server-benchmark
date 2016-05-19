package woshilaiceshide.sserver.benchmark.rapidoid

import org.rapidoid.net.ServerBuilder

object RapidoidHttpServer extends App with woshilaiceshide.sserver.benchmark.ServerProperty {

  val builder = new ServerBuilder();
  builder.protocol(new SimpleHttpProtocol()).address(interface).port(port).workers(2).build().start();

}