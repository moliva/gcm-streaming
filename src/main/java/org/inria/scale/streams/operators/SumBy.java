package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SumByConfiguration;
import org.javatuples.Tuple;

public class SumBy extends BaseOperator implements SumByConfiguration {

	private int keyComponent;
	private int sumComponent;

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		// TODO Auto-generated method stub
		return null;
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
