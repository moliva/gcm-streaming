package org.inria.scale.streams.windows;

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
