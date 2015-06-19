package org.inria.scale.streams.tests.unit.base;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterStreamingTest {

	@Test
	public void shouldStreamWhenInfoIsProvided() throws Exception {
		/**
		 * Set up your blocking queues: Be sure to size these properly based on
		 * expected TPS of your stream
		 */
		final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

		/**
		 * Declare the host you want to connect to, the endpoint, and authentication
		 * (basic auth or oauth)
		 */
		final Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		final StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		// Optional: set up some followings and track terms
		final List<Long> followings = Lists.newArrayList(1234L, 566788L);
		final List<String> terms = Lists.newArrayList("twitter", "api");
		hosebirdEndpoint.followings(followings);
		hosebirdEndpoint.trackTerms(terms);

		// These secrets should be read from a config file
		final String consumerKey = "";
		final String consumerSecret = "";
		final String token = "";
		final String secret = "";
		final Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

		final ClientBuilder builder = new ClientBuilder().name("Hosebird-Client-01")
				// optional: mainly for the logs
				.hosts(hosebirdHosts).authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue)).eventMessageQueue(eventQueue);

		final Client hosebirdClient = builder.build();
		// Attempts to establish a connection.
		hosebirdClient.connect();

		while (!hosebirdClient.isDone()) {
			final String msg = msgQueue.take();
			System.out.println(msg);
		}

		// hosebirdClient.stop();
	}

}
