package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface SortByConfiguration extends AttributeController {

	void setTupleComponent(int position);

	int getTupleComponent();

	void setOrder(String order);

	String getOrder();

}
