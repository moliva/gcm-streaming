package org.inria.scale.streams.windows.sliding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class TimeTriggerStrategy implements TriggerStrategy {

	private final long milliseconds;

	private Timer timer;

	public TimeTriggerStrategy(final long milliseconds) {
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
	public void check(final List<Tuple> tuples) {
		// nothing to do here
	}

	@Override
	public void tearDown() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
