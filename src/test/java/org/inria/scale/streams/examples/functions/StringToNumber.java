package org.inria.scale.streams.examples.functions;

import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.base.Function;

public class StringToNumber implements Function<Tuple, Tuple> {

	@Override
	public Tuple apply(final Tuple tuple) {
		final String value = (String) tuple.getValue(0);

		try {
			return Unit.with(Double.parseDouble(value));
		} catch (final NumberFormatException e) {
			return Unit.with(Double.NaN);
		}
	}

}
