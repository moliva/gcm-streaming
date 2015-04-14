package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface FileWriterConfiguration extends AttributeController {

	void setDirectoryPath(String path);

	String getDirectoryPath();

}
