package org.inria.scale.streams.tests.integration;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.builders.WindowConfigurationBuilder.aWindowConfiguration;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.inria.scale.streams.windows.WindowConfigurationObject;
import org.javatuples.Tuple;
import org.junit.Test;

public class SlidingWindowStrategyWithCountEvictionAndCountTriggerPoliciesTest extends
		SlidingWindowStrategyIntegrationTest {

	private static final long MILLISECONDS_TO_WAIT = 500;

	@Override
	protected WindowConfigurationObject createWindowConfiguration() {
		return aWindowConfiguration().ofSlidingType() //
				.withEvictionPolicy().count(4) //
				.withTriggerPolicy().milliseconds(MILLISECONDS_TO_WAIT) //
				.build();
	}

	@Test
	public void shouldSlideAndTriggerTuplesAccordinglyWhenAddingNewOnes() throws Exception {
		final Tuple tuple1 = tupleWith(1);
		final Tuple tuple2 = tupleWith(2);
		final Tuple tuple3 = tupleWith(3);
		final Tuple tuple4 = tupleWith(4);
		final Tuple tuple5 = tupleWith(5);

		// stores tuples but doesn't trigger as the period hasn't passed
		strategy.check(Arrays.asList(tuple1, tuple2));
		verify(window, never()).send(anyListOf(Tuple.class));
		assertThat(queue, contains(tuple1, tuple2));

		Thread.sleep(MILLISECONDS_TO_WAIT / 2);

		// slides window without triggering
		strategy.check(Arrays.asList(tuple3, tuple4, tuple5));
		verify(window, never()).send(anyListOf(Tuple.class));
		assertThat(queue, contains(tuple2, tuple3, tuple4, tuple5));

		Thread.sleep(MILLISECONDS_TO_WAIT);

		// triggers once after trigger period and queue remains the same
		verify(window).send(listThat(contains(tuple2, tuple3, tuple4, tuple5)));
		assertThat(queue, contains(tuple2, tuple3, tuple4, tuple5));
	}

}
