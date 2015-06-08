package org.inria.scale.streams.windows;

public class WindowConfigurationObject {

	public static final long MINIMUM_TIME_BETWEEN_EXECUTIONS = 100;

	// //////////////////////////////////////////////
	// ******* Window type *******
	// //////////////////////////////////////////////

	public static final String TYPE_TUMBLING = "tumbling";
	public static final String TYPE_SLIDING = "sliding";

	// //////////////////////////////////////////////
	// ******* Tumbling type *******
	// //////////////////////////////////////////////

	public static final String TUMBLING_TIME = "time";
	public static final String TUMBLING_COUNT = "count";

	// //////////////////////////////////////////////
	// ******* Eviction type *******
	// //////////////////////////////////////////////

	public static final String EVICTION_TIME = "time";
	public static final String EVICTION_COUNT = "count";

	// //////////////////////////////////////////////
	// ******* Triggertype *******
	// //////////////////////////////////////////////

	public static final String TRIGGER_TIME = "time";
	public static final String TRIGGER_COUNT = "count";

	private final String type;

	private final String tumblingType;
	private final long milliseconds;
	private final int count;

	private final String evictionType;
	private final long evictionMilliseconds;
	private final int evictionCount;

	private final String triggerType;
	private final long triggerMilliseconds;
	private final int triggerCount;

	public WindowConfigurationObject() {
		// empty constructor to be used by gson
		this(null, null, 0, 0, null, 0, 0, null, 0, 0);
	}

	public WindowConfigurationObject(final String type, final String tumblingType, final long milliseconds,
			final int count, final String evictionType, final long evictionMilliseconds, final int evictionCount,
			final String triggerType, final long triggerMilliseconds, final int triggerCount) {
		this.type = type;
		this.tumblingType = tumblingType;
		this.milliseconds = milliseconds;
		this.count = count;
		this.evictionType = evictionType;
		this.evictionMilliseconds = evictionMilliseconds;
		this.evictionCount = evictionCount;
		this.triggerType = triggerType;
		this.triggerMilliseconds = triggerMilliseconds;
		this.triggerCount = triggerCount;
	}

	public long getTumblingMilliseconds() {
		return milliseconds;
	}

	public String getType() {
		return type;
	}

	public String getTumblingType() {
		return tumblingType;
	}

	public int getTumblingCount() {
		return count;
	}

	public String getEvictionType() {
		return evictionType;
	}

	public long getEvictionMilliseconds() {
		return evictionMilliseconds;
	}

	public int getEvictionCount() {
		return evictionCount;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public long getTriggerMilliseconds() {
		return triggerMilliseconds;
	}

	public int getTriggerCount() {
		return triggerCount;
	}

}
