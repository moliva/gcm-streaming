package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;

public abstract class BaseOutTap implements InStream {

	protected abstract void process(List<Tuple> tuples);

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		process(newTuples);
	}

}
