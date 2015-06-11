package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.operators.CountByKey;
import org.javatuples.Tuple;
import org.junit.Test;

public class CountByKeyTest {

	private final CountByKey operator = new CountByKey();

	@Test
	public void shouldCountValuesAndSumUpAccordingly() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith("eclair"), tupleWith("eclair"), tupleWith("eclair"), //
				tupleWith("froyo"), //
				tupleWith("gingerbread"), tupleWith("gingerbread"), //
				tupleWith("honeycomb"), tupleWith("honeycomb"), tupleWith("honeycomb"),//
				tupleWith("ice cream sandwich"), tupleWith("ice cream sandwich"));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, containsInAnyOrder( //
				tupleWith("eclair", 3), //
				tupleWith("froyo", 1), //
				tupleWith("gingerbread", 2), //
				tupleWith("honeycomb", 3), //
				tupleWith("ice cream sandwich", 2)));
	}

	@Test
	public void shouldCountValuesAndSumUpAccordinglyInAnyOrder() throws Exception {
		final List<Tuple> tuples = Arrays.asList(tupleWith("ice cream sandwich"), tupleWith("froyo"), tupleWith("eclair"),
				tupleWith("honeycomb"), tupleWith("eclair"), tupleWith("gingerbread"), tupleWith("eclair"),
				tupleWith("honeycomb"), tupleWith("gingerbread"), tupleWith("honeycomb"), tupleWith("ice cream sandwich"));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, containsInAnyOrder( //
				tupleWith("eclair", 3), //
				tupleWith("froyo", 1), //
				tupleWith("gingerbread", 2), //
				tupleWith("honeycomb", 3), //
				tupleWith("ice cream sandwich", 2)));
	}

}
