package org.inria.scale.streams.tests.builders;

import org.inria.scale.streams.windows.WindowConfigurationObject;

public class WindowConfigurationBuilder {

	private String type;

	private TumblingBuilder tumblingBuilder;
	private SlidingBuilder slidingBuilder;

	// //////////////////////////////////////////////
	// ******* Creation method *******
	// //////////////////////////////////////////////

	public static WindowConfigurationBuilder aWindowConfiguration() {
		return new WindowConfigurationBuilder();
	}

	// //////////////////////////////////////////////
	// ******* Builder method *******
	// //////////////////////////////////////////////

	public WindowConfigurationObject build() {
		String tumblingType = null;
		long tumblingMilliseconds = 0;
		int tumblingCount = 0;

		String slidingEvictionType = null;
		long slidingEvictionMilliseconds = 0;
		int slidingEvictionCount = 0;

		String slidingTriggerType = null;
		long slidingTriggerMilliseconds = 0;
		int slidingTriggerCount = 0;

		if (tumblingBuilder != null) {
			tumblingType = tumblingBuilder.type;
			tumblingMilliseconds = tumblingBuilder.milliseconds;
			tumblingCount = tumblingBuilder.count;
		}

		if (slidingBuilder != null) {
			if (slidingBuilder.evictionBuilder != null) {
				slidingEvictionType = slidingBuilder.evictionBuilder.type;
				slidingEvictionMilliseconds = slidingBuilder.evictionBuilder.milliseconds;
				slidingEvictionCount = slidingBuilder.evictionBuilder.count;
			}

			if (slidingBuilder.triggerBuilder != null) {
				slidingTriggerType = slidingBuilder.triggerBuilder.type;
				slidingTriggerMilliseconds = slidingBuilder.triggerBuilder.milliseconds;
				slidingTriggerCount = slidingBuilder.triggerBuilder.count;
			}
		}

		return new WindowConfigurationObject(type, //
				tumblingType, tumblingMilliseconds, tumblingCount, //
				slidingEvictionType, slidingEvictionMilliseconds, slidingEvictionCount, //
				slidingTriggerType, slidingTriggerMilliseconds, slidingTriggerCount);
	}

	// //////////////////////////////////////////////
	// ******* Setter methods *******
	// //////////////////////////////////////////////

	public TumblingBuilder ofTumblingType() {
		type = WindowConfigurationObject.TYPE_TUMBLING;
		return new TumblingBuilder();
	}

	public class TumblingBuilder {

		private String type;
		private long milliseconds;
		private int count;

		public TumblingBuilder milliseconds(final long milliseconds) {
			this.type = WindowConfigurationObject.TUMBLING_TIME;
			this.milliseconds = milliseconds;
			return this;
		}

		public TumblingBuilder count(final int count) {
			this.type = WindowConfigurationObject.TUMBLING_COUNT;
			this.count = count;
			return this;
		}

		public WindowConfigurationObject build() {
			tumblingBuilder = this;
			return WindowConfigurationBuilder.this.build();
		}
	}

	public SlidingBuilder ofSlidingType() {
		type = WindowConfigurationObject.TYPE_SLIDING;
		return new SlidingBuilder();
	}

	public class SlidingBuilder {

		private TriggerBuilder triggerBuilder;
		private EvictionBuilder evictionBuilder;

		public EvictionBuilder withEvictionPolicy() {
			evictionBuilder = new EvictionBuilder();
			return evictionBuilder;
		}

		public class EvictionBuilder {

			private String type;
			private long milliseconds;
			private int count;

			public SlidingBuilder milliseconds(final long milliseconds) {
				this.type = WindowConfigurationObject.EVICTION_TIME;
				this.milliseconds = milliseconds;
				return SlidingBuilder.this;
			}

			public SlidingBuilder count(final int count) {
				this.type = WindowConfigurationObject.EVICTION_COUNT;
				this.count = count;
				return SlidingBuilder.this;
			}

		}

		public TriggerBuilder withTriggerPolicy() {
			triggerBuilder = new TriggerBuilder();
			return triggerBuilder;
		}

		public class TriggerBuilder {

			private String type;
			private long milliseconds;
			private int count;

			public SlidingBuilder milliseconds(final long milliseconds) {
				this.type = WindowConfigurationObject.TRIGGER_TIME;
				this.milliseconds = milliseconds;
				return SlidingBuilder.this;
			}

			public SlidingBuilder count(final int count) {
				this.type = WindowConfigurationObject.TRIGGER_COUNT;
				this.count = count;
				return SlidingBuilder.this;
			}

		}

		public WindowConfigurationObject build() {
			slidingBuilder = this;
			return WindowConfigurationBuilder.this.build();
		}
	}

}
