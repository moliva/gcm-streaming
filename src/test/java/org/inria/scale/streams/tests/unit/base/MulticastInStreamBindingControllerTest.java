package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.inria.scale.streams.base.MulticastInStreamBindingController.CLIENT_INTERFACE_NAME;
import static org.inria.scale.streams.base.MulticastInStreamBindingController.DEFAULT_INPUT_SOURCE;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.inria.scale.streams.MulticastInStream;
import org.inria.scale.streams.base.MulticastInStreamBindingController;
import org.junit.Before;
import org.junit.Test;

public class MulticastInStreamBindingControllerTest {

	private final MulticastInStream out = mock(MulticastInStream.class);

	private final MulticastInStreamBindingController operator = new MulticastInStreamBindingController() {
		// empty class
	};

	@Before
	public void bindOutInterface() throws Exception {
		operator.bindFc(CLIENT_INTERFACE_NAME, out);
	}

	@Test
	public void shouldCallTheOutInterfaceWithTheTuplesSentThroughTheDefaultInputSource() throws Exception {
		operator.send(Arrays.asList(tupleWith(1), tupleWith(2)));
		verify(out).receive(intThat(is(equalTo(DEFAULT_INPUT_SOURCE))), listThat(contains(tupleWith(1), tupleWith(2))));

		operator.send(Arrays.asList(tupleWith(3), tupleWith(4)));
		verify(out).receive(intThat(is(equalTo(DEFAULT_INPUT_SOURCE))), listThat(contains(tupleWith(3), tupleWith(4))));
	}

}
