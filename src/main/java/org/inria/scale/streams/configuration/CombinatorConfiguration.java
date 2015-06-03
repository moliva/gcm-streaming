package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface CombinatorConfiguration extends AttributeController {

	void setCombinatorConfiguration(String combinatorConfigurationJson);

	String getCombinatorConfiguration();

}
