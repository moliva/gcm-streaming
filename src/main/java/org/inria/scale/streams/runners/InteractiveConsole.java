package org.inria.scale.streams.runners;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import javax.naming.NamingException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.Fractive;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.extensions.autonomic.console.Console;
import org.objectweb.proactive.extensions.autonomic.exceptions.NotAutonomicException;

public class InteractiveConsole {

	public static void main(final String... args) throws Exception {
		final Options options = new Options();
		options.addOption("c", true, "component url");
		options.addOption("l", false, "print remi objects");
		options.addOption("h", true, "host");
		options.addOption("p", true, "port");

		final CommandLineParser parser = new BasicParser();
		final CommandLine cmd = parser.parse(options, args);

		int port = 1099;
		if (cmd.hasOption("p")) {
			port = Integer.parseInt(cmd.getOptionValue("p"));
		}

		String host = "localhost";
		if (cmd.hasOption("h")) {
			host = cmd.getOptionValue("h");
		}

		if (cmd.hasOption("c")) {
			final String url = cmd.getOptionValue("c");
			connectConsole(url);
		} else {
			System.out.println("Active Object URL not provided, please do so in the following line:");
			try (final Scanner scanIn = new Scanner(System.in)) {
				final String url = scanIn.nextLine();
				connectConsole(url);
			}
		}

		if (cmd.hasOption("l")) {
			final Registry registry = LocateRegistry.getRegistry(host, port);
			final String[] boundNames = registry.list();
			for (final String name : boundNames) {
				System.out.println("\t" + name);
			}
		}

	}

	private static void connectConsole(final String url) throws IOException, NotAutonomicException {
		try {
			final Component c = Fractive.lookup(url);
			final String name = ((PAComponent) c).getComponentParameters().getName();
			System.out.println("Opening console on [" + name + "] @ [" + url + "]");
			new Console(c).run();
		} catch (final NamingException ne) {
			System.err.println("url not found: " + url);
		}
	}

}
