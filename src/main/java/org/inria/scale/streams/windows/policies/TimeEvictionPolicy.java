package org.inria.scale.streams.windows.policies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.SlidingWindowStrategy;
import org.javatuples.Tuple;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

/**
 * <p>
 * Eviction policy that stores tuples up to a total of <code>milliseconds</code>
 * before they are effectively dropped.
 * </p>
 * <p>
 * Note that the tuples might be stored for an extra time of up to
 * {@link TimeEvictionPolicy#DEFAULT_INTERVAL} which is the interval in between
 * checks.
 * </p>
 * 
 * @see SlidingWindowStrategy
 * 
 * @author moliva
 *
 */
public class TimeEvictionPolicy implements EvictionPolicy {

	private static final int DEFAULT_INTERVAL = 100;

	private final long milliseconds;

	private Window window;
	private Timer timer;

	private final Map<Tuple, Long> times = new HashMap<>();

	public TimeEvictionPolicy(final long milliseconds) {
		this.milliseconds = milliseconds;
	}

	@Override
	public void initialize(final Window window) {
		this.window = window;
		populateTimesMap();
		initializeTimers();
	}

	private void populateTimesMap() {
		final long currentTime = System.currentTimeMillis();

		for (final Tuple tuple : window.getTuplesQueue()) {
			recordTime(tuple, currentTime);
		}
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
	public void check(final Tuple tuple) {
		final long currentTime = System.currentTimeMillis();

		final Queue<Tuple> queue = window.getTuplesQueue();
		recordTime(tuple, currentTime);

		queue.add(tuple);
	}

	private void recordTime(final Tuple tuple, final long currentTime) {
		times.put(tuple, currentTime);
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
		times.keySet().removeAll(tuplesToRemove);
	}

	@Override
	public void tearDown() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
