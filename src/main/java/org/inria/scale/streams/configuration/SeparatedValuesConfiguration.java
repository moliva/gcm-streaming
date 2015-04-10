package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface SeparatedValuesConfiguration extends AttributeController {

	void setSeparator(String separator);

	String getSeparator();

}
