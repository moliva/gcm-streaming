package org.inria.scale.streams.base;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.configuration.RouterConfiguration;
import org.javatuples.Tuple;

public class Router extends MulticastInStreamBindingController implements InStream, RouterConfiguration {

	private int outputSource;

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final int inputSource, final List<Tuple> newTuples) {
		send(outputSource, newTuples);
	}

	// //////////////////////////////////////////////
	// ******* SingleToMultipleInStreamConfiguration
	// //////////////////////////////////////////////

	@Override
	public int getInputSource() {
		return outputSource;
	}

	@Override
	public void setInputSource(final int inputSource) {
		this.outputSource = inputSource;
	}

}
