package org.inria.scale.streams.tests.unit.windows;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.inria.scale.streams.windows.SlidingWindowStrategy;
import org.inria.scale.streams.windows.WindowStrategy;
import org.inria.scale.streams.windows.sliding.EvictionPolicy;
import org.inria.scale.streams.windows.sliding.TriggerPolicy;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.Test;
import org.mockito.InOrder;

public class SlidingWindowStrategyTest {

	private final EvictionPolicy evictionPolicy = mock(EvictionPolicy.class);
	private final TriggerPolicy triggerPolicy = mock(TriggerPolicy.class);

	private final WindowStrategy strategy = new SlidingWindowStrategy(evictionPolicy, triggerPolicy);

	@Test
	public void shouldCallTheEvictionStrategyFirstWhenChecking() throws Exception {
		final Tuple tuple = Unit.with(1);

		strategy.check(Arrays.asList(tuple));

		final InOrder inOrder = inOrder(evictionPolicy, triggerPolicy);
		inOrder.verify(evictionPolicy).check(tuple);
		inOrder.verify(triggerPolicy).check(tuple);
	}
}
