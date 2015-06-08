package org.inria.scale.streams.windows;

import java.util.List;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.sliding.EvictionPolicy;
import org.inria.scale.streams.windows.sliding.TriggerPolicy;
import org.javatuples.Tuple;

public class SlidingWindowStrategy implements WindowStrategy {

	private final EvictionPolicy evictionPolicy;
	private final TriggerPolicy triggerPolicy;

	public SlidingWindowStrategy(final EvictionPolicy evictionPolicy, final TriggerPolicy triggerPolicy) {
		this.evictionPolicy = evictionPolicy;
		this.triggerPolicy = triggerPolicy;
	}

	@Override
	public void initialize(final Window window) {
		evictionPolicy.initialize(window);
		triggerPolicy.initialize(window);
	}

	@Override
	public void tearDown() {
		evictionPolicy.tearDown();
		triggerPolicy.tearDown();
	}

	@Override
	public void check(final List<Tuple> tuples) {
		for (final Tuple tuple : tuples) {
			evictionPolicy.check(tuple);
			triggerPolicy.check(tuple);
		}
	}

}
