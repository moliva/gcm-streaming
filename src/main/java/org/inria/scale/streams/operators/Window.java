package org.inria.scale.streams.operators;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.base.MultiActiveServiceFactory;
import org.inria.scale.streams.base.MultiActiveServiceFactoryImpl;
import org.inria.scale.streams.base.MulticastInStreamBindingController;
import org.inria.scale.streams.configuration.WindowConfiguration;
import org.inria.scale.streams.windows.WindowConfigurationObject;
import org.inria.scale.streams.windows.WindowStrategy;
import org.inria.scale.streams.windows.StaticWindowStrategyFactory;
import org.inria.scale.streams.windows.WindowStrategyFactory;
import org.javatuples.Tuple;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

/**
 * Special kind of Operator which does not produce a transformation on the
 * received tuples but rather is in charge of buffering them and trigger new
 * executions of the following operations in the graph according to a
 * {@link WindowConfiguration}.
 * 
 * @see WindowConfigurationObject
 * @see WindowStrategyFactory
 * @see WindowStrategy
 * 
 * @author moliva
 *
 */
public class Window extends MulticastInStreamBindingController implements InStream, WindowConfiguration, RunActive {

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();
	private final MultiActiveServiceFactory multiActiveServiceFactory;
	private final WindowStrategyFactory windowStrategyFactory;

	private String windowConfigurationJson;
	private WindowStrategy windowStrategy;

	private boolean alreadyRunning = false;

	public Window(final MultiActiveServiceFactory multiActiveServiceFactory,
			final WindowStrategyFactory windowStrategyFactory) {
		this.multiActiveServiceFactory = multiActiveServiceFactory;
		this.windowStrategyFactory = windowStrategyFactory;
	}

	public Window() {
		this(new MultiActiveServiceFactoryImpl(), new StaticWindowStrategyFactory());
	}

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		// the first time we have to initialize the window from here, when all the
		// bindings have been completed by the GCM platform
		windowStrategy.initialize(this);
		alreadyRunning = true;

		final MultiActiveService service = createMultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}

		safelyTearDownWindowStrategy();
	}

	private MultiActiveService createMultiActiveService(final Body body) {
		return multiActiveServiceFactory.createService(body);
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
		windowStrategy.check(newTuples);
	}

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setWindowConfiguration(final String windowConfigurationJson) {
		this.windowConfigurationJson = windowConfigurationJson;

		final WindowStrategy newWindowStrategy = windowStrategyFactory.createFrom(windowConfigurationJson);
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

	// //////////////////////////////////////////////
	// ******* Getters *******
	// //////////////////////////////////////////////

	public Queue<Tuple> getTuplesQueue() {
		return tuples;
	}

}
