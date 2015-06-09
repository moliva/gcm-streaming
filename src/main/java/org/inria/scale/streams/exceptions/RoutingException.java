package org.inria.scale.streams.exceptions;

public class RoutingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoutingException() {
		// left empty
	}

	public RoutingException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RoutingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RoutingException(final String message) {
		super(message);
	}

	public RoutingException(final Throwable cause) {
		super(cause);
	}

}
