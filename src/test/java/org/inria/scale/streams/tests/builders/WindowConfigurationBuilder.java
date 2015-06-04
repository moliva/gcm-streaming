package org.inria.scale.streams.tests.builders;

import org.inria.scale.streams.base.WindowConfigurationObject;

public class WindowConfigurationBuilder {

	private String type;
	private String tumblingType;
	private long milliseconds;
	private int count;

	// //////////////////////////////////////////////
	// ******* Builder method *******
	// //////////////////////////////////////////////

	public WindowConfigurationObject build() {
		final WindowConfigurationObject windowConfiguration = new WindowConfigurationObject(type, tumblingType,
				milliseconds, count);
		return windowConfiguration;
	}

	// //////////////////////////////////////////////
	// ******* Setter methods *******
	// //////////////////////////////////////////////

	public WindowConfigurationBuilder setType(final String type) {
		this.type = type;
		return this;
	}

	public WindowConfigurationBuilder setTumblingType(final String tumblingType) {
		this.tumblingType = tumblingType;
		return this;
	}

	public WindowConfigurationBuilder setMilliseconds(final long milliseconds) {
		this.milliseconds = milliseconds;
		return this;
	}

	public WindowConfigurationBuilder setCount(final int count) {
		this.count = count;
		return this;
	}

}
