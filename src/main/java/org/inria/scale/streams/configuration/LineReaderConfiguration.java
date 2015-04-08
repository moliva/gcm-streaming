package org.inria.scale.streams.configuration;

public interface LineReaderConfiguration extends WindowConfiguration {

	void setFilePath(String path);

	String getFilePath();

}
