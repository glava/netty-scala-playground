import java.net.InetSocketAddress
import java.util.concurrent.Executors
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.channel._


object Server extends App {

  val factory =
    new NioServerSocketChannelFactory(
      Executors.newCachedThreadPool(),
      Executors.newCachedThreadPool())

  val bootstrap = new ServerBootstrap(factory)

  bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
    def getPipeline =
      Channels.pipeline(new DiscardServerHandler())

  })

  bootstrap.setOption("child.tcpNoDelay", true)
  bootstrap.setOption("child.keepAlive", true)
  bootstrap.bind(new InetSocketAddress(8080))

}

class DiscardServerHandler extends SimpleChannelHandler {

  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent): Unit = {
    e.getMessage match {
      case buf:ChannelBuffer =>
        while(buf.readable()) {
          println(buf.readByte().toChar)
        }
    }

  }

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) = {
    e.getCause.printStackTrace()
    val ch = e.getChannel
    ch.close()
  }
}