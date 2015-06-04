package org.inria.scale.streams.windows;

import org.inria.scale.streams.operators.Window;

public interface WindowStrategy {

	void initialize(Window window);

	void tearDown();

	void check();

}
