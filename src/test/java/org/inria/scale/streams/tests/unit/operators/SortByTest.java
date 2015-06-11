package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.operators.SortBy;
import org.javatuples.Tuple;
import org.junit.Test;

public class SortByTest {

	private final SortBy operator = new SortBy();

	@Test
	public void shouldSortInDescendingOrderWhenSpecified() throws Exception {
		final List<Tuple> tuples = Arrays.asList(tupleWith(2), tupleWith(6), tupleWith(0), tupleWith(4));

		operator.setOrder(SortBy.DESCENDING_ORDER);
		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(tupleWith(6), tupleWith(4), tupleWith(2), tupleWith(0)));
	}

	@Test
	public void shouldSortByTheComponentSpecified() throws Exception {
		final List<Tuple> tuples = Arrays.asList(tupleWith(1, 2), tupleWith(2, 6), tupleWith(3, 0), tupleWith(4, 4));

		operator.setTupleComponent(1);
		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(tupleWith(3, 0), tupleWith(1, 2), tupleWith(4, 4), tupleWith(2, 6)));
	}

}
