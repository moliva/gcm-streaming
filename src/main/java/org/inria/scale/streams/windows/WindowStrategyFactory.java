package org.inria.scale.streams.windows;

import static org.inria.scale.streams.windows.WindowConfigurationObject.EVICTION_COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.EVICTION_TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TRIGGER_COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TRIGGER_TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TUMBLING_COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TUMBLING_TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TYPE_SLIDING;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TYPE_TUMBLING;

import org.inria.scale.streams.base.ConfigurationParser;
import org.inria.scale.streams.windows.sliding.CountEvictionPolicy;
import org.inria.scale.streams.windows.sliding.CountTriggerPolicy;
import org.inria.scale.streams.windows.sliding.EvictionPolicy;
import org.inria.scale.streams.windows.sliding.TimeEvictionPolicy;
import org.inria.scale.streams.windows.sliding.TimeTriggerPolicy;
import org.inria.scale.streams.windows.sliding.TriggerPolicy;

public class WindowStrategyFactory {

	public static WindowStrategy createFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getType()) {
		case TYPE_TUMBLING:
			switch (windowConfiguration.getTumblingType()) {
			case TUMBLING_TIME:
				return new TimeTumblingWindowStrategy(windowConfiguration.getTumblingMilliseconds());
			case TUMBLING_COUNT:
				return new CountTumblingWindowStrategy(windowConfiguration.getTumblingCount());
			default:
				throw new RuntimeException("Tumbling type is not valid");
			}
		case TYPE_SLIDING:
			return new SlidingWindowStrategy( //
					createEvictionStrategyFrom(windowConfiguration), //
					createTriggerStrategyFrom(windowConfiguration));
		default:
			throw new RuntimeException("Window type is not valid");
		}
	}

	private static TriggerPolicy createTriggerStrategyFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getTriggerType()) {
		case TRIGGER_TIME:
			return new TimeTriggerPolicy(windowConfiguration.getTriggerMilliseconds());
		case TRIGGER_COUNT:
			return new CountTriggerPolicy(windowConfiguration.getTriggerCount());
		default:
			throw new RuntimeException("Trigger type is not valid");
		}
	}

	private static EvictionPolicy createEvictionStrategyFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getEvictionType()) {
		case EVICTION_TIME:
			return new TimeEvictionPolicy(windowConfiguration.getEvictionMilliseconds());
		case EVICTION_COUNT:
			return new CountEvictionPolicy(windowConfiguration.getEvictionCount());
		default:
			throw new RuntimeException("Eviction type is not valid");
		}
	}

	public static WindowStrategy createFrom(final String windowConfigurationJson) {
		final WindowConfigurationObject configuration = new ConfigurationParser()
				.parseWindowConfiguration(windowConfigurationJson);
		return createFrom(configuration);
	}

}
