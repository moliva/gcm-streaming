package org.inria.scale.streams.windows;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.javatuples.Tuple;

public class WindowConfigurationObject {

	public static final String TUMBLING = "tumbling";
	public static final String SLIDING = "sliding";

	public static final String TIME = "time";
	public static final String COUNT = "count";

	public static final long MINIMUM_TIME_BETWEEN_EXECUTIONS = 100;

	private final String type;
	private final String tumblingType;
	private final long milliseconds;
	private final int count;

	// //////////////////////////////////////////////
	// ******* Constructors *******
	// //////////////////////////////////////////////

	public WindowConfigurationObject() {
		// empty constructor to be used by gson
		this(null, null, 0, 0);
	}

	public WindowConfigurationObject(final String type, final String tumblingType, final long milliseconds,
			final int count) {
		this.type = type;
		this.tumblingType = tumblingType;
		this.milliseconds = milliseconds;
		this.count = count;
	}

	public long getTimeBetweenExecutions() {
		switch (tumblingType) {
		case TIME:
			return milliseconds;
		case COUNT:
			return MINIMUM_TIME_BETWEEN_EXECUTIONS;
		default:
			throw new RuntimeException("Parameter not specified for this window");
		}
	}

	public boolean isTimeGoverned() {
		switch (tumblingType) {
		case TIME:
			return true;
		case COUNT:
			return false;
		default:
			throw new RuntimeException("Parameter not specified for this window");
		}
	}

	public <T extends Tuple> TupleSelection<T> selectTuples(final Queue<T> tuples) {
		switch (tumblingType) {
		case TIME:
			return new TupleSelection<T>(tuples, new ConcurrentLinkedQueue<T>());
		case COUNT:
			final int size = tuples.size();
			if (count > size) {
				return new TupleSelection<T>(new ConcurrentLinkedQueue<T>(), tuples);
			} else if (count == size) {
				return new TupleSelection<T>(tuples, new ConcurrentLinkedQueue<T>());
			} else {
				return new TupleSelection<T>(slice(tuples, 0, count), slice(tuples, count, size));
			}
		default:
			throw new RuntimeException("Parameter not specified for this window");
		}
	}

	private <T> Queue<T> slice(final Queue<T> tuples, final int fromIndex, final int toIndex) {
		return new ConcurrentLinkedQueue<>(new ArrayList<>(tuples).subList(fromIndex, toIndex));
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
			return WindowConfigurationObject.this.isTimeGoverned() || !tuplesToProcess.isEmpty();
		}
	}

	// //////////////////////////////////////////////
	// ******* Getter methods *******
	// //////////////////////////////////////////////

	public long getMilliseconds() {
		return milliseconds;
	}

	public String getType() {
		return type;
	}

	public String getTumblingType() {
		return tumblingType;
	}

	public int getCount() {
		return count;
	}

}
