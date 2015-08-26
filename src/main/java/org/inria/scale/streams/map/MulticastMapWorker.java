package org.inria.scale.streams.map;

import java.util.List;

import org.inria.scale.streams.operators.Map;
import org.javatuples.Tuple;
import org.objectweb.proactive.core.component.type.annotations.multicast.MethodDispatchMetadata;
import org.objectweb.proactive.core.component.type.annotations.multicast.ParamDispatchMetadata;
import org.objectweb.proactive.core.component.type.annotations.multicast.ParamDispatchMode;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

/**
 * Interface to talk to a group of {@link MapWorker} that belong to a
 * {@link Map} operator.
 * 
 * @author moliva
 *
 */
public interface MulticastMapWorker {

	@MethodDispatchMetadata(mode = @ParamDispatchMetadata(mode = ParamDispatchMode.BROADCAST))
	void setClassName(String className);

	@MethodDispatchMetadata(mode = @ParamDispatchMetadata(mode = ParamDispatchMode.ROUND_ROBIN))
	List<Wrapper<Tuple>> process(List<Tuple> newTuples);

}
