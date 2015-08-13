package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.map.MulticastMapWorker;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.AttributeController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class Map extends BaseOperator implements AttributeController {

	public static final String WORKER_INTERFACE_NAME = "workers";

	private MulticastMapWorker workers;

	// //////////////////////////////////////////////
	// ******* BaseOperator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final List<Wrapper<Tuple>> results = workers.receive(tuplesToProcess);
		return FluentIterable.from(results).transform(new Function<Wrapper<Tuple>, Tuple>() {

			@Override
			public Tuple apply(final Wrapper<Tuple> wrapper) {
				return wrapper.getValue();
			}
		}).toList();
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

}
