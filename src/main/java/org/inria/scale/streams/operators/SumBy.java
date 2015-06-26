package org.inria.scale.streams.operators;

import static java.lang.Double.parseDouble;

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
		final Map<Object, Double> keyMap = new HashMap<Object, Double>();
		for (final Tuple tuple : tuplesToProcess) {
			final Object key = tuple.getValue(keyComponent);
			final double sumValue = getSumValue(tuple);
			keyMap.put(key, keyMap.containsKey(key) ? keyMap.get(key) + sumValue : sumValue);
		}

		return FluentIterable.from(keyMap.entrySet()).transform(new Function<Entry<Object, Double>, Tuple>() {

			@Override
			public Pair<Object, Double> apply(final Entry<Object, Double> input) {
				return Pair.with(input.getKey(), input.getValue());
			}
		}).toList();
	}

	private double getSumValue(final Tuple tuple) {
		final Object value = tuple.getValue(sumComponent);
		if (value instanceof String) {
			return parseDouble((String) value);
		} else if (value instanceof Double) {
			return (Double) value;
		}

		// else it doesn't modify the sum
		// TODO - add a warning in the log
		return 0.0;
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
