package org.inria.scale.streams.operators;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.base.MulticastInStreamBindingController;
import org.inria.scale.streams.configuration.WindowConfiguration;
import org.inria.scale.streams.windows.WindowStrategy;
import org.inria.scale.streams.windows.WindowStrategyFactory;
import org.javatuples.Tuple;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public class Window extends MulticastInStreamBindingController implements InStream, WindowConfiguration, RunActive {

	private Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	private String windowConfigurationJson;
	private WindowStrategy windowStrategy;

	private boolean alreadyRunning = false;

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		// the first time we have to initialize the window from here, when all the
		// bindings have been completed
		windowStrategy.initialize(this);
		alreadyRunning = true;

		final MultiActiveService service = new MultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}

		safelyTearDownWindowStrategy();
	}

	private void safelyTearDownWindowStrategy() {
		if (windowStrategy != null) {
			windowStrategy.tearDown();
		}
	}

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		tuples.addAll(newTuples);
		windowStrategy.check();
	}

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setWindowConfiguration(final String windowConfigurationJson) {
		this.windowConfigurationJson = windowConfigurationJson;

		final WindowStrategy newWindowStrategy = WindowStrategyFactory.createFrom(windowConfigurationJson);
		safelyTearDownWindowStrategy();
		windowStrategy = newWindowStrategy;

		if (alreadyRunning) {
			windowStrategy.initialize(this);
		}
	}

	@Override
	public String getWindowConfiguration() {
		return windowConfigurationJson;
	}

	public Queue<? extends Tuple> getTuplesQueue() {
		return tuples;
	}

	public void setTuplesQueue(final Queue<Tuple> tuples) {
		this.tuples = tuples;
	}

}
