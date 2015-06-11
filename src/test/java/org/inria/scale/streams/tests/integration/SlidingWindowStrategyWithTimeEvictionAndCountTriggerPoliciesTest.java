package org.inria.scale.streams.tests.integration;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.builders.WindowConfigurationBuilder.aWindowConfiguration;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.windows.WindowConfigurationObject;
import org.javatuples.Tuple;
import org.junit.Test;

public class SlidingWindowStrategyWithTimeEvictionAndCountTriggerPoliciesTest extends
		SlidingWindowStrategyIntegrationTest {

	private static final int MILLISECONDS_TO_WAIT = 500;

	@Override
	protected WindowConfigurationObject createWindowConfiguration() {
		return aWindowConfiguration().ofSlidingType() //
				.withEvictionPolicy().milliseconds(MILLISECONDS_TO_WAIT) //
				.withTriggerPolicy().count(2) //
				.build();
	}

	@Test
	public void shouldSlideAndTriggerTuplesAccordinglyWhenAddingNewOnes() throws Exception {
		final Tuple tuple1 = tupleWith(1);
		final Tuple tuple2 = tupleWith(2);
		final Tuple tuple3 = tupleWith(3);
		final Tuple tuple4 = tupleWith(4);
		final Tuple tuple5 = tupleWith(5);
		final Tuple tuple6 = tupleWith(6);

		// doesn't trigger as policy is count of 2
		strategy.check(Arrays.asList(tuple1));
		verify(window, never()).send(anyListOf(Tuple.class));
		assertThat(queue, contains(tuple1));

		Thread.sleep(MILLISECONDS_TO_WAIT / 2);

		// trigger once as policy is count of 2
		strategy.check(Arrays.asList(tuple2, tuple3));
		verify(window).send((List<? extends Tuple>) argThat(contains(tuple1, tuple2)));
		assertThat(queue, contains(tuple1, tuple2, tuple3));

		Thread.sleep(MILLISECONDS_TO_WAIT * 3 / 4);

		// triggers twice with the tuples stored while dropping tuple1 after sliding
		strategy.check(Arrays.asList(tuple4, tuple5, tuple6));
		verify(window).send((List<? extends Tuple>) argThat(contains(tuple2, tuple3, tuple4)));
		verify(window).send((List<? extends Tuple>) argThat(contains(tuple2, tuple3, tuple4, tuple5, tuple6)));
		assertThat(queue, contains(tuple2, tuple3, tuple4, tuple5, tuple6));
	}
}
