package org.inria.scale.streams.exceptions;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.base.MultipleSourcesCombinator;
import org.inria.scale.streams.controllers.RouterController;

/**
 * Class of exceptions for representing errors in the routing of tuples if the
 * application graph.
 * 
 * @see RouterController
 * @see BaseOperator
 * @see MultipleSourcesCombinator
 * 
 * @author moliva
 *
 */
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
