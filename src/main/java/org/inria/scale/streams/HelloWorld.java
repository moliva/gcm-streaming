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

import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.api.PALifeCycle;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.Launcher;

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
	public static void main(final String[] args) throws Exception {
		boolean useParser = false;
		boolean useTemplates = false;
		boolean useWrapper = false;
		boolean distributed = false;

		for (final String arg : args) {
			useParser |= arg.equals("parser");
			useTemplates |= arg.equals("templates");
			useWrapper |= arg.equals("wrapper");
			distributed |= arg.equals("distributed");
		}

		System.setProperty("gcm.provider", "org.objectweb.proactive.core.component.Fractive");

		if (useParser) {
			// //
			// -------------------------------------------------------------------
			// // OPTION 1 : USE THE (custom) FRACTAL ADL
			// //
			// -------------------------------------------------------------------
			final String arg0 = "-fractal"; // using the fractal component model
			String arg1; // which component definition to load
			final String arg2 = "r";
			final String arg3 = HelloWorld.class.getResource("deployment.xml").toString();

			if (distributed) {
				if (useWrapper)
					arg1 = "org.inria.scale.streams.helloworld-distributed-wrappers";
				else
					arg1 = "org.inria.scale.streams.helloworld-distributed-no-wrappers";
			} else if (useWrapper)
				arg1 = "org.inria.scale.streams.helloworld-local-wrappers";
			else
				arg1 = "org.inria.scale.streams.helloworld-local-no-wrappers";
			Launcher.main(new String[] { arg0, arg1, arg2, arg3 });
		} else {
			// -------------------------------------------------------------------
			// OPTION 2 : DO NOT USE THE FRACTAL ADL
			// -------------------------------------------------------------------
			final Component boot = Utils.getBootstrapComponent();
			final GCMTypeFactory typeFactory = GCM.getGCMTypeFactory(boot);
			Component rootComponent = null;

			// type of root component
			final ComponentType rootType = typeFactory.createFcType(new InterfaceType[] { //
					typeFactory.createFcItfType("r", Runnable.class.getName(), false, false, false) } //
					);

			// type of client component
			final ComponentType clientType = typeFactory.createFcType(new InterfaceType[] {
					typeFactory.createFcItfType("r", Runnable.class.getName(), false, false, false), //
					typeFactory.createFcItfType("s", Service.class.getName(), true, false, false) } //
					);

			// type of server component
			ComponentType serverType = typeFactory.createFcType(new InterfaceType[] {
					typeFactory.createFcItfType("s", Service.class.getName(), false, false, false), //
					typeFactory.createFcItfType(Constants.ATTRIBUTE_CONTROLLER, ServiceAttributes.class.getName(), false, false,
							false) //
					});

			final GenericFactory genericFactory = GCM.getGenericFactory(boot);

			if (!useTemplates) {
				// -------------------------------------------------------------------
				// OPTION 2.1 : CREATE COMPONENTS DIRECTLY
				// -------------------------------------------------------------------

				// create root component
				rootComponent = genericFactory.newFcInstance(rootType, new ControllerDescription("root", Constants.COMPOSITE), null);
				// create client component
				Component clientComponent = genericFactory.newFcInstance(clientType, new ControllerDescription("client", Constants.PRIMITIVE),
						new ContentDescription(ClientImpl.class.getName()));
				// create server component
				Component serverComponent = genericFactory.newFcInstance(serverType, new ControllerDescription("server", Constants.PRIMITIVE),
						new ContentDescription(ServerImpl.class.getName()));

				((ServiceAttributes) GCM.getAttributeController(serverComponent)).setHeader("--------> ");
				((ServiceAttributes) GCM.getAttributeController(serverComponent)).setCount(1);

				if (useWrapper) {
					serverType = typeFactory.createFcType(new InterfaceType[] { typeFactory.createFcItfType("s",
							Service.class.getName(), false, false, false) });
					// create client component "wrapper" component
					final Component CComp = genericFactory.newFcInstance(clientType, new ControllerDescription("client-wrapper",
							Constants.COMPOSITE), null);

					// create server component "wrapper" component
					final Component SComp = genericFactory.newFcInstance(serverType, new ControllerDescription("server-wrapper",
							Constants.COMPOSITE), null);

					// component assembly
					GCM.getContentController(CComp).addFcSubComponent(clientComponent);
					GCM.getContentController(SComp).addFcSubComponent(serverComponent);
					GCM.getBindingController(CComp).bindFc("r", clientComponent.getFcInterface("r"));
					GCM.getBindingController(clientComponent).bindFc("s", GCM.getContentController(CComp).getFcInternalInterface("s"));
					// GCM.getBindingController(cComp).bindFc("s",
					// CComp.getFcInterface("s"));
					GCM.getBindingController(SComp).bindFc("s", serverComponent.getFcInterface("s"));
					// replaces client and server components by "wrapper"
					// components
					// THIS CHANGES REFERENCES (STUBS)
					clientComponent = CComp;
					serverComponent = SComp;
				}

				// component assembly
				GCM.getContentController(rootComponent).addFcSubComponent(clientComponent);
				GCM.getContentController(rootComponent).addFcSubComponent(serverComponent);
				GCM.getBindingController(rootComponent).bindFc("r", clientComponent.getFcInterface("r"));
				GCM.getBindingController(clientComponent).bindFc("s", serverComponent.getFcInterface("s"));
			}

			// start root component
			GCM.getGCMLifeCycleController(rootComponent).startFc();

			// call main method
			((Runnable) rootComponent.getFcInterface("r")).run();
		}
		PALifeCycle.exitSuccess();
	}
}
