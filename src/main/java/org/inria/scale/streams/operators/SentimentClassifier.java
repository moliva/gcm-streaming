package org.inria.scale.streams.operators;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SentimentClassifierConfiguration;
import org.javatuples.Tuple;

/**
 * Based on a tuple and a component index analyzes the sentiment value as a sum
 * of the words contained and the AFINN dictionary.
 *
 * @author moliva
 *
 */
public class SentimentClassifier extends BaseOperator implements SentimentClassifierConfiguration {

	public static final int DEFAULT_INDEX = 0;
	public static final String DEFAULT_PATH = "AFINN/AFINN-111";

	private int componentIndex = DEFAULT_INDEX;
	private String pathInResources = DEFAULT_PATH;

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		// 1) read dictionary (and cache) from resources (maybe done in the set path)
		// 2) for each tuple, get the component index
		// 3) split the string and map with the dic
		// 4) sum them up and make a new tuple
		// 5) send to next operator
		// 6) ???
		// 7) profit
		return tuplesToProcess;
	}

	// //////////////////////////////////////////////
	// ******* SentimentClassifierConfiguration ***
	// //////////////////////////////////////////////

	@Override
	public int getComponentIndex() {
		return componentIndex;
	}

	@Override
	public void setComponentIndex(final int componentIndex) {
		this.componentIndex = componentIndex;
	}

	@Override
	public String getPathInResources() {
		return pathInResources;
	}

	@Override
	public void setPathInResources(final String pathInResources) {
		this.pathInResources = pathInResources;
	}

}
