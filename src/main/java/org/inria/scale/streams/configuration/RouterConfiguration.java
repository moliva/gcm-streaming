package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface RouterConfiguration extends AttributeController {

	int getOutputSource();

	void setOutputSource(int outputSource);

}
