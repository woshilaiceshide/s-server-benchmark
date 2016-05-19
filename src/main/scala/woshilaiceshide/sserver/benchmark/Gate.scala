package woshilaiceshide.sserver.benchmark

object Gate extends App with ServerProperty {

  if (chosen_server == None) {
    ServerProperty.print_help()

  } else {
    chosen_server.get match {
      case "akka" => akka.AkkaHttpServer.main(args)
      case "jetty" => jetty.JettyHttpServer.main(args)
      case "netty" => netty.NettyHttpServer.main(args)
      case "rapidoid" => rapidoid.RapidoidHttpServer.main(args)
      case "sserver" => sserver.SServerHttpServer.main(args)
      case "undertow" => undertow.UndertowHttpServer.main(args)
      case _ => ServerProperty.print_help()
    }
  }

}