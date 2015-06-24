package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface GetterConfiguration extends AttributeController {

	void setTupleComponent(int position);

	int getTupleComponent();

}
