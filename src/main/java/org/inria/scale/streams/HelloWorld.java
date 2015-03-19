/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.inria.scale.streams;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.api.PALifeCycle;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.Launcher;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;

/**
 * This example is a mix from the helloworld examples in the Fractal
 * distribution : the example from Julia, and the one from the FractalADL.<br>
 * The differences are the following : <br>
 * - from FractalADL : <br>
 * * this one uses a custom parser, based on the standard FractalADL, but it is
 * able to add cardinality to virtual nodes and allows the composition of
 * virtual nodes.<br>
 * * there are 4 .fractal files corresponding to definitions of the system in
 * the current vm, in distributed vms (this uses the ProActive deployment
 * capabilities), a version with wrapping composite components and a version
 * without wrapping components.
 *
 * Use the "parser" parameter to make it work.<br>
 * - from Julia :
 *
 *
 * Sections involving templates have been removed, because this implementation
 * does not provide templates. <br>
 * A functionality offered by ProActive is the automatic deployment of
 * components onto remote locations.<br>
 * TODO change comment When using the "distributed" option with the "parser"
 * option, the ADL loader will load the "helloworld-distributed.xml" ADL, which
 * affects virtual nodes to components, and the "deployment.xml" file, which
 * maps the virtual nodes to real nodes.<br>
 * If other cases, all components are instantiated locally, in the current
 * virtual machine. <br>
 *
 *
 */
public class HelloWorld {

	private static final String ADL = "org.inria.scale.streams.helloworld-distributed-wrappers";

	// private static final String ADL =
	// "org.inria.scale.streams.helloworld-distributed-no-wrappers";
	// private static final String ADL =
	// "org.inria.scale.streams.helloworld-local-wrappers";
	// private static final String ADL =
	// "org.inria.scale.streams.helloworld-local-no-wrappers";

	public static void main(final String[] args) throws Exception {
		try {
			// get the component Factory allowing component creation from ADL
			Factory factory = org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
			Map<String, Object> context = new HashMap<String, Object>();

			// retrieve the deployment descriptor
			ProActiveDescriptor deploymentDescriptor = //
			PADeployment.getProactiveDescriptor(HelloWorld.class.getResource("deployment.xml").getPath());
			context.put("deployment-descriptor", deploymentDescriptor);
			deploymentDescriptor.activateMappings();

			Component compositeWrapper = (Component) factory.newComponent(ADL, context);

//			Component sComp = (Component) compositeWrapper.getFcInterface("s");
//			((ServiceAttributes) GCM.getAttributeController(sComp)).setHeader("--------> ");
//			((ServiceAttributes) GCM.getAttributeController(sComp)).setCount(1);

			// start PrimitiveComputer component
			GCM.getGCMLifeCycleController(compositeWrapper).startFc();

			Runnable itf = ((Runnable) compositeWrapper.getFcInterface("r"));

			// call component
			itf.run();

			Thread.sleep(1000);
			// wait for the end of execution
			// and kill JVM created with the deployment descriptor
			deploymentDescriptor.killall(false);

			PALifeCycle.exitSuccess();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
