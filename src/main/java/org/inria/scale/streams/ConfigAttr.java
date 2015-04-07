package org.inria.scale.streams;

import org.objectweb.fractal.api.control.AttributeController;

public interface ConfigAttr extends AttributeController {

	void setConfig(String config);

	String getConfig();

}
