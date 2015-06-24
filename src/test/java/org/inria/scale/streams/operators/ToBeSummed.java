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

	@SuppressWarnings("unchecked")
	private int getSum(final Tuple tuple) {
		final List<Tuple> values = (List<Tuple>) tuple.getValue(2);
		if (values.isEmpty()) {
			return 0;
		} else {
			return (int) values.get(0).getValue(1);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> getTerms(final Tuple tuple) {
		return FluentIterable.from((List<Tuple>) tuple.getValue(1)).transformAndConcat(new Function<Tuple, List<String>>() {

			@Override
			public List<String> apply(final Tuple tuple) {
				return (List<String>) tuple.getValue(1);
			}
		}).toList();
	}

}
