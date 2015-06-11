package org.inria.scale.streams.tests.unit.windows;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.sliding.CountEvictionPolicy;
import org.inria.scale.streams.windows.sliding.EvictionPolicy;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.Before;
import org.junit.Test;

public class CountEvictionPolicyTest {

	private final Window window = mock(Window.class);
	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>();

	private final EvictionPolicy policy = new CountEvictionPolicy(5);

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializePolicy() {
		policy.initialize(window);
	}

	@Test
	public void shouldAddTuplesWhenCountIsNotReached() throws Exception {
		final Tuple tuple1 = createTuple(1);
		policy.check(tuple1);
		assertThat(queue, contains(tuple1));

		final Tuple tuple2 = createTuple(2);
		policy.check(tuple2);
		assertThat(queue, contains(tuple1, tuple2));
	}

	@Test
	public void shouldSlideAddingNewTuplesAndDiscardingThePreviousWhenCountIsSurpassed() throws Exception {
		final Tuple tuple1 = createTuple(1);
		final Tuple tuple2 = createTuple(2);
		final Tuple tuple3 = createTuple(3);
		final Tuple tuple4 = createTuple(4);
		final Tuple tuple5 = createTuple(5);
		queue.addAll(Arrays.asList(tuple1, tuple2, tuple3, tuple4, tuple5));

		final Tuple extraTuple = createTuple(6);
		policy.check(extraTuple);

		assertThat(queue, not(contains(tuple1)));
		assertThat(queue, contains(tuple2, tuple3, tuple4, tuple5, extraTuple));
	}

	// //////////////////////////////////////////////
	// ******* Utils *******
	// //////////////////////////////////////////////

	private Unit<Integer> createTuple(final int value) {
		return Unit.with(value);
	}
}
