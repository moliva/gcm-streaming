package org.inria.scale.streams.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class TextNormalizer implements InStream, BindingController {

	private InStream out;

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<? extends Tuple> newTuples) {
		tuples.addAll(newTuples);
	}

	@Override
	public void process() {
		final List<Tuple> tuplesToProcess = new ArrayList<>(tuples);
		tuples.removeAll(tuplesToProcess);

		final List<Tuple> processedTuples = FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

			@Override
			public Tuple apply(final Tuple tuple) {
				return normalizeTuple(tuple);
			}
		}).toList();

		out.receive(processedTuples);
	}

	private Unit<?> normalizeTuple(final Tuple tuple) {
		final List<Object> list = FluentIterable.from(tuple).transform(new Function<Object, Object>() {

			@Override
			public Object apply(final Object input) {
				if (input instanceof String)
					return ((String) input).trim().replaceAll("[,.;:]+", "").toLowerCase();
				else
					return input;
			}
		}).toList();
		return Unit.fromIterable(list);
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
			out = (InStream) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = null;
	}

}
