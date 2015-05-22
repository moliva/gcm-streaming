package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.MultipleInStream;
import org.inria.scale.streams.configuration.RouterConfiguration;
import org.javatuples.Tuple;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public class Router implements InStream, BindingController, RouterConfiguration {

	private MultipleInStream out;
	private int inputSource;

	@SuppressWarnings("unchecked")
	protected void send(final List<? extends Tuple> tuples) {
		out.receive(inputSource, (List<Tuple>) tuples);
	}

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<Tuple> newTuples) {
		send(newTuples);
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
			out = (MultipleInStream) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = null;
	}

	// //////////////////////////////////////////////
	// ******* SingleToMultipleInStreamConfiguration
	// //////////////////////////////////////////////

	@Override
	public int getInputSource() {
		return inputSource;
	}

	@Override
	public void setInputSource(final int inputSource) {
		this.inputSource = inputSource;
	}

}
