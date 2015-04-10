package org.inria.scale.streams.examples;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;

import java.util.ArrayList;
import java.util.Collections;
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

public class Tokenizer implements InStream, BindingController {

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

		final List<Unit<String>> processedTuples = FluentIterable.from(tuplesToProcess)
				.transformAndConcat(new Function<Tuple, Iterable<Unit<String>>>() {

					@Override
					public List<Unit<String>> apply(final Tuple tuple) {
						return tokenizeLine(tuple);
					}
				}).toList();

		out.receive(processedTuples);
	}

	private List<Unit<String>> tokenizeLine(final Tuple tuple) {
		return FluentIterable.from(tuple).transformAndConcat(new Function<Object, Iterable<Unit<String>>>() {

			@Override
			public Iterable<Unit<String>> apply(final Object input) {
				if (input instanceof String) {
					final String[] strings = ((String) input).split("\\s+");
					return FluentIterable.of(strings).filter(not(equalTo(""))).transform(wrapIntoTuple());
				} else
					return Collections.emptyList();
			}

		}).toList();
	}

	private Function<String, Unit<String>> wrapIntoTuple() {
		return new Function<String, Unit<String>>() {

			@Override
			public Unit<String> apply(final String string) {
				return Unit.with(string);
			}
		};
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
