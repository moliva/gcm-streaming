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

/**
 * Reduces the tuples in the batch by counting the number of appearances of each
 * of the keys (value with index 0 of the tuple). The resulting tuples are of
 * the form of a {@link Pair} like the following (key of tuple, count of
 * appearances).
 * 
 * @author moliva
 *
 */
public class CountByKey extends BaseOperator {

	@Override
	public List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final Map<Tuple, Integer> tokenMap = new HashMap<Tuple, Integer>();
		for (final Tuple tuple : tuplesToProcess) {
			tokenMap.put(tuple, tokenMap.containsKey(tuple) ? tokenMap.get(tuple) + 1 : 1);
		}

		return FluentIterable.from(tokenMap.entrySet())
				.transform(new Function<Entry<Tuple, Integer>, Pair<Object, Integer>>() {

					@Override
					public Pair<Object, Integer> apply(final Entry<Tuple, Integer> input) {
						return Pair.with(input.getKey().getValue(0), input.getValue());
					}
				}).toList();
	}

}
