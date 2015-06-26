package org.inria.scale.streams.windows.policies;

import java.util.ArrayList;
import java.util.Queue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.SlidingWindowStrategy;
import org.javatuples.Tuple;

/**
 * Trigger policy that counts the number of received tuples up to a total of
 * <code>count</code> and triggers the execution with the currently stored
 * tuples in the window resetting its internal counter.
 * 
 * @see SlidingWindowStrategy
 * 
 * @author moliva
 *
 */
public class CountTriggerPolicy implements TriggerPolicy {

	private final int count;

	private Window window;
	private int current;

	public CountTriggerPolicy(final int count) {
		this.count = count;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
		this.current = 0;
	}

	@Override
	public void check(final Tuple tuple) {
		final Queue<Tuple> queue = window.getTuplesQueue();

		if (++current >= count) {
			current = 0;
			window.send(new ArrayList<Tuple>(queue));
		}
	}

	@Override
	public void tearDown() {
		// nothing to do here
	}
}
