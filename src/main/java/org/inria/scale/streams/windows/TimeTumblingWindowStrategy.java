package org.inria.scale.streams.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class TimeTumblingWindowStrategy implements WindowStrategy {

	private final long milliseconds;

	private Window window;

	private Timer triggerTimer;

	public TimeTumblingWindowStrategy(final long milliseconds) {
		this.milliseconds = milliseconds;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
		initializeTimers();
	}

	private void initializeTimers() {
		triggerTimer = new Timer();
		triggerTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				singleCheck();
			}
		}, milliseconds, milliseconds);
	}

	@Override
	public void check(final List<Tuple> tuples) {
		final Queue<Tuple> queue = window.getTuplesQueue();
		queue.addAll(tuples);
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
		return new TupleSelection<T>(tuples, new ConcurrentLinkedQueue<T>());
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
			return true;
		}
	}

}
