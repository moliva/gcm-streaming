package org.inria.scale.streams.base;

import java.util.List;

import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

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
