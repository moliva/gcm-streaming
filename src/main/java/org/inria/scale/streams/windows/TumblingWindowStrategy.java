package org.inria.scale.streams.windows;

import static org.inria.scale.streams.windows.WindowConfigurationObject.COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.MINIMUM_TIME_BETWEEN_EXECUTIONS;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TIME;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class TumblingWindowStrategy implements WindowStrategy {

	private final String type;
	private final long milliseconds;
	private final int count;

	private Window window;

	private Timer triggerTimer;

	public TumblingWindowStrategy(final String type, final long milliseconds, final int count) {
		this.type = type;
		this.milliseconds = milliseconds;
		this.count = count;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
		initializeTimers();
	}

	private void initializeTimers() {
		if (TIME.equals(type)) {
			triggerTimer = new Timer();
			triggerTimer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					singleCheck();
				}
			}, getTimeBetweenExecutions(), getTimeBetweenExecutions());
		}
	}

	@Override
	public void check(final List<Tuple> tuples) {
		final Queue<Tuple> queue = window.getTuplesQueue();
		if (TIME.equals(type)) {
			queue.addAll(tuples);
		} else {
			for (final Tuple tuple : tuples) {
				queue.add(tuple);
				singleCheck();
			}
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
		if (triggerTimer != null) {
			triggerTimer.cancel();
		}
	}

	public <T extends Tuple> TupleSelection<T> selectTuples(final Queue<T> tuples) {
		switch (type) {
		case TIME:
			return new TupleSelection<T>(tuples, new ConcurrentLinkedQueue<T>());
		case COUNT:
			final int size = tuples.size();
			if (count > size) {
				return new TupleSelection<T>(new ConcurrentLinkedQueue<T>(), tuples);
			} else if (count == size) {
				return new TupleSelection<T>(tuples, new ConcurrentLinkedQueue<T>());
			} else {
				return new TupleSelection<T>(slice(tuples, 0, count), slice(tuples, count, size));
			}
		default:
			throw new RuntimeException("Parameter not specified for this window");
		}
	}

	public long getTimeBetweenExecutions() {
		switch (type) {
		case TIME:
			return milliseconds;
		case COUNT:
			return MINIMUM_TIME_BETWEEN_EXECUTIONS;
		default:
			throw new RuntimeException("Parameter not specified for this window");
		}
	}

	private <T> Queue<T> slice(final Queue<T> tuples, final int fromIndex, final int toIndex) {
		return new ConcurrentLinkedQueue<>(new ArrayList<>(tuples).subList(fromIndex, toIndex));
	}

	public boolean isTimeGoverned() {
		return TIME.equals(type);
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
			return isTimeGoverned() || !tuplesToProcess.isEmpty();
		}
	}

}
