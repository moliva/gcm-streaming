package org.inria.scale.streams.examples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.configuration.SortByConfiguration;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

import com.google.common.collect.FluentIterable;

public class SortBy implements InStream, BindingController, SortByConfiguration {

	private static final String DESCENDING_ORDER = "desc";

	private InStream out;
	private int position;
	private String order;

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

		final List<Tuple> processedTuples = FluentIterable.from(tuplesToProcess).toSortedList(new Comparator<Tuple>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Tuple tuple1, final Tuple tuple2) {
				return order(((Comparable<Object>) tuple1.getValue(position)).compareTo(tuple2.getValue(position)));
			}
		});

		out.receive(processedTuples);
	}

	private int order(final int comparisonResult) {
		return DESCENDING_ORDER.equalsIgnoreCase(order) ? -comparisonResult : comparisonResult;
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

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void setTupleComponent(final int position) {
		this.position = position;
	}

	@Override
	public int getTupleComponent() {
		return position;
	}

	@Override
	public void setOrder(final String order) {
		this.order = order;
	}

	@Override
	public String getOrder() {
		return order;
	}
}
