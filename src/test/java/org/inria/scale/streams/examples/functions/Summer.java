package org.inria.scale.streams.examples.functions;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.base.Function;

public class Summer implements Function<List<Tuple>, List<Tuple>> {

	@Override
	public List<Tuple> apply(final List<Tuple> tuples) {
		double sum = 0;
		for (final Tuple tuple : tuples) {
			final double value = (double) tuple.getValue(0);
			sum += value;
		}

		return Arrays.asList((Tuple) Unit.with(sum));
	}

}
