package org.inria.scale.streams;

import java.util.List;

import org.javatuples.Tuple;

public interface InStream {

	void receive(List<? extends Tuple> newTuples);

}
