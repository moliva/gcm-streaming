package org.inria.scale.streams.outtaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InnerProcessor;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class SystemOutputWriter implements InStream, InnerProcessor {

	private final Queue<Tuple> lines = new ConcurrentLinkedQueue<>();

	@Override
	public void receive(final List<? extends Tuple> tuples) {
		lines.addAll(tuples);
	}

	@Override
	public void process() {
		final List<Tuple> tuplesToSend = new ArrayList<>(lines);
		lines.removeAll(tuplesToSend);

		for (final Tuple tuple : tuplesToSend)
			System.out.println(Joiner.on(" ").join(tuple));
	}

}
