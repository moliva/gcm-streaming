package org.inria.scale.streams.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.configuration.CombinatorConfiguration;
import org.javatuples.Tuple;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public abstract class TwoWayCombinator extends MulticastInStreamBindingController implements InStream,
		CombinatorConfiguration, RunActive {

	private long batchIntervalMilliseconds = 100;

	private final Map<Integer, Queue<Tuple>> tuplesMap = new HashMap<>();

	protected abstract List<? extends Tuple> process(List<Tuple> tuples0, List<Tuple> tuples1);

	public TwoWayCombinator() {
		tuplesMap.put(0, new ConcurrentLinkedQueue<Tuple>());
		tuplesMap.put(1, new ConcurrentLinkedQueue<Tuple>());
	}

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

	public void process() {
		final List<Tuple> tuples0 = removeAllTuples(tuplesMap.get(0));
		final List<Tuple> tuples1 = removeAllTuples(tuplesMap.get(1));

		if (tuples0.isEmpty() || tuples1.isEmpty()) {
			// don't process if one of the two is empty
			return;
		}

		send(process(tuples0, tuples1));
	}

	private List<Tuple> removeAllTuples(final Queue<Tuple> tuples) {
		final List<Tuple> tuplesRemoved = new ArrayList<>(tuples);
		tuples.removeAll(tuplesRemoved);
		return tuplesRemoved;
	}

	// //////////////////////////////////////////////
	// ******* MultipleInStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		if (!tuplesMap.containsKey(inputSource)) {
			tuplesMap.put(inputSource, new ConcurrentLinkedQueue<Tuple>());
		}

		final Queue<Tuple> queue = tuplesMap.get(inputSource);
		queue.addAll(newTuples);
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
