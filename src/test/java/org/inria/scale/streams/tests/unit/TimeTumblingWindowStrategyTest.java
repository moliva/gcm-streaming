package org.inria.scale.streams.tests.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.TimeTumblingWindowStrategy;
import org.inria.scale.streams.windows.WindowStrategy;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeTumblingWindowStrategyTest {

	private static final int MILLISECONDS_TRIGGER = 500;

	private final Window window = mock(Window.class);
	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>();

	private final WindowStrategy strategy = new TimeTumblingWindowStrategy(MILLISECONDS_TRIGGER);

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializeStrategy() {
		strategy.initialize(window);
	}

	@After
	public void tearDownStrategy() {
		strategy.tearDown();
	}

	@Test
	public void shouldSlideAndTriggerTuplesAccordinglyWhenTimeIsPassed() throws Exception {
		final Tuple tuple1 = createTuple(1);
		final Tuple tuple2 = createTuple(2);
		final Tuple tuple3 = createTuple(3);

		strategy.check(Arrays.asList(tuple1));
		verify(window, never()).send(anyListOf(Tuple.class));
		assertThat(queue, contains(tuple1));

		Thread.sleep(MILLISECONDS_TRIGGER / 2);

		strategy.check(Arrays.asList(tuple2, tuple3));
		verify(window, never()).send(anyListOf(Tuple.class));
		assertThat(queue, contains(tuple1, tuple2, tuple3));

		Thread.sleep(MILLISECONDS_TRIGGER);

		// after this time we check that it has triggered but tuples are still there
		verify(window).send((List<? extends Tuple>) argThat(contains(tuple1, tuple2, tuple3)));
		assertThat(queue, is(empty()));
	}

	// //////////////////////////////////////////////
	// ******* Utils *******
	// //////////////////////////////////////////////

	private Unit<Integer> createTuple(final int value) {
		return Unit.with(value);
	}
}
