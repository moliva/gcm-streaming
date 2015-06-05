package org.inria.scale.streams.windows;

import java.util.List;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

public interface WindowStrategy {

	void initialize(Window window);

	void tearDown();

	void check(List<Tuple> tuples);

}
