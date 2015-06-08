package org.inria.scale.streams.windows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class CountTumblingWindowStrategy implements WindowStrategy {

	private final int count;

	private Window window;

	public CountTumblingWindowStrategy(final int count) {
		this.count = count;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
	}

	@Override
	public void check(final List<Tuple> tuples) {
		final Queue<Tuple> queue = window.getTuplesQueue();
		for (final Tuple tuple : tuples) {
			queue.add(tuple);
			singleCheck();
		}
	}

	private void singleCheck() {
		final Queue<Tuple> tuplesQueue = window.getTuplesQueue();
		final List<Tuple> selection = selectTuples(new ConcurrentLinkedQueue<>(tuplesQueue));
		if (!selection.isEmpty()) {
			tuplesQueue.removeAll(selection);
			window.send(selection);
		}
	}

	@Override
	public void tearDown() {
		// nothing to do here
	}

	public <T extends Tuple> List<T> selectTuples(final Queue<T> tuples) {
		final int size = tuples.size();
		if (count > size) {
			return Collections.emptyList();
		} else if (count == size) {
			return new ArrayList<T>(tuples);
		} else {
			return new ArrayList<>(tuples).subList(0, count);
		}
	}

}
