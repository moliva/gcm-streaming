package org.inria.scale.streams;

import java.util.List;

import org.javatuples.Tuple;
import org.objectweb.proactive.core.component.type.annotations.multicast.MethodDispatchMetadata;
import org.objectweb.proactive.core.component.type.annotations.multicast.ParamDispatchMetadata;
import org.objectweb.proactive.core.component.type.annotations.multicast.ParamDispatchMode;

/**
 * 
 * @author moliva
 *
 */
public interface RoundRobinInStream {

	@MethodDispatchMetadata(mode = @ParamDispatchMetadata(mode = ParamDispatchMode.ROUND_ROBIN))
	void receive(int inputSource, List<Tuple> newTuples);

}
