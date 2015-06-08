package org.inria.scale.streams.windows.sliding;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public interface EvictionPolicy {

	void initialize(Window window);

	void tearDown();

	void check(Tuple tuple);

}
