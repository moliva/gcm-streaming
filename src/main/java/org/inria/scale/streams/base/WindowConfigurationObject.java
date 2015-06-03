package org.inria.scale.streams.base;

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

	public WindowConfigurationObject() {
		// empty constructor to be used by gson
		this(null, null, 0);
	}

	public WindowConfigurationObject(final String type, final String tumblingType, final long milliseconds) {
		this.type = type;
		this.tumblingType = tumblingType;
		this.milliseconds = milliseconds;
	}

	public long getMilliseconds() {
		return milliseconds;
	}

	public String getType() {
		return type;
	}

	public String getTumblingType() {
		return tumblingType;
	}

	public long getTimeBetweenExecutions() {
		switch (tumblingType) {
		case TIME:
			return milliseconds;
		default:
			return MINIMUM_TIME_BETWEEN_EXECUTIONS;
		}
	}

	public TupleSelection selectTuples(final List<? extends Tuple> tuples) {
		return new TupleSelection();
	}

	class TupleSelection {

	}

}
