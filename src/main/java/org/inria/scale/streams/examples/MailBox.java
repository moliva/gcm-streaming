package org.inria.scale.streams.examples;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InTap;
import org.inria.scale.streams.configuration.SeparatedValuesConfiguration;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;

public class MailBox implements BindingController, SeparatedValuesConfiguration, InTap, RunActive {

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void startStreaming() {}
	@Override
	public void runActivity(final Body body) {

		final Thread thread = new Thread("consuming thread") {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(batchIntervalMilliseconds);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}

					out.process();
				}
			}
		};
		thread.start();
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	private InStream out;

	private String separator;
	private long batchIntervalMilliseconds;

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

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setBatchInterval(final long batchIntervalMilliseconds) {
		this.batchIntervalMilliseconds = batchIntervalMilliseconds;
	}

	@Override
	public long getBatchInterval() {
		return batchIntervalMilliseconds;
	}

	// //////////////////////////////////////////////
	// ******* SeparatedValuesConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setSeparator(final String separator) {
		this.separator = separator;
	}

	@Override
	public String getSeparator() {
		return separator;
	}

}
