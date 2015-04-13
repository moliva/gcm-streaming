package org.inria.scale.streams.operators;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;

import java.util.Collections;
import java.util.List;

import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class Tokenizer extends BaseOperator {

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return FluentIterable.from(tuplesToProcess).transformAndConcat(new Function<Tuple, Iterable<Unit<String>>>() {

			@Override
			public List<Unit<String>> apply(final Tuple tuple) {
				return tokenizeLine(tuple);
			}
		}).toList();
	}

	private List<Unit<String>> tokenizeLine(final Tuple tuple) {
		return FluentIterable.from(tuple).transformAndConcat(new Function<Object, Iterable<Unit<String>>>() {

			@Override
			public Iterable<Unit<String>> apply(final Object input) {
				if (input instanceof String) {
					final String[] strings = ((String) input).split("\\s+");
					return FluentIterable.of(strings).filter(not(equalTo(""))).transform(wrapIntoTuple());
				} else
					return Collections.emptyList();
			}

		}).toList();
	}

	private Function<String, Unit<String>> wrapIntoTuple() {
		return new Function<String, Unit<String>>() {

			@Override
			public Unit<String> apply(final String string) {
				return Unit.with(string);
			}
		};
	}

}
