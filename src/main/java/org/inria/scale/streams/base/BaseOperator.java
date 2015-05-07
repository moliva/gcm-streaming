package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;

public abstract class BaseOperator extends MulticastInStreamBindingController implements InStream {

	protected abstract List<? extends Tuple> processTuples(List<Tuple> tuplesToProcess);

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<Tuple> newTuples) {
		send(processTuples(newTuples));
	}

}
