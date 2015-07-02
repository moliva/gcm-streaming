package org.inria.scale.streams.windows;

import org.inria.scale.streams.base.MultipleSourcesAggregator;

/**
 *
 * Represents the configuration for the {@link MultipleSourcesAggregator} to use
 * when combining batches of tuples.
 *
 * @author moliva
 *
 */
public class CombinatorConfigurationObject {

	private long timeBetweenExecutions;

	public long getTimeBetweenExecutions() {
		return timeBetweenExecutions;
	}

}
