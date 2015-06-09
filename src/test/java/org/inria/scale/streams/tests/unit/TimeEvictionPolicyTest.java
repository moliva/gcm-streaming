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
import org.inria.scale.streams.windows.sliding.CountTriggerPolicy;
import org.inria.scale.streams.windows.sliding.EvictionPolicy;
import org.inria.scale.streams.windows.sliding.TimeEvictionPolicy;
import org.inria.scale.streams.windows.sliding.TriggerPolicy;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeEvictionPolicyTest {

	private static final long MILLISECONDS_TO_WAIT = 1000;

	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>();

	private final Window window = mock(Window.class);

	private final EvictionPolicy policy = new TimeEvictionPolicy(MILLISECONDS_TO_WAIT);

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializePolicy() {
		policy.initialize(window);
	}

	@After
	public void tearDownPolicy() {
		policy.tearDown();
	}

	@Test
	public void shouldStoreTuplesWhenAddedAndEvictThemWhenTimeIsReached() throws Exception {
		final Tuple tuple1 = createTuple(1);
		policy.check(tuple1);
		assertThat(queue, contains(tuple1));

		Thread.sleep(MILLISECONDS_TO_WAIT / 2);

		assertThat(queue, contains(tuple1));

		final Tuple tuple2 = createTuple(2);
		policy.check(tuple2);
		assertThat(queue, contains(tuple1, tuple2));

		Thread.sleep(MILLISECONDS_TO_WAIT);

		assertThat(queue, contains(tuple2));

		Thread.sleep(MILLISECONDS_TO_WAIT);

		assertThat(queue, is(empty()));
	}

	// //////////////////////////////////////////////
	// ******* Utils *******
	// //////////////////////////////////////////////

	private Unit<Integer> createTuple(final int value) {
		return Unit.with(value);
	}
}
