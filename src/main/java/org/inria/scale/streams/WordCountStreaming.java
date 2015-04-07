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

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.api.PALifeCycle;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;


public class WordCountStreaming {

	private static final String ADL = "org.inria.scale.streams.WordCountStreaming";

	public static void main(final String... args) throws Exception {
		try {
			// get the component Factory allowing component creation from ADL
			final Factory factory = org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
			final Map<String, Object> context = new HashMap<String, Object>();

			// retrieve the deployment descriptor
			final ProActiveDescriptor deploymentDescriptor = //
					PADeployment.getProactiveDescriptor(WordCountStreaming.class.getClassLoader().getResource("deployment.xml").getPath());
			context.put("deployment-descriptor", deploymentDescriptor);
			deploymentDescriptor.activateMappings();

			final Component compositeWrapper = (Component) factory.newComponent(ADL, context);

			// start PrimitiveComputer component
			GCM.getGCMLifeCycleController(compositeWrapper).startFc();

			final Runnable runnable = (Runnable) compositeWrapper.getFcInterface("r");

			// call component
			runnable.run();

			Thread.sleep(5000);
			// wait for the end of execution
			// and kill JVM created with the deployment descriptor
			// deploymentDescriptor.killall(true);

			PALifeCycle.exitSuccess();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
