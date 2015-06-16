package org.inria.scale.streams.tests.unit.windows;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.CountTumblingWindowStrategy;
import org.inria.scale.streams.windows.WindowStrategy;
import org.javatuples.Tuple;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CountTumblingWindowStrategyTest {

	private final Window window = mock(Window.class);
	private final Queue<Tuple> queue = new ConcurrentLinkedQueue<Tuple>();

	private final WindowStrategy strategy = new CountTumblingWindowStrategy(5);

	@Before
	public void setWindowMockUp() {
		when(window.getTuplesQueue()).thenReturn(queue);
	}

	@Before
	public void initializeStrategy() {
		strategy.initialize(window);
	}

	@Test
	public void shouldNotCallTheWindowWhenTheCountIsNotMetAndAddTuplesToQueue() throws Exception {
		final Tuple tuple = tupleWith(1);

		strategy.check(Arrays.asList(tuple));

		verify(window, never()).send(anyListOf(Tuple.class));
		assertThat(queue, contains(tuple));
	}

	@Test
	public void shouldCallTheWindowWhenTheCountIsMetAndEmptyTheQueue() throws Exception {
		final Tuple[] tuples = new Tuple[] { tupleWith(1), tupleWith(2), tupleWith(3), tupleWith(4), tupleWith(5) };

		strategy.check(Arrays.asList(tuples));

		verify(window, times(1)).send(listThat(contains(tuples)));
		assertThat(queue, is(empty()));
	}

	@Test
	public void shouldCallTheWindowWhenTheCountIsMetAndLeaveTheExtraTuplesInTheQueue() throws Exception {
		final Tuple[] tuples = new Tuple[] { tupleWith(1), tupleWith(2), tupleWith(3), tupleWith(4), tupleWith(5) };
		final Tuple extraTuple = tupleWith(6);
		final List<Tuple> tuplesToAdd = Lists.newArrayList(tuples);
		tuplesToAdd.add(extraTuple);

		strategy.check(tuplesToAdd);

		verify(window, times(1)).send(listThat(contains(tuples)));
		assertThat(queue, contains(extraTuple));
	}

}
