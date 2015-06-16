package org.inria.scale.streams.base;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.multiactivity.MultiActiveService;

/**
 * Creates and returns {@link MultiActiveService}.
 * 
 * @author moliva
 *
 */
public interface MultiActiveServiceFactory {

	MultiActiveService createService(Body body);

}
