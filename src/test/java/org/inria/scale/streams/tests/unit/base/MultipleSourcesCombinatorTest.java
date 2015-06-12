package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.base.MulticastInStreamBindingController.CLIENT_INTERFACE_NAME;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.inria.scale.streams.MulticastInStream;
import org.inria.scale.streams.base.MultipleSourcesCombinator;
import org.inria.scale.streams.exceptions.RoutingException;
import org.javatuples.Tuple;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.proactive.Body;

@Ignore("Have to run Proactive/GCM to make this test work!")
public class MultipleSourcesCombinatorTest {

	private static final int NUMBER_OF_SOURCES = 3;

	private final MulticastInStream out = mock(MulticastInStream.class);
	private final Body body = mock(Body.class);

	private final MultipleSourcesCombinator operator = new MultipleSourcesCombinator(NUMBER_OF_SOURCES) {

		@Override
		protected List<? extends Tuple> process() {
			return Arrays.asList(tupleWith("result"));
		}

	};

	@Before
	public void setBodyMockUp() {
		when(body.isActive()).thenReturn(true);
	}

	@Before
	public void initializeSystem() throws Exception {
		operator.bindFc(CLIENT_INTERFACE_NAME, out);
		operator.setCombinatorConfiguration("( timeBetweenExecutions: 500 )");
		operator.runActivity(body);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldForwardResultsWhenTuplesAreReceived() throws Exception {

		operator.receive(0, Arrays.asList(tupleWith(0)));
		operator.receive(1, Arrays.asList(tupleWith(1)));
		operator.receive(2, Arrays.asList(tupleWith(2)));

		verify(out).receive(anyInt(), (List<Tuple>) argThat(contains(tupleWith("results1"))));
	}

	@Test(expected = RoutingException.class)
	public void shouldNotAcceptAnInvalidInputSource() throws Exception {
		operator.receive(NUMBER_OF_SOURCES, Collections.<Tuple> emptyList());
	}

}
