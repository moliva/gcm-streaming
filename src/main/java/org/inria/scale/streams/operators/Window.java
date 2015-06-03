package org.inria.scale.streams.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.base.ConfigurationParser;
import org.inria.scale.streams.base.MulticastInStreamBindingController;
import org.inria.scale.streams.base.WindowConfigurationObject;
import org.inria.scale.streams.configuration.WindowConfiguration;
import org.javatuples.Tuple;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public class Window extends MulticastInStreamBindingController implements InStream, WindowConfiguration, RunActive {

	private WindowConfigurationObject windowConfiguration;

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	private Timer timer;

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				send(process());
			}
		}, windowConfiguration.getTimeBetweenExecutions(), windowConfiguration.getTimeBetweenExecutions());

		final MultiActiveService service = new MultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}

		timer.cancel();
	}

	public List<Tuple> process() {
		final List<Tuple> tuplesToSend = new ArrayList<>(tuples);
		tuples.removeAll(tuplesToSend);
		return tuplesToSend;
	}

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		tuples.addAll(newTuples);
	}

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setWindowConfiguration(final String windowConfigurationJson) {
		this.windowConfiguration = new ConfigurationParser().parseWindowConfiguration(windowConfigurationJson);
	}

	@Override
	public String getWindowConfiguration() {
		return new ConfigurationParser().serialize(windowConfiguration);
	}

}
