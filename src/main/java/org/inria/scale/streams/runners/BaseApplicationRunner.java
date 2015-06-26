package org.inria.scale.streams.runners;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;
import org.objectweb.proactive.extensions.autonomic.adl.AFactory;
import org.objectweb.proactive.extensions.autonomic.adl.AFactoryFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

/**
 * Allows users to execute their applications in a simple way, passing as an
 * argument the ADL and, optionally, the deployment configuration of the system.
 *
 * @author moliva
 *
 */
public class BaseApplicationRunner {

	private static final String DEFAULT_DEPLOYMENT_XML = "deployment.xml";

	/**
	 * <p>
	 * Main method for a simple application based on ADL.
	 * </p>
	 * <p>
	 * <b>Important remark:</b> Do not forget to pass the following parameters
	 * when executing the application:<br />
	 * <code>-Djava.security.manager -Djava.security.policy=src/test/resources/allPerm.policy  -Dgcm.provider=org.objectweb.proactive.core.component.Fractive</code>
	 * </p>
	 *
	 * @param args
	 *          The parameters accepted currently are an ADL file
	 *          <b>(required)</b> and secondly, an XML deployment file as optional
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception {
		// verify parameter length
		if (args.length < 1) {
			System.err.println("At least an ADL package and filename should be passed as a parameter");
			System.err.println("Optionally a deployment file can be passed too");
			System.exit(1);
		}

		// handle arguments
		final String adl = args[0];
		final String deploymentFilename = args.length > 1 ? args[1] : DEFAULT_DEPLOYMENT_XML;

		// get the component Factory allowing component creation from ADL
		final AFactory factory = (AFactory) AFactoryFactory.getAFactory();
		// final Factory factory =
		// org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
		final Map<String, Object> context = new HashMap<String, Object>();

		// retrieve the deployment descriptor
		final String deploymentFilePath = BaseApplicationRunner.class.getClassLoader().getResource(deploymentFilename)
				.getPath();
		final ProActiveDescriptor deploymentDescriptor = PADeployment.getProactiveDescriptor(deploymentFilePath);
		context.put("deployment-descriptor", deploymentDescriptor);
		deploymentDescriptor.activateMappings();

		final Component compositeWrapper = (Component) factory.newAutonomicComponent(adl, context);

		Remmos.enableMonitoring(compositeWrapper);
		Thread.sleep(1000);
		final MonitorController mon = Remmos.getMonitorController(compositeWrapper);
		Thread.sleep(1000);
		mon.startGCMMonitoring();

		// final Component compositeWrapper = (Component) factory.newComponent(adl,
		// context);

		// start PrimitiveComputer component
		GCM.getGCMLifeCycleController(compositeWrapper).startFc();

		System.out.println("*\n*\n* App ready: " + ((PAComponent) compositeWrapper).getID().toString() + "\n*\n*");

		while (true)
			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
	}

}
