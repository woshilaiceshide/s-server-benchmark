package woshilaiceshide.sserver.benchmark.rapidoid;

import org.rapidoid.net.*;

public class RapidoidHttpServer {

	public static void main(String[] args) {

		ServerBuilder builder = new ServerBuilder();
		builder.protocol(new SimpleHttpProtocol()).port(8989).workers(2).build().start();
	}

}
