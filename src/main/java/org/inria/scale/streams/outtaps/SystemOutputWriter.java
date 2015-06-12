package org.inria.scale.streams.outtaps;

import java.util.List;

import org.inria.scale.streams.base.BaseOutTap;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

/**
 * Writes the contents of the tuples in the standard output. The tuples will be
 * printed per line joining their components with a blank.
 *
 * @see System#out
 *
 * @author miguel
 *
 */
public class SystemOutputWriter extends BaseOutTap {

	@Override
	public void process(final List<Tuple> tuples) {
		for (final Tuple tuple : tuples)
			System.out.println(Joiner.on(" ").join(tuple));
	}

}
