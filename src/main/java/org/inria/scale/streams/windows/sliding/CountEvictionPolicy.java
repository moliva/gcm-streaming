package org.inria.scale.streams.windows.sliding;

import java.util.Queue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.SlidingWindowStrategy;
import org.javatuples.Tuple;

/**
 * Eviction policy that stores up to a number of <code>count</code> tuples in
 * total. Tuples will be dropped in FIFO order as they arrive and the window
 * gets full.
 * 
 * @see SlidingWindowStrategy
 * 
 * @author moliva
 *
 */
public class CountEvictionPolicy implements EvictionPolicy {

	private final int count;

	private Window window;

	public CountEvictionPolicy(final int count) {
		this.count = count;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;

		final Queue<Tuple> queue = window.getTuplesQueue();
		final int originalSize = queue.size();
		for (int i = 0; i < originalSize - count; i++) {
			singleCheck(queue);
		}
	}

	@Override
	public void check(final Tuple tuple) {
		final Queue<Tuple> queue = window.getTuplesQueue();
		queue.add(tuple);

		singleCheck(queue);
	}

	private void singleCheck(final Queue<Tuple> queue) {
		if (queue.size() > count) {
			queue.remove();
		}
	}

	@Override
	public void tearDown() {
		// nothing to do here
	}

}
