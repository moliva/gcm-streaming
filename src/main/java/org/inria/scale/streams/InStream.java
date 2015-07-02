package org.inria.scale.streams;

import java.util.List;

import org.inria.scale.streams.base.MulticastInStreamBindingController;
import org.inria.scale.streams.base.MultipleSourcesAggregator;
import org.inria.scale.streams.controllers.RouterController;
import org.javatuples.Tuple;

/**
 * Main abstraction for every component susceptible of receiving tuples to be
 * processed. For this the current interface should be implemented processing or
 * storing the tuples and forwarding the results later on if it's the case. See
 * the ADL definitions for the three basic components in a streaming graph,
 * InTap, Operator, OutTap, where each interface exposed is dependent on this
 * role.
 * 
 * @see MulticastInStream A broadcasting interface for each component in the
 *      graph
 * @see MulticastInStreamBindingController Base implementation for every
 *      component that should forward tuples after being retained/processed
 * 
 * @author moliva
 *
 */
public interface InStream {

	/**
	 * Method for receiving the new tuples, most probably forwarded from the
	 * previous components in the execution graph.
	 * 
	 * @param inputSource
	 *          By default, this will always 0 and the forwarding port will also
	 *          be it, but this could change in a scenario implementing an
	 *          operator with multiple sources, more on this in
	 *          {@link MultipleSourcesAggregator} and {@link RouterController}
	 * @param newTuples
	 *          Tuples received from the previous operation in the graph, could be
	 *          of any subclass of {@link Tuple} as processed by the previous
	 *          operation. The tuples are received in the order that they should
	 *          be processed if the same order is respected
	 */
	void receive(int inputSource, List<Tuple> newTuples);

}
