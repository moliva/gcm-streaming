package org.inria.scale.streams.windows;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.WindowConfigurationObject.TupleSelection;
import org.javatuples.Tuple;

public class TumblingWindowStrategy implements WindowStrategy {

	@Override
	public void initialize(final Window window) {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				final TupleSelection<Tuple> selection = windowConfigurationJson
						.selectTuples(new ConcurrentLinkedQueue<>(tuples));
				if (selection.shouldTrigger()) {
					tuples = selection.getNewTuples();
					send(new ArrayList<>(selection.getTuplesToProcess()));
				}
			}
		}, windowConfigurationJson.getTimeBetweenExecutions(), windowConfigurationJson.getTimeBetweenExecutions());
		timer.cancel();

	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub

	}

}
