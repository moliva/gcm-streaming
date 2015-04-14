package org.inria.scale.streams.outtaps;

import java.util.List;

import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class SystemOutputWriter extends BaseOutTap {

	@Override
	protected void processTuples(final List<Tuple> tuplesToProcess) {
		for (final Tuple tuple : tuplesToProcess)
			System.out.println(Joiner.on(" ").join(tuple));
	}

}
