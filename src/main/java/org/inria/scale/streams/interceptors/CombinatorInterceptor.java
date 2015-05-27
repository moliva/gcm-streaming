package org.inria.scale.streams.interceptors;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.control.AbstractPAController;
import org.objectweb.proactive.core.component.interception.Interceptor;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactoryImpl;
import org.objectweb.proactive.core.mop.MethodCall;

public class CombinatorInterceptor extends AbstractPAController implements RouterController, Interceptor {

	public CombinatorInterceptor(final Component owner) {
		super(owner);
	}

	public CombinatorInterceptor() {
		super(null);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void beforeMethodInvocation(final String interfaceName, final MethodCall methodCall) {
		System.out.println("before before before");
	}

	@Override
	public void afterMethodInvocation(final String interfaceName, final MethodCall methodCall, final Object result) {
		System.out.println("after afater afaters");
	}

	@Override
	protected void setControllerItfType() {
		try {
			System.out.println("Setting controller interface");
			setItfType(PAGCMTypeFactoryImpl.instance().createFcItfType(RouterController.ROUTER_NAME,
					RouterController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE));
		} catch (final InstantiationException e) {
			throw new ProActiveRuntimeException("cannot create controller " + this.getClass().getName());
		}
	}

}
