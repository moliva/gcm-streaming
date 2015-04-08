package org.inria.scale.streams.examples;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.configuration.WindowConfiguration;

public class CsvWriter implements InStream, WindowConfiguration {

	private long batchIntervalMilliseconds;

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void process(final Object tuple) {
		System.out.println("hello from csvwriter, received " + tuple);
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
