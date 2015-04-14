package org.inria.scale.streams.intaps;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InTap;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;

public abstract class BaseInTap implements InTap, BindingController, RunActive {

	private InStream out;

	private boolean firstTime = true;

	protected abstract void startStreaming();

	protected void send(final List<? extends Tuple> tuples) {
		out.receive((List<Tuple>)tuples);
	}

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		if (firstTime) {
			startStreaming();
			firstTime = false;
		}
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		return new String[] { "out" };
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("out"))
			return out;
		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = (InStream) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = null;
	}

}
