package org.inria.scale.streams.examples;

import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.extensions.autonomic.adl.AFactory;
import org.objectweb.proactive.extensions.autonomic.adl.AFactoryFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

public class Simple {

	public static void main(final String... args) throws Exception {
		main1("org.inria.scale.streams.examples.Simple");
	}

	public static void main1(final String... args) throws Exception {
		// verify parameter length
		if (args.length < 1) {
			System.err.println("At least an ADL package and filename should be passed as a parameter");
			System.err.println("Optionally a deployment file can be passed too");
			System.exit(1);
		}

		// handle arguments
		final String adl = args[0];
		// final String deploymentFilename = args.length > 1 ? args[1] : null;

		// get the component Factory allowing component creation from ADL
		final AFactory factory = (AFactory) AFactoryFactory.getAFactory();
		// final Factory factory =
		// org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
		// final Map<String, Object> context = new HashMap<String, Object>();

		// retrieve the deployment descriptor
		// final String deploymentFilePath =
		// BaseApplicationRunner.class.getClassLoader().getResource(deploymentFilename)
		// .getPath();
		// final ProActiveDescriptor deploymentDescriptor =
		// PADeployment.getProactiveDescriptor(deploymentFilePath);
		// context.put("deployment-descriptor", deploymentDescriptor);
		// deploymentDescriptor.activateMappings();

		final Component compositeWrapper = (Component) factory.newAutonomicComponent(adl, null);

		Remmos.enableMonitoring(compositeWrapper);
		Thread.sleep(1000);
		final MonitorController mon = Remmos.getMonitorController(compositeWrapper);
		Thread.sleep(1000);
		mon.startGCMMonitoring();
		// TODO - the metrics would come here
		final PAContentController cc = Utils.getPAContentController(compositeWrapper);
		for (final Component subComp : cc.getFcSubComponents())
			Remmos.getMonitorController(subComp).setRecordStoreCapacity(16);

		// final Component compositeWrapper = (Component) factory.newComponent(adl,
		// context);

		// start PrimitiveComputer component
		// GCM.getGCMLifeCycleController(compositeWrapper).startFc();
		Utils.getPAGCMLifeCycleController(compositeWrapper).startFc();

		System.out.println("*\n*\n* App ready: " + ((PAComponent) compositeWrapper).getID().toString() + "\n*\n*");

		while (true)
			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
	}

}
