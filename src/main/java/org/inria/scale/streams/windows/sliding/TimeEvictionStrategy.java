package org.inria.scale.streams.windows.sliding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class TimeEvictionStrategy implements EvictionStrategy {

	private static final int DEFAULT_INTERVAL = 100;

	private final long milliseconds;

	private Window window;
	private Timer timer;

	private final Map<Tuple, Long> times = new HashMap<>();

	public TimeEvictionStrategy(final long milliseconds) {
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
		}, DEFAULT_INTERVAL, DEFAULT_INTERVAL);
	}

	@Override
	public void check(final List<Tuple> tuples) {
		final long currentTime = System.currentTimeMillis();

		final Queue<Tuple> queue = window.getTuplesQueue();
		for (final Tuple tuple : tuples) {
			times.put(tuple, currentTime);
		}
		queue.addAll(tuples);
	}

	private void singleCheck() {
		final long currentTime = System.currentTimeMillis();

		final Queue<Tuple> tuplesQueue = window.getTuplesQueue();
		final List<Tuple> tuplesToRemove = FluentIterable.from(new ArrayList<>(tuplesQueue)).filter(new Predicate<Tuple>() {

			@Override
			public boolean apply(final Tuple tuple) {
				return currentTime - times.get(tuple) > milliseconds;
			}
		}).toList();

		tuplesQueue.removeAll(tuplesToRemove);
	}

	@Override
	public void tearDown() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
