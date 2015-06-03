package org.inria.scale.streams.tests.builders;

import org.inria.scale.streams.base.WindowConfigurationObject;

public class WindowConfigurationBuilder {

	private String type;
	private String tumblingType;
	private long milliseconds;

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

	public WindowConfigurationObject build() {
		final WindowConfigurationObject windowConfiguration = new WindowConfigurationObject(type, tumblingType, milliseconds);
		return windowConfiguration;
	}


}
