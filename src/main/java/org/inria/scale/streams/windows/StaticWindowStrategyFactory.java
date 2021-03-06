package org.inria.scale.streams.windows;

import static org.inria.scale.streams.windows.WindowConfigurationObject.EVICTION_COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.EVICTION_TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TRIGGER_COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TRIGGER_TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TUMBLING_COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TUMBLING_TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TYPE_SLIDING;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TYPE_TUMBLING;

import org.inria.scale.streams.exceptions.WindowStrategyCreationException;
import org.inria.scale.streams.windows.policies.CountEvictionPolicy;
import org.inria.scale.streams.windows.policies.CountTriggerPolicy;
import org.inria.scale.streams.windows.policies.EvictionPolicy;
import org.inria.scale.streams.windows.policies.TimeEvictionPolicy;
import org.inria.scale.streams.windows.policies.TimeTriggerPolicy;
import org.inria.scale.streams.windows.policies.TriggerPolicy;

/**
 * Current implementation of the {@link WindowStrategyFactory} defined
 * statically by the existent window types and their configurations.
 * 
 * @author moliva
 *
 */
public class StaticWindowStrategyFactory implements WindowStrategyFactory {

	@Override
	public WindowStrategy createFrom(final String windowConfigurationJson) {
		final WindowConfigurationObject configuration = new ConfigurationParser()
				.parseWindowConfiguration(windowConfigurationJson);
		return createFrom(configuration);
	}

	@Override
	public WindowStrategy createFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getType()) {
		case TYPE_TUMBLING:
			switch (windowConfiguration.getTumblingType()) {
			case TUMBLING_TIME:
				return new TimeTumblingWindowStrategy(windowConfiguration.getTumblingMilliseconds());
			case TUMBLING_COUNT:
				return new CountTumblingWindowStrategy(windowConfiguration.getTumblingCount());
			default:
				throw new WindowStrategyCreationException("Tumbling type is not valid", windowConfiguration);
			}
		case TYPE_SLIDING:
			return new SlidingWindowStrategy( //
					createEvictionStrategyFrom(windowConfiguration), //
					createTriggerStrategyFrom(windowConfiguration));
		default:
			throw new WindowStrategyCreationException("Window type is not valid", windowConfiguration);
		}
	}

	private TriggerPolicy createTriggerStrategyFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getTriggerType()) {
		case TRIGGER_TIME:
			return new TimeTriggerPolicy(windowConfiguration.getTriggerMilliseconds());
		case TRIGGER_COUNT:
			return new CountTriggerPolicy(windowConfiguration.getTriggerCount());
		default:
			throw new WindowStrategyCreationException("Trigger type is not valid", windowConfiguration);
		}
	}

	private EvictionPolicy createEvictionStrategyFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getEvictionType()) {
		case EVICTION_TIME:
			return new TimeEvictionPolicy(windowConfiguration.getEvictionMilliseconds());
		case EVICTION_COUNT:
			return new CountEvictionPolicy(windowConfiguration.getEvictionCount());
		default:
			throw new WindowStrategyCreationException("Eviction type is not valid", windowConfiguration);
		}
	}

}
