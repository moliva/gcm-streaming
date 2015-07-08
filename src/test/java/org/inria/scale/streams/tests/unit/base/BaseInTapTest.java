package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.inria.scale.streams.base.BaseInTap;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.proactive.Body;

public class BaseInTapTest {

	private int count = 0;

	private final Body body = mock(Body.class);

	private final BaseInTap inTap = new BaseInTap() {

		@Override
		protected void startStreaming() {
			count++;
		}
	};

	@Ignore("Changing interfaces")
	@Test
	public void shouldCallStartStreamingWhenTheActivityIsRun() throws Exception {
//		inTap.runComponentActivity(body);

		assertThat(count, is(equalTo(1)));
	}

	@Ignore("Changing interfaces")
	@Test
	public void shouldCallStartStreamingWhenOnlyOneTime() throws Exception {
//		inTap.runComponentActivity(body);
//		inTap.runComponentActivity(body);
//		inTap.runComponentActivity(body);

		assertThat(count, is(equalTo(1)));
	}

}
