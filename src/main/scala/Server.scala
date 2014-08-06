import actors.EchoActor
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.channel._
import akka.actor.ActorSystem

object Server extends App {

    val ServerPort = 8080
    val system = ActorSystem("mySystem")

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
    bootstrap.bind(new InetSocketAddress(ServerPort))
    println(s"Server started. You can telnet to localhost ${ServerPort}")
}

class DiscardServerHandler extends SimpleChannelHandler {

    override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent): Unit = {
        val echoActor = Server.system.actorOf(EchoActor.props(ctx.getChannel))
        echoActor ! e.getMessage
    }

    override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) = {
        e.getCause.printStackTrace()
        val ch = e.getChannel
        ch.close()
    }
}