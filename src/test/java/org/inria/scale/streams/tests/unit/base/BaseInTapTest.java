package org.inria.scale.streams.tests.unit.base;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.inria.scale.streams.base.BaseInTap;
import org.junit.Test;

public class BaseInTapTest {

	private int count = 0;

	private final BaseInTap inTap = new BaseInTap() {

		@Override
		protected void startStreaming() {
			count++;
		}
	};

	@Test
	public void shouldCallStartStreamingWhenTheActivityIsRun() throws Exception {
		inTap.onStart();
		
		Thread.sleep(100);

		assertThat(count, is(equalTo(1)));
	}

}
