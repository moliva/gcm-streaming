package org.inria.scale.streams.tests.utils;

import static org.mockito.Matchers.argThat;

import java.util.List;

import org.hamcrest.Matcher;

public class Matchers {

	@SuppressWarnings("unchecked")
	public static <T> List<T> listThat(final Matcher<Iterable<? extends T>> matcher) {
		return (List<T>) argThat(matcher);
	}

}
