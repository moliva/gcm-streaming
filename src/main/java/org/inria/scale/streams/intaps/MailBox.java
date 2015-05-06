package org.inria.scale.streams.intaps;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.inria.scale.streams.configuration.WindowConfiguration;
import org.inria.scale.streams.operators.BaseOperator;
import org.javatuples.Tuple;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public class MailBox extends BaseOperator implements WindowConfiguration, RunActive {

	private long batchIntervalMilliseconds = 100;

	@Override
	public void runActivity(final Body body) {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				process();
			}
		}, batchIntervalMilliseconds, batchIntervalMilliseconds);

		final MultiActiveService service = new MultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}

		timer.cancel();
	}

	// //////////////////////////////////////////////
	// ******* BaseOperator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return tuplesToProcess;
	}

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setBatchInterval(final long batchIntervalMilliseconds) {
		this.batchIntervalMilliseconds = batchIntervalMilliseconds;
	}

	@Override
	public long getBatchInterval() {
		return batchIntervalMilliseconds;
	}

}
