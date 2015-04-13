package org.inria.scale.streams.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InnerProcessor;
import org.inria.scale.streams.configuration.SeparatedValuesConfiguration;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class SeparatedValuesWriter implements InStream, InnerProcessor, SeparatedValuesConfiguration {

	private String separator;

	private final Queue<Tuple> lines = new ConcurrentLinkedQueue<>();

	@Override
	public void receive(final List<? extends Tuple> tuples) {
		lines.addAll(tuples);
	}

	@Override
	public void process() {
		final List<Tuple> tuplesToSend = new ArrayList<>(lines);
		lines.removeAll(tuplesToSend);

		for (final Tuple tuple : tuplesToSend)
			System.out.println(Joiner.on(separator).join(tuple));
	}

	// //////////////////////////////////////////////
	// ******* SeparatedValuesConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setSeparator(final String separator) {
		this.separator = separator;
	}

	@Override
	public String getSeparator() {
		return separator;
	}

}
