package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.MulticastInStream;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public abstract class BaseOperator implements InStream, BindingController {

	private MulticastInStream out;

	protected abstract List<? extends Tuple> processTuples(List<Tuple> tuplesToProcess);

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	@Override
	public void receive(final List<Tuple> newTuples) {
		final List<? extends Tuple> processedTuples = processTuples(newTuples);
		out.receive((List<Tuple>) processedTuples);
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		return new String[] { "out" };
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("out"))
			return out;
		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = (MulticastInStream) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = null;
	}

}
