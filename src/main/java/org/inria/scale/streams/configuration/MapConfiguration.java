package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface MapConfiguration extends AttributeController {

	String getClassName();

	void setClassName(String className);

}
