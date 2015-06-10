package org.inria.scale.streams;

import java.util.List;

import org.javatuples.Tuple;
import org.objectweb.proactive.core.component.type.annotations.multicast.MethodDispatchMetadata;
import org.objectweb.proactive.core.component.type.annotations.multicast.ParamDispatchMetadata;
import org.objectweb.proactive.core.component.type.annotations.multicast.ParamDispatchMode;

/**
 * Interface for defining broadcasts of {@link InStream InStreams} in the way
 * out of an operator allowing a single operator to feed more than one component
 * in the graph. Used in the ADL definitions of the main concepts, InTap and
 * Operator.
 * 
 * @author moliva
 *
 */
public interface MulticastInStream {

	@MethodDispatchMetadata(mode = @ParamDispatchMetadata(mode = ParamDispatchMode.BROADCAST))
	void receive(int inputSource, List<Tuple> newTuples);

}
