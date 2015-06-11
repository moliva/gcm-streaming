package org.inria.scale.streams.windows;

import org.inria.scale.streams.base.MultipleSourcesCombinator;

/**
 *
 * Represents the configuration for the {@link MultipleSourcesCombinator} to use
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
