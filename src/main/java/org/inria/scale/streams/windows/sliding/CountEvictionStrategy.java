package org.inria.scale.streams.windows.sliding;

import java.util.List;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public class CountEvictionStrategy implements EvictionStrategy {

	private final int count;

	public CountEvictionStrategy(final int count) {
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
	public void check(final List<Tuple> tuples) {
		// TODO Auto-generated method stub
		
	}

}
