package org.inria.scale.streams.outtaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InnerProcessor;
import org.javatuples.Tuple;

public abstract class BaseOutTap implements InStream, InnerProcessor {

	private final Queue<Tuple> tuples = new ConcurrentLinkedQueue<>();

	protected abstract void processTuples(List<Tuple> tuplesToProcess);

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

		processTuples(tuplesToProcess);
	}

}
