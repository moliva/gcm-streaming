package org.inria.scale.streams;

import java.util.List;

import org.javatuples.Tuple;

public interface MultipleInStream {

	void receive(int inputSource, List<Tuple> newTuples);

}
