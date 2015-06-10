package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SeparatedValuesConfiguration;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

/**
 * Joins all the values in a tuple into a single string, printing a
 * <code>separator</code> in between each of the components of a single tuple
 * resulting in a {@link Unit}.
 * 
 * @author moliva
 *
 */
public class SeparatedValuesTransformer extends BaseOperator implements SeparatedValuesConfiguration {

	private String separator;

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

			@Override
			public Tuple apply(final Tuple tuple) {
				return Unit.with(Joiner.on(separator).join(tuple));
			}
		}).toList();
	}

	// //////////////////////////////////////////////
	// ******* SeparatedValuesConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setSeparator(final String separator) {
		this.separator = separator;
	}

	@Override
	public String getSeparator() {
		return separator;
	}

}
