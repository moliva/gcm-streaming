package org.inria.scale.streams.intaps;

import org.inria.scale.streams.InTap;
import org.inria.scale.streams.InnerProcessor;
import org.inria.scale.streams.configuration.WindowConfiguration;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;

public class MailBox implements BindingController, WindowConfiguration, InTap, RunActive {

	private InnerProcessor processor;

	private long batchIntervalMilliseconds = 100;

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {

		while (true) {
			try {
				Thread.sleep(batchIntervalMilliseconds);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			processor.process();
		}
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		return new String[] { "processor" };
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("processor"))
			return processor;
		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("processor"))
			processor = (InnerProcessor) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (clientItfName.equals("processor"))
			processor = null;
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

}
