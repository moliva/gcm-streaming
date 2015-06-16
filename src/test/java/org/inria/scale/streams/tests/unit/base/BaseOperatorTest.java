package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.base.MulticastInStreamBindingController.CLIENT_INTERFACE_NAME;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.inria.scale.streams.MulticastInStream;
import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.exceptions.RoutingException;
import org.javatuples.Tuple;
import org.junit.Before;
import org.junit.Test;

public class BaseOperatorTest {

	private final MulticastInStream out = mock(MulticastInStream.class);

	private List<Tuple> tuplesToProcess;

	private final BaseOperator operator = new BaseOperator() {

		private int count = 0;

		@Override
		protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
			BaseOperatorTest.this.tuplesToProcess = tuplesToProcess;
			return Arrays.asList(tupleWith("results" + ++count));
		}

	};

	@Before
	public void bindOutInterface() throws Exception {
		operator.bindFc(CLIENT_INTERFACE_NAME, out);
	}

	@Test
	public void shouldForwardResultsWhenTuplesAreReceived() throws Exception {

		operator.receive(0, Arrays.asList(tupleWith(1), tupleWith(2)));

		assertThat(tuplesToProcess, contains(tupleWith(1), tupleWith(2)));
		verify(out).receive(anyInt(), listThat(contains(tupleWith("results1"))));

		operator.receive(0, Arrays.asList(tupleWith(3), tupleWith(4)));

		assertThat(tuplesToProcess, contains(tupleWith(3), tupleWith(4)));
		verify(out).receive(anyInt(), listThat(contains(tupleWith("results2"))));
	}

	@Test(expected = RoutingException.class)
	public void shouldNotAcceptAnInvalidInputSource() throws Exception {
		operator.receive(1, Collections.<Tuple> emptyList());
	}

}
