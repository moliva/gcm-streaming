package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.MapReduceConfiguration;
import org.inria.scale.streams.map.MulticastMapWorker;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class MapReduce extends BaseOperator implements MapReduceConfiguration {

	public static final String WORKER_INTERFACE_NAME = "workers";

	private MulticastMapWorker workers;

	private String mappingClassName;
	private String reductionClassName;

	// //////////////////////////////////////////////
	// ******* BaseOperator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		workers.setClassName(mappingClassName);

		final List<Wrapper<Tuple>> results = workers.process(tuplesToProcess);
		final List<Tuple> tuples = FluentIterable.from(results).transform(new Function<Wrapper<Tuple>, Tuple>() {

			@Override
			public Tuple apply(final Wrapper<Tuple> wrapper) {
				return wrapper.getValue();
			}
		}).toList();
		final Function<List<Tuple>, List<Tuple>> reductionFunction = getReductionFunction();
		if (reductionFunction == null) {
			return tuples;
		} else {
			return reductionFunction.apply(tuples);
		}
	}

	private Function<List<Tuple>, List<Tuple>> getReductionFunction() {
		if (reductionClassName == null || reductionClassName.isEmpty()) {
			return null;
		}

		try {
			@SuppressWarnings("unchecked")
			final Class<Function<List<Tuple>, List<Tuple>>> reductionClass = (Class<Function<List<Tuple>, List<Tuple>>>) Class
					.forName(reductionClassName);
			return reductionClass.newInstance();

		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Class name " + reductionClassName + " was not found in the classpath");
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Error while instantiating the class " + reductionClassName, e);
		}
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		final List<String> interfaces = Lists.newArrayList(super.listFc());
		interfaces.add(WORKER_INTERFACE_NAME);
		return interfaces.toArray(new String[interfaces.size()]);
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals(WORKER_INTERFACE_NAME)) {
			return workers;
		}

		return super.lookupFc(clientItfName);
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals(WORKER_INTERFACE_NAME)) {
			workers = (MulticastMapWorker) serverItf;
		} else {
			super.bindFc(clientItfName, serverItf);
		}
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (clientItfName.equals(WORKER_INTERFACE_NAME)) {
			workers = null;
		} else {
			super.unbindFc(clientItfName);
		}
	}

	// //////////////////////////////////////////////
	// ******* Mapconfiguratino *******
	// //////////////////////////////////////////////

	@Override
	public String getMappingClassName() {
		return mappingClassName;
	}

	@Override
	public void setMappingClassName(final String className) {
		this.mappingClassName = className;
	}

	@Override
	public String getReductionClassName() {
		return reductionClassName;
	}

	@Override
	public void setReductionClassName(final String className) {
		this.reductionClassName = className;
	}

}
