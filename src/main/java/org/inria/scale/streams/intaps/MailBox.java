package org.inria.scale.streams.intaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.MulticastInStream;
import org.inria.scale.streams.configuration.WindowConfiguration;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public class MailBox implements InStream, BindingController, WindowConfiguration, RunActive {

	private MulticastInStream out;

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	private long batchIntervalMilliseconds = 100;

	@Override
	public void runActivity(final Body body) {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				process();
			}
		}, batchIntervalMilliseconds, batchIntervalMilliseconds);

		final MultiActiveService service = new MultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}

		timer.cancel();
	}

	public void process() {
		final List<Tuple> tuplesToSend = new ArrayList<>(tuples);
		tuples.removeAll(tuplesToSend);
		out.receive(tuplesToSend);
	}

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<Tuple> newTuples) {
		tuples.addAll(newTuples);
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		return new String[] { "out" };
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("out"))
			return out;
		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = (MulticastInStream) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = null;
	}

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setBatchInterval(final long batchIntervalMilliseconds) {
		this.batchIntervalMilliseconds = batchIntervalMilliseconds;
	}

	@Override
	public long getBatchInterval() {
		return batchIntervalMilliseconds;
	}

}
