package org.inria.scale.streams.map;

import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.ValidWrapper;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

public class ReflectionMapWorker implements MapWorker {

	@Override
	public Wrapper<Tuple> receive(final Tuple newTuple) {
		return new ValidWrapper<Tuple>(Unit.with("loquito"));
	}

}
