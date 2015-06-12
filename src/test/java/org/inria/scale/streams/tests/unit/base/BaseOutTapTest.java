package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.inria.scale.streams.base.BaseOutTap;
import org.inria.scale.streams.exceptions.RoutingException;
import org.javatuples.Tuple;
import org.junit.Test;

public class BaseOutTapTest {

	private List<Tuple> receivedTuples;

	private final BaseOutTap outTap = new BaseOutTap() {

		@Override
		protected void processTuples(final List<Tuple> tuples) {
			receivedTuples = tuples;
		}

	};

	@Test
	public void shouldCallProcessTuplesWhenTheyAreReceived() throws Exception {
		outTap.receive(0, Arrays.asList(tupleWith(1), tupleWith(2)));
		assertThat(receivedTuples, contains(tupleWith(1), tupleWith(2)));

		outTap.receive(0, Arrays.asList(tupleWith(3), tupleWith(4)));
		assertThat(receivedTuples, contains(tupleWith(3), tupleWith(4)));
	}

	@Test(expected = RoutingException.class)
	public void shouldNotAcceptAnInvalidInputSource() throws Exception {
		outTap.receive(1, Collections.<Tuple> emptyList());
	}

}
