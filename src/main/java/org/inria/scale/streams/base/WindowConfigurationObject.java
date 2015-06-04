package org.inria.scale.streams.base;

import java.util.Collections;
import java.util.List;

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

	public <T extends Tuple> TupleSelection<T> selectTuples(final List<T> tuples) {
		switch (tumblingType) {
		case TIME:
			return new TupleSelection<T>(tuples, Collections.<T> emptyList());
		case COUNT:
			final int size = tuples.size();
			if (count > size) {
				return new TupleSelection<T>(Collections.<T> emptyList(), tuples);
			} else if (count == size) {
				return new TupleSelection<T>(tuples, Collections.<T> emptyList());
			} else {
				return new TupleSelection<T>(tuples.subList(0, count), tuples.subList(count, size));
			}
		default:
			throw new RuntimeException("Parameter not specified for this window");
		}
	}

	public class TupleSelection<T extends Tuple> {

		private final List<T> tuplesToProcess;
		private final List<T> newTuples;

		public TupleSelection(final List<T> tuplesToProcess, final List<T> newTuples) {
			this.tuplesToProcess = tuplesToProcess;
			this.newTuples = newTuples;
		}

		public List<T> getTuplesToProcess() {
			return tuplesToProcess;
		}

		public List<T> getNewTuples() {
			return newTuples;
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
