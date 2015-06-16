package org.inria.scale.streams.base;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.multiactivity.MultiActiveService;

/**
 * Creates {@link MultiActiveService} instances.
 * 
 * @author moliva
 *
 */
public class MultiActiveServiceFactoryImpl implements MultiActiveServiceFactory {

	@Override
	public MultiActiveService createService(final Body body) {
		return new MultiActiveService(body);
	}

}
