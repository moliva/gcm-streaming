package org.inria.scale.streams.intaps;

import java.util.Arrays;
import java.util.Scanner;

import org.inria.scale.streams.base.BaseInTap;
import org.javatuples.Unit;

/**
 * InTap for reading directly from the system input stream (i.e. the console
 * inputs). It generates a tuple for each line read.
 * 
 * @author moliva
 *
 */
public class SystemInputLineReader extends BaseInTap {

	@Override
	protected void startStreaming() {
		try (final Scanner scanIn = new Scanner(System.in)) {
			while (scanIn.hasNext()) {
				send(Arrays.asList(Unit.with(scanIn.nextLine())));
			}
		}
	}

}
