package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface DictionarySentimentClassifierConfiguration extends AttributeController {

	void setPathInResources(String pathInResources);

	String getPathInResources();

	void setComponentIndex(int componentIndex);

	int getComponentIndex();

	void setCharset(String charset);

	String getCharset();

}
