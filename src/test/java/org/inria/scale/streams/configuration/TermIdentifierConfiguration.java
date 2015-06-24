package org.inria.scale.streams.configuration;

import org.objectweb.fractal.api.control.AttributeController;

public interface TermIdentifierConfiguration extends AttributeController {

	String getTerms();
	
	void setTerms(String people);
	
}
