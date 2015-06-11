package org.inria.scale.streams.tests.utils;

import org.javatuples.Decade;
import org.javatuples.Ennead;
import org.javatuples.Octet;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Septet;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import org.javatuples.Unit;

public class TupleUtils {

	public static Tuple tupleWith(final Object... content) {
		switch (content.length) {
		case 1:
			return Unit.fromArray(content);
		case 2:
			return Pair.fromArray(content);
		case 3:
			return Triplet.fromArray(content);
		case 4:
			return Quartet.fromArray(content);
		case 5:
			return Quintet.fromArray(content);
		case 6:
			return Sextet.fromArray(content);
		case 7:
			return Septet.fromArray(content);
		case 8:
			return Octet.fromArray(content);
		case 9:
			return Ennead.fromArray(content);
		case 10:
			return Decade.fromArray(content);
		default:
			throw new RuntimeException("Cannot create a tuple of size " + content.length);
		}
	}
}
