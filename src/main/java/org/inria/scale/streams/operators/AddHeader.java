package org.inria.scale.streams.operators;

import java.util.Date;
import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.collect.Lists;

public class AddHeader extends BaseOperator {

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final List<Tuple> newTuples = Lists.newArrayList(tuplesToProcess);
		newTuples.add(0, Unit.with(header()));
		return newTuples;
	}

	private String header() {
		return "**----- " + new Date() + "-----**";
	}

}
