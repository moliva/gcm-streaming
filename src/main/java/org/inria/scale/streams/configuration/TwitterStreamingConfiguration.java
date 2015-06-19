package org.inria.scale.streams.configuration;

public interface TwitterStreamingConfiguration {

	String getConsumerKey();

	void setConsumerKey(String consumerKey);

	String getConsumerSecret();

	void setConsumerSecret(String consumerSecret);

	String getToken();

	void setToken(String token);

	String getSecret();

	void setSecret(String secret);

	String getTerms();

	void setTerms(String terms);

	String getFollowings();

	void setFollowings(String followings);
}
