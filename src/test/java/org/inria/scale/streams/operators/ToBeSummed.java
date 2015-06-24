package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseUnitOperator;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class ToBeSummed extends BaseUnitOperator {

	@Override
	protected Iterable<? extends Tuple> processTuple(final Tuple tupleToProcess) {
		final List<String> terms = getTerms(tupleToProcess);
		final int sum = getSum(tupleToProcess);

		return FluentIterable.from(terms).transform(new Function<String, Tuple>() {

			@Override
			public Tuple apply(final String term) {
				return Pair.with(term, sum);
			}
		}).toList();
	}

	private int getSum(final Tuple tuple) {
		return (int) ((Tuple) tuple.getValue(2)).getValue(1);
	}

	@SuppressWarnings("unchecked")
	private List<String> getTerms(final Tuple tuple) {
		return (List<String>) ((Tuple) tuple.getValue(1)).getValue(1);
	}

}
