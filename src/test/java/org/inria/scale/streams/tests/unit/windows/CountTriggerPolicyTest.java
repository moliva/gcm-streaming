package org.inria.scale.streams.tests.unit.windows;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.tests.utils.TupleUtils;
import org.inria.scale.streams.windows.policies.CountTriggerPolicy;
import org.inria.scale.streams.windows.policies.TriggerPolicy;
import org.javatuples.Tuple;
import org.junit.Before;
import org.junit.Test;

public class CountTriggerPolicyTest {

	private final Tuple[] tuples = new Tuple[] { TupleUtils.tupleWith(1), TupleUtils.tupleWith(2) };
	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>(Arrays.asList(tuples));

	private final Window window = mock(Window.class);

	private final TriggerPolicy policy = new CountTriggerPolicy(3);

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializePolicy() {
		policy.initialize(window);
	}

	@Test
	public void shouldTriggerWhenCountIsMet() throws Exception {
		final Tuple tuple1 = TupleUtils.tupleWith(1);
		policy.check(tuple1);
		verify(window, never()).send(anyListOf(Tuple.class));

		final Tuple tuple2 = TupleUtils.tupleWith(2);
		policy.check(tuple2);
		verify(window, never()).send(anyListOf(Tuple.class));

		final Tuple tuple3 = TupleUtils.tupleWith(3);
		policy.check(tuple3);
		verify(window).send(listThat(contains(tuples)));
	}

}
