package org.inria.scale.streams.operators;

import static java.lang.Integer.parseInt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SumByConfiguration;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class SumBy extends BaseOperator implements SumByConfiguration {

	private int keyComponent;
	private int sumComponent;

	@Override
	public List<Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final Map<Object, Integer> keyMap = new HashMap<>();
		for (final Tuple tuple : tuplesToProcess) {
			final Object key = tuple.getValue(keyComponent);
			final int sumValue = getSumValue(tuple);
			keyMap.put(key, keyMap.containsKey(key) ? keyMap.get(key) + sumValue : sumValue);
		}

		return FluentIterable.from(keyMap.entrySet()).transform(new Function<Entry<Object, Integer>, Tuple>() {

			@Override
			public Pair<Object, Integer> apply(final Entry<Object, Integer> input) {
				return Pair.with(input.getKey(), input.getValue());
			}
		}).toList();
	}

	private int getSumValue(final Tuple tuple) {
		final Object value = tuple.getValue(sumComponent);
		if (value instanceof String) {
			return parseInt((String) value);
		} else if (value instanceof Integer) {
			return (Integer) value;
		}

		// else it doesn't modify the sum
		// TODO - add a warning in the log
		return 0;
	}

	// //////////////////////////////////////////////
	// ******* SumByConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setKeyTupleComponent(final int position) {
		this.keyComponent = position;
	}

	@Override
	public int getKeyTupleComponent() {
		return keyComponent;
	}

	@Override
	public void setSumTupleComponent(final int position) {
		this.sumComponent = position;
	}

	@Override
	public int getSumTupleComponent() {
		return sumComponent;
	}

}
