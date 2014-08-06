package actors

import akka.actor.{Props, Actor}
import org.jboss.netty.channel.Channel

class EchoActor(channel: Channel) extends Actor {

    def receive = {
        case message@_ => channel.write(message)
    }

}

object EchoActor {
    def props(channel: Channel): Props = Props(new EchoActor(channel))
}
