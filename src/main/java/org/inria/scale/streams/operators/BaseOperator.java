package org.inria.scale.streams.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InnerProcessor;
import org.inria.scale.streams.MulticastInStream;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public abstract class BaseOperator implements InStream, InnerProcessor, BindingController {

	private MulticastInStream out;

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	protected abstract List<? extends Tuple> processTuples(List<Tuple> tuplesToProcess);

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<Tuple> newTuples) {
		tuples.addAll(newTuples);
	}

	// //////////////////////////////////////////////
	// ******* InnerProcessor *******
	// //////////////////////////////////////////////

	@Override
	public void process() {
		final List<Tuple> tuplesToProcess = new ArrayList<>(tuples);
		tuples.removeAll(tuplesToProcess);

		final List<? extends Tuple> processedTuples = processTuples(tuplesToProcess);

		out.receive((List<Tuple>)processedTuples);
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