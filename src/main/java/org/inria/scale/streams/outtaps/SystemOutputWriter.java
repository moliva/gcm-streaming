package org.inria.scale.streams.outtaps;

import java.util.List;

import org.inria.scale.streams.base.BaseOutTap;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class SystemOutputWriter extends BaseOutTap {

	@Override
	protected void process(final List<Tuple> tuples) {
		for (final Tuple tuple : tuples)
			System.out.println(Joiner.on(" ").join(tuple));
	}

}
