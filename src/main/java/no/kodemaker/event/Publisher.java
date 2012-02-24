package no.kodemaker.event;

import akka.actor.UntypedActor;
import org.zeromq.ZMQ;

public class Publisher extends UntypedActor {
	private final String endpoint;
	private final Writer<Object> writer;
	private ZMQ.Context context;
	private ZMQ.Socket socket;

	protected Publisher(String endpoint, Writer<Object> writer) {
		this.endpoint = endpoint;
		this.writer = writer;
	}

	@Override
	public void preStart() {
		context = ZMQ.context(5);
		socket = context.socket(ZMQ.PUB);
		socket.bind(endpoint);
	}

	@Override
	public void postStop() {
		socket.close();
		context.term();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		socket.send(writer.write(message), 0);
	}
}
