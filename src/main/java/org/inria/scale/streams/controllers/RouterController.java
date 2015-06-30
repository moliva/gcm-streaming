package org.inria.scale.streams.controllers;

import java.io.Serializable;

import org.inria.scale.streams.base.BaseTwoSourcesCombinator;
import org.inria.scale.streams.base.MultipleSourcesCombinator;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.control.AbstractPAController;
import org.objectweb.proactive.core.component.interception.Interceptor;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactoryImpl;
import org.objectweb.proactive.core.mop.MethodCall;

/**
 * 
 * Controller and interceptor for routing tuples from different server
 * interfaces into its own way inside a Combinator operator (see ADL definition)
 * with more than one input source possible.
 * 
 * @see MultipleSourcesCombinator
 * @see BaseTwoSourcesCombinator
 * 
 * @author moliva
 *
 */
public class RouterController extends AbstractPAController implements Interceptor, Serializable {

	private static final long serialVersionUID = 1L;
	private static final String INTERFACE_NAME = "router-controller";

	public RouterController(final Component owner) {
		super(owner);
	}

	@Override
	protected void setControllerItfType() {
		try {
			setItfType(PAGCMTypeFactoryImpl.instance().createFcItfType(INTERFACE_NAME, Interceptor.class.getName(),
					TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE));
		} catch (final InstantiationException e) {
			throw new ProActiveRuntimeException("cannot create controller" + getClass().getName());
		}
	}

	@Override
	public void beforeMethodInvocation(final String interfaceName, final MethodCall methodCall) {
		final int interfaceNumber = parseInterfaceName(interfaceName);
		final Object[] effectiveArguments = methodCall.getEffectiveArguments();

		effectiveArguments[0] = interfaceNumber;
	}

	private int parseInterfaceName(final String interfaceName) {
		try {
			final String lastCharacter = interfaceName.substring(interfaceName.length() - 1);
			return Integer.parseInt(lastCharacter);
		} catch (final NumberFormatException e) {
			// if it's not a number, let's go with the default port
			System.err.println("Warning: Interface " + interfaceName + " from component " + owner
					+ " does not finish in a number. Using default port (i.e. 0) instead");
			return 0;
		}
	}

	@Override
	public void afterMethodInvocation(final String interfaceName, final MethodCall methodCall, final Object result) {
		// nothing to do here!
	}

}
