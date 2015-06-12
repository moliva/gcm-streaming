package org.inria.scale.streams.tests.unit.windows;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
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
import org.inria.scale.streams.windows.policies.TimeTriggerPolicy;
import org.inria.scale.streams.windows.policies.TriggerPolicy;
import org.javatuples.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeTriggerPolicyTest {

	private static final long MILLISECONDS_TO_WAIT = 500;
	private static final long MILLISECONDS_EXTRA = MILLISECONDS_TO_WAIT / 2;

	private final Tuple[] tuples = new Tuple[] { tupleWith(1), tupleWith(2) };
	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>(Arrays.asList(tuples));

	private final Window window = mock(Window.class);

	private final TriggerPolicy policy = new TimeTriggerPolicy(MILLISECONDS_TO_WAIT);

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

	@SuppressWarnings("unchecked")
	@Test
	public void shouldTriggerAfterTimeReached() throws Exception {
		verify(window, never()).send(anyListOf(Tuple.class));

		// waiting a window + an interval of confidence to ensure the correct
		// execution of the triggering
		Thread.sleep(MILLISECONDS_TO_WAIT + MILLISECONDS_EXTRA);

		verify(window).send((List<Tuple>) argThat(contains(tuples)));
	}

}
