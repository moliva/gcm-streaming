package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;

public abstract class BaseOperator extends MulticastInStreamBindingController implements InStream {

	/**
	 * Processes a list of tuples and returns the resulting tuples out of this
	 * process.
	 *
	 * @param tuplesToProcess
	 *          Just received tuples to be processed by the operator
	 * @return List of resulting tuples to be sent to the next operator in the
	 *         graph
	 */
	protected abstract List<? extends Tuple> processTuples(List<Tuple> tuplesToProcess);

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<Tuple> newTuples) {
		send(processTuples(newTuples));
	}

}
