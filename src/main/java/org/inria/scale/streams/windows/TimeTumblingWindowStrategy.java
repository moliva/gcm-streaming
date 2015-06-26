package org.inria.scale.streams.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class TimeTumblingWindowStrategy implements WindowStrategy {

	private final long milliseconds;

	private Window window;

	private Timer timer;

	public TimeTumblingWindowStrategy(final long milliseconds) {
		this.milliseconds = milliseconds;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
		initializeTimers();
	}

	private void initializeTimers() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

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
		final Queue<Tuple> tuplesQueue = window.getTuplesQueue();
		final List<Tuple> tuples = new ArrayList<Tuple>(tuplesQueue);
		tuplesQueue.removeAll(tuples);
		window.send(tuples);
	}

	@Override
	public void tearDown() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
