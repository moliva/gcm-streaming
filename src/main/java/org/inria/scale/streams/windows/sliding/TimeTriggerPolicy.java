package org.inria.scale.streams.windows.sliding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.SlidingWindowStrategy;
import org.javatuples.Tuple;

/**
 * Trigger policy that triggers the execution of the tuples every
 * <code>milliseconds</code> defined by the user with the current queue stored
 * in the window.
 * 
 * @see SlidingWindowStrategy
 * 
 * @author moliva
 *
 */
public class TimeTriggerPolicy implements TriggerPolicy {

	private final long milliseconds;

	private Timer timer;

	public TimeTriggerPolicy(final long milliseconds) {
		this.milliseconds = milliseconds;
	}

	@Override
	public void initialize(final Window window) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				window.send(new ArrayList<>(window.getTuplesQueue()));
			}
		}, milliseconds, milliseconds);
	}

	@Override
	public void check(final Tuple tuple) {
		// nothing to do here
	}

	@Override
	public void tearDown() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
