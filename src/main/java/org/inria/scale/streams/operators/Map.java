package org.inria.scale.streams.operators;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.inria.scale.streams.base.BaseOperator;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class Map extends BaseOperator {

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final UUID batchId = UUID.randomUUID();
		final AtomicLong count = new AtomicLong();
		final List<Tuple> list = FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

			@Override
			public Tuple apply(final Tuple tuple) {
				return Triplet.with(batchId, count.getAndIncrement(), tuple);
			}

		}).toList();
		return null;
	}

	public class ControlTuple {

	}

}
