package org.inria.scale.streams.tests.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.WindowConfigurationObject;
import org.inria.scale.streams.windows.WindowStrategy;
import org.inria.scale.streams.windows.WindowStrategyFactory;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.After;
import org.junit.Before;

public abstract class SlidingWindowStrategyIntegrationTest {

	protected final Window window = mock(Window.class);
	protected final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>();

	protected WindowStrategy strategy;

	protected abstract WindowConfigurationObject createWindowConfiguration();

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializeStrategy() {
		strategy = WindowStrategyFactory.createFrom(createWindowConfiguration());
		strategy.initialize(window);
	}

	@After
	public void tearDownStrategy() {
		strategy.tearDown();
	}

	// //////////////////////////////////////////////
	// ******* Utils *******
	// //////////////////////////////////////////////

	protected Unit<Integer> createTuple(final int value) {
		return Unit.with(value);
	}

}
