package org.inria.scale.streams.windows.sliding;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class CountTriggerPolicy implements TriggerPolicy {

	private final int count;

	public CountTriggerPolicy(final int count) {
		this.count = count;
	}

	@Override
	public void initialize(final Window window) {
		// TODO Auto-generated method stub
	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void check(final Tuple tuple) {
		// TODO Auto-generated method stub
		
	}


}
