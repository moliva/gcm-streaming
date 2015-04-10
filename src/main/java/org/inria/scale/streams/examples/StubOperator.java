package org.inria.scale.streams.examples;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public class StubOperator implements InStream, BindingController {

	@Override
	public void receive(final List<? extends Tuple> tuples) {

	}

	@Override
	public void process() {

	}

	@Override
	public String[] listFc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		// TODO Auto-generated method stub

	}

}
