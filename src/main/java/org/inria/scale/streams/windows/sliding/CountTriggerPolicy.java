package org.inria.scale.streams.windows.sliding;

import java.util.ArrayList;
import java.util.Queue;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

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
			window.send(new ArrayList<>(queue));
		}
	}

	@Override
	public void tearDown() {
		// nothing to do here
	}
}
