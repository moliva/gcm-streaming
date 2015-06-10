package org.inria.scale.streams.base;

import java.util.List;

import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * <p>
 * Further abstraction for implementers of an Operator which will process one
 * tuple at time not considering the rest of the tuples in the batch.
 * </p>
 * <p>
 * This base class is aimed for those developers that prefer simplicity in their
 * implementations and that cannot take any kind of advantage out of being able
 * to process all the batch together (e.g. by using vectorization or other
 * strategies) and for operations that will strictly map tuples not needing to
 * reduce the batch in any way.
 * </p>
 * <p>
 * Finally, for each processed tuple a resulting {@link Iterable} of
 * {@link Tuple} should be returned to be later concatenated with all the others
 * results of the original and forwarded to the next components.
 * 
 * @author moliva
 *
 */
public abstract class BaseUnitOperator extends BaseOperator {

	/**
	 * Processes a single tuple and returns the resulting tuples out of this
	 * process.
	 *
	 * @param tupleToProcess
	 *          Just received tuple to be processed by the operator
	 * @return <p>
	 *         List of resulting tuples to be appended to the list sent to the
	 *         next operator in the graph
	 *         </p>
	 *         <p>
	 *         <b>Note that all the results of processed tuples for the same batch
	 *         are to be sent in the same call</b>
	 *         </p>
	 */
	protected abstract Iterable<? extends Tuple> processTuple(final Tuple tupleToProcess);

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return FluentIterable.from(tuplesToProcess).transformAndConcat(new Function<Tuple, Iterable<? extends Tuple>>() {

			@Override
			public Iterable<? extends Tuple> apply(final Tuple tupleToProcess) {
				return processTuple(tupleToProcess);
			}
		}).toList();
	}

}
