package org.inria.scale.streams.operators;

import java.util.List;

import org.javatuples.Tuple;

public class Buffer extends BaseOperator {

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return tuplesToProcess;
	}

}
