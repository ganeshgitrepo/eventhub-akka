package no.kodemaker.event;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import static akka.actor.Actors.actorOf;
import static no.kodemaker.event.Subscriber.Receive;

public class SubscriberMain {
	public static void main(String[] args) {
		final ActorRef logger = actorOf(Logger.class).start();
		ActorRef subscriber = actorOf(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Subscriber("tcp://localhost:5555", new StringFormat(), logger);
			}
		}).start();
		subscriber.sendOneWay(Receive);
	}

	public static class Logger extends UntypedActor {
		@Override
		public void onReceive(Object message) throws Exception {
			log().logger().info("Received " + message);
		}
	}
}
