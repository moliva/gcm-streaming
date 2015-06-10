package org.inria.scale.streams.exceptions;

import org.inria.scale.streams.windows.ConfigurationParser;
import org.inria.scale.streams.windows.WindowConfigurationObject;
import org.inria.scale.streams.windows.WindowStrategy;
import org.inria.scale.streams.windows.WindowStrategyFactory;

/**
 * Error while creating a {@link WindowStrategy} out of a
 * {@link WindowConfigurationObject}.
 * 
 * @see WindowStrategyFactory
 * @see WindowConfigurationObject
 * @see ConfigurationParser
 * 
 * @author moliva
 *
 */
public class WindowStrategyCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final WindowConfigurationObject configuration;

	public WindowStrategyCreationException(final String message, final WindowConfigurationObject configuration) {
		super(message);
		this.configuration = configuration;
	}

	public WindowConfigurationObject getConfiguration() {
		return configuration;
	}

}
