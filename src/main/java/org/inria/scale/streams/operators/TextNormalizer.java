package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * Expects tuples that contain one string component, normalizing them by
 * applying trim, lower casing them and removing punctuation marks, ignoring any
 * other kind of component.
 * 
 * @author moliva
 *
 */
public class TextNormalizer extends BaseOperator {

	@Override
	public List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

			@Override
			public Tuple apply(final Tuple tuple) {
				return normalizeTuple(tuple);
			}
		}).toList();
	}

	private Unit<?> normalizeTuple(final Tuple tuple) {
		final List<Object> normalizedTupleComponents = FluentIterable.from(tuple).transform(new Function<Object, Object>() {

			@Override
			public Object apply(final Object input) {
				if (input instanceof String)
					return ((String) input).trim().replaceAll("[,.;:¡!¿?\\(\\)\\[\\]\\{\\}]+", "").toLowerCase();
				else
					return input;
			}
		}).toList();
		return Unit.fromIterable(normalizedTupleComponents);
	}

}
