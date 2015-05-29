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
import org.inria.scale.streams.base.exceptions.RoutingException;
import org.inria.scale.streams.configuration.CombinatorConfiguration;
import org.javatuples.Tuple;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public abstract class MultipleSourcesCombinator extends MulticastInStreamBindingController implements InStream,
		CombinatorConfiguration, RunActive {

	private long batchIntervalMilliseconds = 100;

	private final int inputSourceNumber;
	private final Map<Integer, Queue<Tuple>> tuplesMap = new HashMap<>();

	/**
	 * This is the method to be implemented by any subclass defining a certain
	 * number of input sources. The logic for getting the actual tuples from the
	 * queues and processing them should be implemented here. The API for doing
	 * this currently allows you to call the {@link #removeAllTuples(int)} passing
	 * the input source index as a parameter.
	 * 
	 * @return The list of tuples to be sent to the following operators in the
	 *         application
	 */
	protected abstract List<? extends Tuple> process();

	public MultipleSourcesCombinator(final int inputSourceNumber) {
		this.inputSourceNumber = inputSourceNumber;
		initializeQueues();
	}

	private void initializeQueues() {
		for (int i = 0; i < inputSourceNumber; i++) {
			tuplesMap.put(i, new ConcurrentLinkedQueue<Tuple>());
		}
	}

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				send(process());
			}
		}, batchIntervalMilliseconds, batchIntervalMilliseconds);

		final MultiActiveService service = new MultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}

		timer.cancel();
	}

	/**
	 * Allows the user to get all the tuples that are currently enqueued for a
	 * specific input source while removing them from the queue afterwardss.
	 * 
	 * @param inputSource
	 *          Input source from which to take and remove the tuples
	 * @return The list of tuples enqueued for the specific input source
	 */
	protected List<Tuple> removeAllTuples(final int inputSource) {
		validateInputSource(inputSource);

		final Queue<Tuple> tuples = tuplesMap.get(inputSource);
		final List<Tuple> tuplesRemoved = new ArrayList<>(tuples);
		tuples.removeAll(tuplesRemoved);

		return tuplesRemoved;
	}

	// //////////////////////////////////////////////
	// ******* MultipleInStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		validateInputSource(inputSource);

		final Queue<Tuple> queue = tuplesMap.get(inputSource);
		queue.addAll(newTuples);
	}

	private void validateInputSource(final int inputSource) {
		if (inputSource < inputSourceNumber) {
			throw new RoutingException("invalid input source: " + inputSource
					+ ". this operator can't receive an input higher than " + (inputSourceNumber - 1));
		}
	}

	// //////////////////////////////////////////////
	// ******* CombinatorConfiguration *******
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
