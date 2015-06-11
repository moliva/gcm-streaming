package org.inria.scale.streams.tests.utils;

import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.javatuples.Unit;

public class TupleUtils {

	public static Tuple tupleWith(final Object... content) {
		switch (content.length) {
		case 1:
			return Unit.fromArray(content);
		case 2:
			return Pair.fromArray(content);
		default:
			throw new RuntimeException("Cannot create a tuple of that size");
		}
	}
}
