package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.base.MulticastInStreamBindingController.CLIENT_INTERFACE_NAME;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.inria.scale.streams.MulticastInStream;
import org.inria.scale.streams.base.MultipleSourcesCombinator;
import org.inria.scale.streams.exceptions.RoutingException;
import org.inria.scale.streams.multiactivity.MultiActiveServiceFactory;
import org.javatuples.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public class MultipleSourcesCombinatorTest {

	private static final int NUMBER_OF_SOURCES = 3;
	private static final long TIME_TO_WAIT = 500;

	private final MultiActiveServiceFactory serviceFactory = mock(MultiActiveServiceFactory.class);
	private final MultiActiveService service = mock(MultiActiveService.class);

	private final MulticastInStream out = mock(MulticastInStream.class);
	private final Body body = mock(Body.class);

	private final List<Tuple> receivedTuples = new ArrayList<>();

	private final MultipleSourcesCombinator operator = new MultipleSourcesCombinator(NUMBER_OF_SOURCES, serviceFactory) {

		@Override
		protected List<? extends Tuple> process() {
			for (int i = 0; i < NUMBER_OF_SOURCES; ++i) {
				receivedTuples.addAll(removeAllTuples(i));
			}

			return Arrays.asList(tupleWith("result"));
		}

	};

	@Before
	public void setBodyMockUp() {
		when(body.isActive()).thenReturn(true);
	}

	@Before
	public void setServiceFactoryMockUp() {
		when(serviceFactory.createService(body)).thenReturn(service);
	}

	@After
	public void setBodyMockDown() {
		when(body.isActive()).thenReturn(false);
	}

	@Before
	public void initializeOperator() throws Exception {
		operator.bindFc(CLIENT_INTERFACE_NAME, out);
		operator.setCombinatorConfiguration("( timeBetweenExecutions: " + TIME_TO_WAIT + " )");
	}

	@Test
	public void shouldForwardResultsWhenTuplesAreReceived() throws Exception {
		// run in a different thread as it is just a synchronous call in the test
		new Thread() {
			@Override
			public void run() {
				operator.runActivity(body);
			}

		}.start();

		operator.receive(0, Arrays.asList(tupleWith(0)));
		operator.receive(1, Arrays.asList(tupleWith(1)));
		operator.receive(2, Arrays.asList(tupleWith(2)));

		Thread.sleep(TIME_TO_WAIT * 2);

		verify(out, atLeastOnce()).receive(anyInt(), listThat(contains(tupleWith("result"))));
		assertThat(receivedTuples, contains(tupleWith(0), tupleWith(1), tupleWith(2)));
	}

	@Test(expected = RoutingException.class)
	public void shouldNotAcceptAnInvalidInputSource() throws Exception {
		operator.receive(NUMBER_OF_SOURCES, Collections.<Tuple> emptyList());
	}

}
