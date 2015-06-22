package org.inria.scale.streams.tests.unit.base;

import static org.inria.scale.streams.base.MulticastInStreamBindingController.CLIENT_INTERFACE_NAME;

import org.inria.scale.streams.base.MulticastInStreamBindingController;
import org.inria.scale.streams.intaps.TwitterStreaming;
import org.junit.Ignore;
import org.junit.Test;

public class TwitterStreamingTest {

	@Ignore("Implement this test")
	@Test
	public void shouldStreamWhenInfoIsProvided() throws Exception {
		final TwitterStreaming twitter = new TwitterStreaming();
		// twitter.bindFc(CLIENT_INTERFACE_NAME, );

		twitter.setConsumerKey("");
		twitter.setConsumerSecret("");
		twitter.setAccessToken("");
		twitter.setAccessTokenSecret("");

		twitter.setTerms("bieber, shakira");

		twitter.startStreaming();
	}

}
