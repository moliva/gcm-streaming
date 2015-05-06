package org.inria.scale.streams.outtaps;

import java.util.List;

import org.inria.scale.streams.InStream;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class SystemOutputWriter implements InStream {

	@Override
	public void receive(final List<Tuple> tuplesToProcess) {
		for (final Tuple tuple : tuplesToProcess)
			System.out.println(Joiner.on(" ").join(tuple));
	}

}
