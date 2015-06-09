package org.inria.scale.streams.tests.unit;

import static org.hamcrest.Matchers.contains;
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
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.Before;
import org.junit.Test;

public class CountTriggerPolicyTest {

	private final Tuple[] tuples = new Tuple[] { createTuple(1), createTuple(2) };
	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>(Arrays.asList(tuples));

	private final Window window = mock(Window.class);

	private final CountTriggerPolicy policy = new CountTriggerPolicy(3);

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializeStrategy() {
		policy.initialize(window);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldTriggerWhenCountIsMet() throws Exception {
		final Tuple tuple1 = createTuple(1);
		policy.check(tuple1);
		verify(window, never()).send(anyListOf(Tuple.class));

		final Tuple tuple2 = createTuple(2);
		policy.check(tuple2);
		verify(window, never()).send(anyListOf(Tuple.class));

		final Tuple tuple3 = createTuple(3);
		policy.check(tuple3);
		verify(window).send((List<Tuple>) argThat(contains(tuples)));
	}

	// //////////////////////////////////////////////
	// ******* Utils *******
	// //////////////////////////////////////////////

	private Unit<Integer> createTuple(final int value) {
		return Unit.with(value);
	}
}
