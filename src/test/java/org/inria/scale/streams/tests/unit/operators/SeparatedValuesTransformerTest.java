package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.operators.SeparatedValuesTransformer;
import org.javatuples.Tuple;
import org.junit.Test;

public class SeparatedValuesTransformerTest {

	private final SeparatedValuesTransformer operator = new SeparatedValuesTransformer();

	@Test
	public void shouldJoinStringRepresentationsOfTheValuesInTuples() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith("But", "it's a", 5, "o'clock world", "when the whistle blows"), //
				tupleWith("No", 1, "owns", 'a', "piece of my time"));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(//
				tupleWith("But it's a 5 o'clock world when the whistle blows"), //
				tupleWith("No 1 owns a piece of my time")));
	}

	@Test
	public void shouldJoinValuesWithTheSeparatorSet() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith("Everyday", "it's", "a gettin'", "closer"), //
				tupleWith("Goin'", "faster", "than", "a", "roller", "coaster"));

		operator.setSeparator(",,,");
		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(//
				tupleWith("Everyday,,,it's,,,a gettin',,,closer"), //
				tupleWith("Goin',,,faster,,,than,,,a,,,roller,,,coaster")));
	}

}
