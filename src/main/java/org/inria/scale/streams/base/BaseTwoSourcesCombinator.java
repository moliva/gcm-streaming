package org.inria.scale.streams.base;

import java.util.List;

import org.javatuples.Tuple;

public abstract class BaseTwoSourcesCombinator extends MultipleSourcesCombinator {

	/**
	 * Actual implementation of the operation that will process the tuples from
	 * both input sources at a time.
	 * 
	 * @param tuples0
	 *          List of tuples from input source 0
	 * @param tuples1
	 *          List of tuples from input source 1
	 * @return List of tuples resulting from processing the ones in the input
	 */
	protected abstract List<? extends Tuple> process(List<Tuple> tuples0, List<Tuple> tuples1);

	// //////////////////////////////////////////////
	// ******* MultipleCombinator *******
	// //////////////////////////////////////////////

	public BaseTwoSourcesCombinator() {
		super(2);
	}

	@Override
	public List<? extends Tuple> process() {
		final List<Tuple> tuples0 = removeAllTuples(0);
		final List<Tuple> tuples1 = removeAllTuples(1);

		return process(tuples0, tuples1);
	}

}
