package org.inria.scale.streams.windows.sliding;

import java.util.List;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public interface EvictionStrategy {

	void initialize(Window window);

	void tearDown();

	void check(List<Tuple> tuples);

}
