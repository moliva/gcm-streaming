package org.inria.scale.streams.configuration;

public interface SeparatedValuesConfiguration extends WindowConfiguration {

	void setSeparator(String separator);

	String getSeparator();

}
