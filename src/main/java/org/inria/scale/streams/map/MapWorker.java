package org.inria.scale.streams.map;

import org.inria.scale.streams.operators.Map;
import org.javatuples.Tuple;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

/**
 * Interface to be implemented by workers for the {@link Map} operator.
 * 
 * @author moliva
 *
 */
public interface MapWorker {

	Wrapper<Tuple> process(Tuple newTuple);

	void setClassName(String className);

}
