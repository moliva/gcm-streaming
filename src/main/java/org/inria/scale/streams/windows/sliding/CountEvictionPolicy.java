package org.inria.scale.streams.windows.sliding;

import java.util.Queue;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class CountEvictionPolicy implements EvictionPolicy {

	private final int count;

	private Window window;

	public CountEvictionPolicy(final int count) {
		this.count = count;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
	}

	@Override
	public void check(final Tuple tuple) {
		final Queue<Tuple> queue = window.getTuplesQueue();
		queue.add(tuple);

		if (queue.size() > count) {
			queue.remove();
		}
	}

	@Override
	public void tearDown() {
		// nothing to do here
	}

}
