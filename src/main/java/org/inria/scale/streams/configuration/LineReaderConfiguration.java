package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface LineReaderConfiguration extends AttributeController {

	void setFilePath(String path);

	String getFilePath();

	void setCharset(String charset);

	String getCharset();

}
