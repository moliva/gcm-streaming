package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.base.exceptions.RoutingException;
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
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		if (inputSource > 0) {
			throw new RoutingException("this operator doesn't allow an input source greater than 0, invalid input source "
					+ inputSource);
		}

		send(processTuples(newTuples));
	}

}
