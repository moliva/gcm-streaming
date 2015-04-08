package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface WindowConfiguration extends AttributeController {

	void setBatchInterval(long milliseconds);

	long getBatchInterval();

}
