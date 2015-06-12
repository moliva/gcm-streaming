package org.inria.scale.streams.tests.unit.windows;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.sliding.EvictionPolicy;
import org.inria.scale.streams.windows.sliding.TimeEvictionPolicy;
import org.javatuples.Tuple;
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
		final Tuple tuple1 = tupleWith(1);
		policy.check(tuple1);
		assertThat(queue, contains(tuple1));

		Thread.sleep(MILLISECONDS_TO_WAIT / 2);

		// window hasn't slide yet, tuple1 should be still there
		assertThat(queue, contains(tuple1));

		final Tuple tuple2 = tupleWith(2);
		policy.check(tuple2);
		assertThat(queue, contains(tuple1, tuple2));

		Thread.sleep(MILLISECONDS_TO_WAIT);

		// the window should have slided one time
		assertThat(queue, contains(tuple2));

		Thread.sleep(MILLISECONDS_TO_WAIT);

		// the window should have slided yet one more time
		assertThat(queue, is(empty()));
	}

	@Test
	public void shouldEvictTuplesPreviouslyStoredAfterTimePassedWhenEvictionPolicyIsSet() throws Exception {
		// tear down policy, we'll reinitialize it with some preexistent tuples
		policy.tearDown();

		// already existing tuples in queue
		queue.add(tupleWith(1));
		queue.add(tupleWith(2));

		// reinitializing policy
		policy.initialize(window);

		// tuples must be here when policy is just initialized
		assertThat(queue, contains(tupleWith(1), tupleWith(2)));

		Thread.sleep(MILLISECONDS_TO_WAIT * 2);

		// tuples must be evicted after the eviction time (plus an extra) has passed
		assertThat(queue, is(empty()));
	}

}
