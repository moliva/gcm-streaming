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

public class SlidingWindowStrategyWithTimeEvictionAndTimeTriggerPoliciesTest extends
		SlidingWindowStrategyIntegrationTest {

	private static final int MILLISECONDS_EVICTION = 500;
	private static final int MILLISECONDS_TRIGGER = MILLISECONDS_EVICTION / 2;

	@Override
	protected WindowConfigurationObject createWindowConfiguration() {
		return aWindowConfiguration().ofSlidingType() //
				.withEvictionPolicy().milliseconds(MILLISECONDS_EVICTION) //
				.withTriggerPolicy().milliseconds(MILLISECONDS_TRIGGER) //
				.build();
	}

	@Test
	public void shouldSlideAndTriggerTuplesAccordinglyWhenAddingNewOnes() throws Exception {
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
		assertThat(queue, contains(tuple1, tuple2, tuple3));

		Thread.sleep(MILLISECONDS_TRIGGER);

		// after this time no tuple is triggered but the window slides
		assertThat(queue, contains(tuple2, tuple3));
	}
}
