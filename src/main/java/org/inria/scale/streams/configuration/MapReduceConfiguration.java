package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface MapReduceConfiguration extends AttributeController {
	
	String getMappingClassName();
	
	void setMappingClassName(String className);

	String getReductionClassName();

	void setReductionClassName(String className);

}
