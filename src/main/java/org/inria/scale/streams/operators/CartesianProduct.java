package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseTwoSourcesCombinator;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * Produces the Cartesian product of two sets of tuples.
 * 
 * @author moliva
 *
 */
public class CartesianProduct extends BaseTwoSourcesCombinator {

	// //////////////////////////////////////////////
	// ******* BaseTwoSourcesCombinator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuples0, final List<Tuple> tuples1) {
		return FluentIterable.from(tuples0).transformAndConcat(new Function<Tuple, Iterable<? extends Tuple>>() {

			@Override
			public Iterable<? extends Tuple> apply(final Tuple tuple0) {
				return FluentIterable.from(tuples1).transform(new Function<Tuple, Tuple>() {

					@Override
					public Tuple apply(final Tuple tuple1) {
						return Pair.with(tuple0, tuple1);
					}
				});
			}
		}).toList();
	}

}
