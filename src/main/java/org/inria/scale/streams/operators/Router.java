package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.base.MulticastInStreamBindingController;
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
	// ******* RouterConfiguration
	// //////////////////////////////////////////////

	@Override
	public int getOutputSource() {
		return outputSource;
	}

	@Override
	public void setOutputSource(final int outputSource) {
		this.outputSource = outputSource;
	}

}
