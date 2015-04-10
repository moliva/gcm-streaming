package org.inria.scale.streams.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class Count implements InStream, BindingController {

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

		final Map<Tuple, Integer> tokenMap = new HashMap<>();
		for (final Tuple tuple : tuplesToProcess)
			tokenMap.put(tuple, tokenMap.containsKey(tuple) ? tokenMap.get(tuple) + 1 : 1);

		final List<Pair<Object, Integer>> processedTuples = FluentIterable.from(tokenMap.entrySet())
				.transform(new Function<Entry<Tuple, Integer>, Pair<Object, Integer>>() {

					@Override
					public Pair<Object, Integer> apply(final Entry<Tuple, Integer> input) {
						return Pair.with(input.getKey().getValue(0), input.getValue());
					}
				}).toList();

		out.receive(processedTuples);
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
