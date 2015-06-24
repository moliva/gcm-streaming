package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.GetterConfiguration;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class Getter extends BaseOperator implements GetterConfiguration {

	private int tupleComponent;

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

			@Override
			public Tuple apply(final Tuple tuple) {
				return Unit.with(tuple.getValue(tupleComponent));
			}
		}).toList();
	}

	// //////////////////////////////////////////////
	// ******* GetterConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setTupleComponent(final int position) {
		this.tupleComponent = position;
	}

	@Override
	public int getTupleComponent() {
		return tupleComponent;
	}

}
