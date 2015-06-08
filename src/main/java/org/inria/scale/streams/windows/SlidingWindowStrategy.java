package org.inria.scale.streams.windows;

import java.util.List;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.sliding.EvictionStrategy;
import org.inria.scale.streams.windows.sliding.TriggerStrategy;
import org.javatuples.Tuple;

public class SlidingWindowStrategy implements WindowStrategy {

	private final EvictionStrategy evictionStrategy;
	private final TriggerStrategy triggerStrategy;

	public SlidingWindowStrategy(final EvictionStrategy evictionStrategy, final TriggerStrategy triggerStrategy) {
		this.evictionStrategy = evictionStrategy;
		this.triggerStrategy = triggerStrategy;
	}

	@Override
	public void initialize(final Window window) {
		triggerStrategy.initialize(window);
		evictionStrategy.initialize(window);
	}

	@Override
	public void tearDown() {
		triggerStrategy.tearDown();
		evictionStrategy.tearDown();
	}

	@Override
	public void check(final List<Tuple> tuples) {
		triggerStrategy.check(tuples);
		evictionStrategy.check(tuples);
	}

}
