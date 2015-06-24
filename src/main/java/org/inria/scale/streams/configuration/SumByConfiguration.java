package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface SumByConfiguration extends AttributeController {

	void setKeyTupleComponent(int position);

	int getKeyTupleComponent();

	void setSumTupleComponent(int position);

	int getSumTupleComponent();

}
