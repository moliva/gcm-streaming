package org.inria.scale.streams.windows;

import static org.inria.scale.streams.windows.WindowConfigurationObject.COUNT;
import static org.inria.scale.streams.windows.WindowConfigurationObject.SLIDING;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TIME;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TUMBLING;

import org.inria.scale.streams.base.ConfigurationParser;

public class WindowStrategyFactory {

	public static WindowStrategy createFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getType()) {
		case TUMBLING:
			switch (windowConfiguration.getTumblingType()) {
			case TIME:
				return new TimeTumblingWindowStrategy(windowConfiguration.getMilliseconds());
			case COUNT:
				return new CountTumblingWindowStrategy(windowConfiguration.getCount());
			default:
				throw new RuntimeException("Tumbling type is not valid");
			}
		case SLIDING:
			return new SlidingWindowStrategy();
		default:
			throw new RuntimeException("Window type is not valid");
		}
	}

	public static WindowStrategy createFrom(final String windowConfigurationJson) {
		final WindowConfigurationObject configuration = new ConfigurationParser()
				.parseWindowConfiguration(windowConfigurationJson);
		return createFrom(configuration);
	}

}
