package org.inria.scale.streams.intaps;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static org.inria.scale.streams.utils.Functions.trim;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.inria.scale.streams.base.BaseInTap;
import org.inria.scale.streams.configuration.TwitterStreamingConfiguration;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterStreaming extends BaseInTap implements TwitterStreamingConfiguration {

	private static final int DEFAULT_EVENT_QUEUE_SIZE = 1000;
	private static final int DEFAULT_MESSAGE_QUEUE_SIZE = 100000;

	private String consumerKey = "";
	private String consumerSecret = "";
	private String accessToken = "";
	private String accessTokenSecret = "";
	private String terms = "";
	private String followings = "";
	private String host = Constants.STREAM_HOST;

	@Override
	public void startStreaming() {
		/**
		 * Set up your blocking queues: Be sure to size these properly based on
		 * expected TPS of your stream
		 */
		final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>(DEFAULT_MESSAGE_QUEUE_SIZE);
		final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(DEFAULT_EVENT_QUEUE_SIZE);

		final Client client = new ClientBuilder() //
				// TODO - random name? sequential at least
				.name("Hosebird-Client-01") //
				.hosts(createHosts()) //
				// TODO - enable basic authentication too
				.authentication(createAuthentication()) //
				.endpoint(createEndpoint()) //
				.processor(new StringDelimitedProcessor(messageQueue)) //
				.eventMessageQueue(eventQueue)//
				.build();

		client.connect();

		while (!client.isDone()) {
			try {
				final String tweet = messageQueue.take();
				send(Arrays.asList(createTuple(tweet)));
			} catch (final InterruptedException e) { 
				e.printStackTrace();
			} catch (final NullPointerException e) { 
				e.printStackTrace();
			}
		}
	}

	private Tuple createTuple(final String tweet) {
		final JsonObject json = new Gson().fromJson(tweet, JsonObject.class);
		return Pair.with(json.get("text").getAsString(), tweet);
	}

	private HttpHosts createHosts() {
		return new HttpHosts(host);
	}

	private StatusesFilterEndpoint createEndpoint() {
		final StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		setTerms(endpoint);
		setFollowings(endpoint);
		return endpoint;
	}

	private OAuth1 createAuthentication() {
		return new OAuth1(consumerKey, consumerSecret, accessToken, accessTokenSecret);
	}

	private void setFollowings(final StatusesFilterEndpoint endpoint) {
		final List<Long> parsedFollowings = FluentIterable.of(followings.split(",")) //
				.transform(trim()) //
				.filter(not(equalTo(""))) //
				.transform(toLong()) //
				.toList();

		if (!parsedFollowings.isEmpty()) {
			endpoint.followings(parsedFollowings);
		}
	}

	private Function<String, Long> toLong() {
		return new Function<String, Long>() {

			@Override
			public Long apply(final String following) {
				return Long.parseLong(following);
			}
		};
	}

	private void setTerms(final StatusesFilterEndpoint endpoint) {
		final List<String> parsedTerms = FluentIterable.of(terms.split(",")) //
				.transform(trim()) //
				.filter(not(equalTo(""))) //
				.toList();

		if (!parsedTerms.isEmpty()) {
			endpoint.trackTerms(parsedTerms);
		}
	}

	// //////////////////////////////////////////////
	// ******* TwitterStreamingConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public String getConsumerKey() {
		return consumerKey;
	}

	@Override
	public void setConsumerKey(final String consumerKey) {
		this.consumerKey = consumerKey;
	}

	@Override
	public String getConsumerSecret() {
		return consumerSecret;
	}

	@Override
	public void setConsumerSecret(final String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public void setAccessToken(final String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	@Override
	public void setAccessTokenSecret(final String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	@Override
	public String getTerms() {
		return terms;
	}

	@Override
	public void setTerms(final String terms) {
		this.terms = terms;
	}

	@Override
	public String getFollowings() {
		return followings;
	}

	@Override
	public void setFollowings(final String followings) {
		this.followings = followings;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public void setHost(final String host) {
		this.host = host;
	}

}
