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

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	private String windowConfigurationJson;
	private WindowStrategy windowStrategy;

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
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
		windowStrategy.initialize(this);
	}

	@Override
	public String getWindowConfiguration() {
		return windowConfigurationJson;
	}

}
