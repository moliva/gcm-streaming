package org.inria.scale.streams.examples;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;

public class WordCountStreaming {

	private static final String ADL = "org.inria.scale.streams.examples.WordCountStreaming";

	public static void main(final String... args) throws Exception {
		try {
			// get the component Factory allowing component creation from ADL
			final Factory factory = org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
			final Map<String, Object> context = new HashMap<String, Object>();

			// retrieve the deployment descriptor
			final ProActiveDescriptor deploymentDescriptor = //
					PADeployment.getProactiveDescriptor(WordCountStreaming.class.getClassLoader().getResource("deployment.xml")
							.getPath());
			context.put("deployment-descriptor", deploymentDescriptor);
			deploymentDescriptor.activateMappings();

			final Component compositeWrapper = (Component) factory.newComponent(ADL, context);

			// start PrimitiveComputer component
			GCM.getGCMLifeCycleController(compositeWrapper).startFc();

			//			new Thread() {
			//				@Override
			//				public void run() {
			//					InTap runnable1 = null;
			//					try {
			//						runnable1 = (InTap) compositeWrapper.getFcInterface("run1");
			//					} catch (final NoSuchInterfaceException e) {
			//						// TODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//					runnable1.startStreaming();
			//				};
			//			}.start();
			//
			//			final InTap runnable = (InTap) compositeWrapper.getFcInterface("run");
			//			runnable.startStreaming();

			// Thread.sleep(5000);

			// wait for the end of execution
			// and kill JVM created with the deployment descriptor
			// deploymentDescriptor.killall(true);

			// PALifeCycle.exitSuccess();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
