package org.inria.scale.streams.configuration;

public interface SentimentClassifierConfiguration {

	void setPathInResources(String pathInResources);

	String getPathInResources();

	void setComponentIndex(int componentIndex);

	int getComponentIndex();

	void setCharset(String charset);

	String getCharset();

}
