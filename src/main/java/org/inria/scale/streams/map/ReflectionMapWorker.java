package org.inria.scale.streams.map;

import org.javatuples.Tuple;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.ValidWrapper;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.WrongWrapper;

import com.google.common.base.Function;

public class ReflectionMapWorker implements MapWorker {

	private String className;

	@Override
	public Wrapper<Tuple> process(final Tuple newTuple) {
		final Function<Tuple, Tuple> mappingFunction = getMappingFunction();

		try {
			final Tuple processedTuple = mappingFunction.apply(newTuple);
			return new ValidWrapper<Tuple>(processedTuple);
		} catch (final Exception e) {
			return new WrongWrapper<Tuple>("Error while applying the mapping function from class " + className);
		}
	}

	private Function<Tuple, Tuple> getMappingFunction() {
		if (className == null || className.isEmpty()) {
			throw new RuntimeException("Class name is null or empty for the current worker");
		}

		try {
			@SuppressWarnings("unchecked")
			final Class<Function<Tuple, Tuple>> mappingsClass = (Class<Function<Tuple, Tuple>>) Class.forName(className);
			return mappingsClass.newInstance();
			
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Class name " + className + " was not found in the classpath");
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Error while instantiating the class " + className, e);
		}
	}

	@Override
	public void setClassName(final String className) {
		this.className = className;
	}

}
