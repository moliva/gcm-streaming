package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.TwoWayCombinator;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class CartesianProduct extends TwoWayCombinator {

	// //////////////////////////////////////////////
	// ******* TwoWayCombinator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> process(final List<Tuple> tuples0, final List<Tuple> tuples1) {
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
