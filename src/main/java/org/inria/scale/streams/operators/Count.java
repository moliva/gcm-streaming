package org.inria.scale.streams.operators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.inria.scale.streams.base.BaseOperator;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class Count extends BaseOperator {

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final Map<Tuple, Integer> tokenMap = new HashMap<>();
		for (final Tuple tuple : tuplesToProcess)
			tokenMap.put(tuple, tokenMap.containsKey(tuple) ? tokenMap.get(tuple) + 1 : 1);

		return FluentIterable.from(tokenMap.entrySet())
				.transform(new Function<Entry<Tuple, Integer>, Pair<Object, Integer>>() {

					@Override
					public Pair<Object, Integer> apply(final Entry<Tuple, Integer> input) {
						return Pair.with(input.getKey().getValue(0), input.getValue());
					}
				}).toList();
	}

}
