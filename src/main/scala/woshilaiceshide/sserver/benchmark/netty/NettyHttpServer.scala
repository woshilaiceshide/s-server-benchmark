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

object NettyHttpServer extends App {

  class HttpHelloWorldServerInitializer extends ChannelInitializer[SocketChannel] {

    override def initChannel(ch: SocketChannel) {
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

    val ch: Channel = b.bind(8383).sync().channel();

    System.err.println("Open your web browser and navigate to http://127.0.0.1:8383/");

    ch.closeFuture().sync();
  } finally {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }

}