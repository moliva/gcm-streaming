/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.component.request;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.body.UniversalBody;
import org.objectweb.proactive.core.body.future.MethodCallResult;
import org.objectweb.proactive.core.body.request.Request;
import org.objectweb.proactive.core.body.request.RequestImpl;
import org.objectweb.proactive.core.body.request.ServeException;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.PAInterface;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.body.ComponentBody;
import org.objectweb.proactive.core.component.body.ComponentBodyImpl;
import org.objectweb.proactive.core.component.control.PAGathercastControllerImpl;
import org.objectweb.proactive.core.component.control.PAInterceptorControllerImpl;
import org.objectweb.proactive.core.component.identity.PAComponentImpl;
import org.objectweb.proactive.core.component.interception.Interceptor;
import org.objectweb.proactive.core.component.representative.ItfID;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.mop.MethodCall;
import org.objectweb.proactive.core.mop.MethodCallExecutionFailedException;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

/**
 * Method calls to components are actually reified calls, and
 * {@link ComponentRequest} contains a reification of the call.
 * <p>
 * This class handles the tagging of the call (a component call), and the
 * redispatching to the targeted component metaobject, interface reference, base
 * object. It also allows pre and post processing of functional invocations with
 * inputInterceptors.
 *
 * @author The ProActive Team
 */
public class ComponentRequestImpl extends RequestImpl implements ComponentRequest, Serializable {
	protected static final Logger logger = ProActiveLogger.getLogger(Loggers.COMPONENTS_REQUESTS);

	// private int shortcutCounter = 0;
	// private Shortcut shortcut;
	private final Class<?> declaringClass;

	// priorities for NF requests (notably when using filters on functional
	// requests) :
	// private short priority=ComponentRequest.STRICT_FIFO_PRIORITY;
	public ComponentRequestImpl(final MethodCall methodCall, final UniversalBody sender, final boolean isOneWay,
			final long nextSequenceID) {
		super(methodCall, sender, isOneWay, nextSequenceID);
		declaringClass = methodCall.getReifiedMethod().getDeclaringClass();
	}

	public ComponentRequestImpl(final Request request) {
		super(request.getMethodCall(), request.getSender(), request.isOneWay(), request.getSequenceNumber());
		declaringClass = methodCall.getReifiedMethod().getDeclaringClass();
	}

	/**
	 * redirects the call to the adequate component metaobject : either to a
	 * controller, through the chain of controllers, to a functional interface in
	 * the case of a composite (no preprocessing in that case), or directly
	 * executes the invocation on the base object if this component is a primitive
	 * component and the invocation is a functional invocation.
	 */
	@Override
	protected MethodCallResult serveInternal(final Body targetBody) throws ServeException {
		Object result = null;
		Throwable exception = null;

		final PAComponentImpl actualComponent = ((ComponentBody) targetBody).getPAComponentImpl();
		if (logger.isDebugEnabled())
			try {
				logger.debug("invocation on method [" + methodCall.getName() + "] of interface ["
						+ methodCall.getComponentMetadata().getComponentInterfaceName() + "] on component : ["
						+ GCM.getNameController(actualComponent).getFcName() + "]");
			} catch (final NoSuchInterfaceException e) {
				e.printStackTrace();
			}

		try {
			if (actualComponent == null)
				throw new ServeException("trying to execute a component method on an object that is not a component");

			final Interface targetItf = (Interface) actualComponent.getFcInterface(methodCall.getComponentMetadata()
					.getComponentInterfaceName());
			final PAGCMInterfaceType itfType = (PAGCMInterfaceType) targetItf.getFcItfType();

			if (isControllerRequest()) {
				// Serving non-functional request
				if (itfType.isGCMGathercastItf()
						&& !getMethodCall().getComponentMetadata().getSenderItfID()
						.equals(new ItfID(itfType.getFcItfName(), targetBody.getID())))
					// delegate to gather controller, except for self requests
					result = ((PAGathercastControllerImpl) ((PAInterface) GCM.getGathercastController(actualComponent))
							.getFcItfImpl()).handleRequestOnGatherItf(this);
				else if (methodCall.getComponentMetadata().getComponentInterfaceName().equals(Constants.ATTRIBUTE_CONTROLLER)) {
					final Object reifiedObject = targetBody.getReifiedObject();
					if (isAnAttribute(methodCall, reifiedObject)) {
						// something something
					} else
						result = methodCall.execute(reifiedObject);
				} else
					result = methodCall.execute(targetItf);
			} else {
				// Serving functional request
				interceptBeforeInvocation(targetBody);

				final String hierarchical_type = actualComponent.getComponentParameters().getHierarchicalType();

				// gather: interception managed with non-transformed incoming requests
				if (itfType.isGCMGathercastItf()
						&& !getMethodCall().getComponentMetadata().getSenderItfID()
						.equals(new ItfID(itfType.getFcItfName(), targetBody.getID())))
					// delegate to gather controller, except for self requests
					result = ((PAGathercastControllerImpl) ((PAInterface) GCM.getGathercastController(actualComponent))
							.getFcItfImpl()).handleRequestOnGatherItf(this);
				else if (hierarchical_type.equals(Constants.COMPOSITE))
					// forward to functional interface whose name is given as a parameter
					// in the method call
					try {
						if (getShortcut() != null) {
							// TODO allow stopping shortcut here
						}
						// executing on connected server interface
						result = methodCall.execute(targetItf);
					} catch (final IllegalArgumentException e) {
						throw new ServeException("could not reify method call : ", e);
					} catch (final Throwable e) {
						e.printStackTrace();
						throw new ServeException("could not reify method call : ", e);
					}
				else {
					// the component is a primitive directly execute the method on the
					// active object
					if (logger.isDebugEnabled())
						if (getShortcutLength() > 0)
							logger.debug("request has crossed " + (getShortcutLength() - 1)
									+ " membranes before reaching a primitive component");
					result = methodCall.execute(targetBody.getReifiedObject());
				}
				interceptAfterInvocation(targetBody, result);
			}
		} catch (final NoSuchInterfaceException nsie) {
			throw new ServeException("cannot serve request : problem accessing a component controller", nsie);
		} catch (final MethodCallExecutionFailedException e) {
			throw new ServeException("serve method " + methodCall.getReifiedMethod().toString() + " failed", e);
		} catch (final java.lang.reflect.InvocationTargetException e) {
			exception = e.getTargetException();
			logger.debug("Serve method " + methodCall.getReifiedMethod().getName() + " failed: ", e);

			if (isOneWay)
				throw new ServeException("serve method " + methodCall.getReifiedMethod().toString() + " failed", exception);
		}

		return new MethodCallResult(result, exception);
	}

	private boolean isAnAttribute(final MethodCall methodCall, final Object reifiedObject) {
		final Field[] fields = reifiedObject.getClass().getDeclaredFields();
		final Optional<String> match = FluentIterable.of(fields).transformAndConcat(new Function<Field, Iterable<String>>() {

			@Override
			public Iterable<String> apply(final Field field) {
				final String fieldName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
				final String getter = "get" + fieldName;
				final String setter = "set" + fieldName;

				final List<String> list = field.isAnnotationPresent(Attribute.class) ? Arrays.asList(getter, setter)
						: Collections.<String> emptyList();
				return list;
			}
		}).firstMatch(new Predicate<String>() {

			@Override
			public boolean apply(final String methodName) {
				return methodName.equals(methodCall.getName());
			}
		});

		System.out.println("method is " + match);

		return false;
	}

	// intercept and delegate for preprocessing from the inputInterceptors
	private void interceptBeforeInvocation(final Body targetBody) {
		if (methodCall.getReifiedMethod() != null)
			try {
				final PAInterceptorControllerImpl interceptorControllerImpl = (PAInterceptorControllerImpl) ((PAInterface) Utils
						.getPAInterceptorController(((ComponentBody) targetBody).getPAComponentImpl())).getFcItfImpl();
				final List<Interceptor> inputInterceptors = interceptorControllerImpl.getInterceptors(methodCall
						.getComponentMetadata().getComponentInterfaceName());
				final Iterator<Interceptor> it = inputInterceptors.iterator();

				while (it.hasNext())
					try {
						it.next().beforeMethodInvocation(methodCall.getComponentMetadata().getComponentInterfaceName(), methodCall);
					} catch (final NullPointerException e) {
						logger.error("could not intercept invocation : " + e.getMessage());
					}
			} catch (final NoSuchInterfaceException nsie) {
				// No PAInterceptorController, nothing to do
			}
	}

	// intercept and delegate for postprocessing from the inputInterceptors
	private void interceptAfterInvocation(final Body targetBody, final Object result) {
		if (methodCall.getReifiedMethod() != null)
			if (((ComponentBody) targetBody).getPAComponentImpl() != null)
				try {
					final PAInterceptorControllerImpl interceptorControllerImpl = (PAInterceptorControllerImpl) ((PAInterface) Utils
							.getPAInterceptorController(((ComponentBody) targetBody).getPAComponentImpl())).getFcItfImpl();
					final List<Interceptor> inputInterceptors = interceptorControllerImpl.getInterceptors(methodCall
							.getComponentMetadata().getComponentInterfaceName());
					// use inputInterceptors in reverse order after invocation
					final ListIterator<Interceptor> it = inputInterceptors.listIterator();

					// go to the end of the list first
					while (it.hasNext())
						it.next();
					while (it.hasPrevious())
						it.previous().afterMethodInvocation(methodCall.getComponentMetadata().getComponentInterfaceName(),
								methodCall, result);
				} catch (final NoSuchInterfaceException nsie) {
					// No PAInterceptorController, nothing to do
				}
	}

	/*
	 * @see org.objectweb.proactive.core.component.request.ComponentRequest#
	 * isControllerRequest()
	 */
	@Override
	public boolean isControllerRequest() {
		// according to the Fractal spec v2.0 , section 4.1
		return Utils.isControllerItfName(methodCall.getComponentMetadata().getComponentInterfaceName());
	}

	/*
	 * @see
	 * org.objectweb.proactive.core.component.request.ComponentRequest#isStopFcRequest
	 * ()
	 */
	@Override
	public boolean isStopFcRequest() {
		return declaringClass.equals(LifeCycleController.class) && "stopFc".equals(getMethodName());
	}

	/*
	 * @see org.objectweb.proactive.core.component.request.ComponentRequest#
	 * isStartFcRequest()
	 */
	@Override
	public boolean isStartFcRequest() {
		return declaringClass.equals(LifeCycleController.class) && "startFc".equals(getMethodName());
	}

	@Override
	public void notifyReception(final UniversalBody bodyReceiver) throws IOException {
		if (getShortcut() != null) {
			if (logger.isDebugEnabled())
				logger.debug("notifying reception of method " + methodCall.getName());
			final Shortcut shortcut = getShortcut();
			shortcut.updateDestination(bodyReceiver.getRemoteAdapter());
			shortcut.getSender().createShortcut(shortcut);

			((ComponentBodyImpl) bodyReceiver).keepShortcut(shortcut);
		}
		super.notifyReception(bodyReceiver);
	}

	@Override
	public void shortcutNotification(final UniversalBody sender, final UniversalBody intermediate) {
		methodCall.getComponentMetadata().shortcutNotification(sender, intermediate);
	}

	@Override
	public Shortcut getShortcut() {
		return methodCall.getComponentMetadata().getShortcut();
	}

	@Override
	public int getShortcutLength() {
		return getShortcut() == null ? 0 : getShortcut().length();
	}

	/*
	 * @see
	 * org.objectweb.proactive.core.component.request.ComponentRequest#getNFPriority
	 * ()
	 */
	@Override
	public short getPriority() {
		return methodCall.getComponentMetadata().getPriority();
	}
}
