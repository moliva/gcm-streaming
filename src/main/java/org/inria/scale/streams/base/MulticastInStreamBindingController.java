package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.MulticastInStream;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public abstract class MulticastInStreamBindingController implements BindingController {

	private static final String CLIENT_INTERFACE_NAME = "out";
	private static final int DEFAULT_INPUT_SOURCE = 0;

	private MulticastInStream out;

	@SuppressWarnings("unchecked")
	protected void send(final List<? extends Tuple> tuples) {
		out.receive(DEFAULT_INPUT_SOURCE, (List<Tuple>) tuples);
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		return new String[] { CLIENT_INTERFACE_NAME };
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals(CLIENT_INTERFACE_NAME)) {
			return out;
		}

		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals(CLIENT_INTERFACE_NAME)) {
			out = (MulticastInStream) serverItf;
		}
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (clientItfName.equals(CLIENT_INTERFACE_NAME)) {
			out = null;
		}
	}

}
