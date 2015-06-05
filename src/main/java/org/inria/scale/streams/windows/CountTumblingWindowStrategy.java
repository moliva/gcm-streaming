package org.inria.scale.streams.windows;

import java.util.ArrayList;
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
		final TupleSelection<Tuple> selection = selectTuples(new ConcurrentLinkedQueue<>(window.getTuplesQueue()));
		if (selection.shouldTrigger()) {
			window.setTuplesQueue(selection.getNewTuples());
			window.send(new ArrayList<>(selection.getTuplesToProcess()));
		}
	}

	@Override
	public void tearDown() {
		// nothing to do here
	}

	public <T extends Tuple> TupleSelection<T> selectTuples(final Queue<T> tuples) {
		final int size = tuples.size();
		if (count > size) {
			return new TupleSelection<T>(new ConcurrentLinkedQueue<T>(), tuples);
		} else if (count == size) {
			return new TupleSelection<T>(tuples, new ConcurrentLinkedQueue<T>());
		} else {
			return new TupleSelection<T>(slice(tuples, 0, count), slice(tuples, count, size));
		}
	}

	private <T> Queue<T> slice(final Queue<T> tuples, final int fromIndex, final int toIndex) {
		return new ConcurrentLinkedQueue<>(new ArrayList<>(tuples).subList(fromIndex, toIndex));
	}

	public class TupleSelection<T extends Tuple> {

		private final Queue<T> tuplesToProcess;
		private final Queue<T> newTuples;

		public TupleSelection(final Queue<T> tuplesToProcess, final Queue<T> newTuples) {
			this.tuplesToProcess = tuplesToProcess;
			this.newTuples = newTuples;
		}

		public Queue<T> getTuplesToProcess() {
			return tuplesToProcess;
		}

		public Queue<T> getNewTuples() {
			return newTuples;
		}

		public boolean shouldTrigger() {
			return !tuplesToProcess.isEmpty();
		}
	}

}
