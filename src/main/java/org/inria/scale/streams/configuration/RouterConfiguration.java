package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface RouterConfiguration extends AttributeController {

	int getInputSource();

	void setInputSource(int inputSource);

}