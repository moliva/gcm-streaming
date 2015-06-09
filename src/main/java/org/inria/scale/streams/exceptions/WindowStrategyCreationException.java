package org.inria.scale.streams.exceptions;

import org.inria.scale.streams.windows.WindowConfigurationObject;

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
