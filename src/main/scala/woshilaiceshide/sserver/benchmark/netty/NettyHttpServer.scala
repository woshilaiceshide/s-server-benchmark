package woshilaiceshide.sserver.benchmark.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.example.http.helloworld.HttpHelloWorldServerHandler
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.channel.socket.nio.NioServerSocketChannel

object NettyHttpServer extends App with woshilaiceshide.sserver.benchmark.ServerProperty {

  class HttpHelloWorldServerInitializer extends ChannelInitializer[SocketChannel] {

    override def initChannel(ch: SocketChannel) {
      //ch.config().setAllowHalfClosure(true)
      val p = ch.pipeline();
      p.addLast(new HttpServerCodec());
      p.addLast(new HttpHelloWorldServerHandler());
    }
  }

  val bossGroup: EventLoopGroup = new NioEventLoopGroup(1);
  val workerGroup: EventLoopGroup = new NioEventLoopGroup(2);
  try {

    val b = new ServerBootstrap();
    b.option[java.lang.Integer](ChannelOption.SO_BACKLOG, 1024);
    b.group(bossGroup, workerGroup).channel(classOf[NioServerSocketChannel]).childHandler(new HttpHelloWorldServerInitializer());

    val ch: Channel = b.bind(interface, port).sync().channel();

    //??? just check for ipv4
    System.err.println(s"Open your web browser and navigate to http://${if ("0.0.0.0" == interface) "127.0.0.1" else interface}:${port}/");

    ch.closeFuture().sync();
  } finally {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }

}