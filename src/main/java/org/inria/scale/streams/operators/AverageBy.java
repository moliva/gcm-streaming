package org.inria.scale.streams.operators;

import static java.lang.Double.parseDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.AverageByConfiguration;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class AverageBy extends BaseOperator implements AverageByConfiguration {

	private int keyComponent;
	private int sumComponent;

	@Override
	public List<Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final Map<Object, Pair<Double, Integer>> keyMap = new HashMap<Object, Pair<Double, Integer>>();
		for (final Tuple tuple : tuplesToProcess) {
			final Object key = tuple.getValue(keyComponent);

			final double sumValue = getSumValue(tuple);
			final int countValue = getCountValue(tuple);

			if (keyMap.containsKey(key)) {
				final Pair<Double, Integer> pair = keyMap.get(key);
				final double oldSum = pair.getValue0();
				final int oldCount = pair.getValue1();

				keyMap.put(key, Pair.with(oldSum + sumValue, oldCount + countValue));
			} else {
				keyMap.put(key, Pair.with(sumValue, countValue));
			}
		}

		return FluentIterable.from(keyMap.entrySet())
				.transform(new Function<Entry<Object, Pair<Double, Integer>>, Tuple>() {

					@Override
					public Pair<Object, Double> apply(final Entry<Object, Pair<Double, Integer>> input) {
						final Pair<Double, Integer> pair = input.getValue();
						final double sum = pair.getValue0();
						final int count = pair.getValue1();

						return Pair.with(input.getKey(), count > 0 ? sum / count : 0);
					}
				}).toList();
	}

	private int getCountValue(final Tuple tuple) {
		final Object value = tuple.getValue(sumComponent);
		if (value instanceof String || value instanceof Double) {
			return 1;
		}

		// else it doesn't modify the count
		// TODO - add a warning in the log
		return 0;
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
	// ******* AverageByConfiguration *******
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
