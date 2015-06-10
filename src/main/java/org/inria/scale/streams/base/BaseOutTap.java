package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;

/**
 * Base abstraction for OutTaps. This kind of operation should receive tuples
 * from the previous components and process them, outputting results outside the
 * system in the form of a file, console, socket or any other form, not being
 * able to forward tuples to any other operator.
 * 
 * @author moliva
 *
 */
public abstract class BaseOutTap implements InStream {

	/**
	 * Method to be implemented by classes extending the current one. It should do
	 * the necessary and minimum operations to the tuples received to output them
	 * through a certain medium.
	 * 
	 * @param tuples
	 *          List of tuples to be output by the tap
	 */
	protected abstract void process(List<Tuple> tuples);

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		process(newTuples);
	}

}
