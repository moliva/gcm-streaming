package org.inria.scale.streams.multiactivity;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.multiactivity.component.ComponentMultiActiveService;

/**
 * Creates {@link ComponentMultiActiveService} instances.
 * 
 * @author moliva
 *
 */
public class ComponentMultiActiveServiceFactory implements MultiActiveServiceFactory {

	@Override
	public ComponentMultiActiveService createService(final Body body) {
		return new ComponentMultiActiveService(body);
	}

}
