package org.inria.scale.streams.utils;

import com.google.common.base.Function;

public class Functions {

	public static Function<String, String> trim() {
		return new Function<String, String>() {

			@Override
			public String apply(final String string) {
				return string.trim();
			}
		};
	}

}
