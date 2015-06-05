package org.inria.scale.streams.windows;

import static org.inria.scale.streams.windows.WindowConfigurationObject.SLIDING;
import static org.inria.scale.streams.windows.WindowConfigurationObject.TUMBLING;

import org.inria.scale.streams.base.ConfigurationParser;

public class WindowStrategyFactory {

	public static WindowStrategy createFrom(final WindowConfigurationObject windowConfiguration) {
		switch (windowConfiguration.getType()) {
		case TUMBLING:
			return new TumblingWindowStrategy( //
					windowConfiguration.getTumblingType(), //
					windowConfiguration.getMilliseconds(), //
					windowConfiguration.getCount());
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
