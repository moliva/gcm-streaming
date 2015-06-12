package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.base.BaseUnitOperator;
import org.javatuples.Tuple;
import org.junit.Test;

public class BaseUnitOperatorTest {

	private final BaseUnitOperator operator = new BaseUnitOperator() {

		private int count = 0;

		@Override
		protected Iterable<? extends Tuple> processTuple(final Tuple tupleToProcess) {
			return Arrays.asList(tupleWith(++count), tupleWith(++count));
		}

	};

	@Test
	public void shouldForwardResultsWhenTuplesAreReceived() throws Exception {
		final List<Tuple> tuplesToProcess = Arrays.asList(tupleWith('a'), tupleWith('b'));

		final List<? extends Tuple> receivedTuples = operator.processTuples(tuplesToProcess);

		assertThat(receivedTuples, contains(tupleWith(1), tupleWith(2), tupleWith(3), tupleWith(4)));
	}

}
