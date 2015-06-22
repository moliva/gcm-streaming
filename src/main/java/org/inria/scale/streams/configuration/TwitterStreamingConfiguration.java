package org.inria.scale.streams.configuration;

public interface TwitterStreamingConfiguration {

	String getConsumerKey();

	void setConsumerKey(String consumerKey);

	String getConsumerSecret();

	void setConsumerSecret(String consumerSecret);

	String getAccessToken();

	void setAccessToken(String accessToken);

	String getAccessTokenSecret();

	void setAccessTokenSecret(String accessTokenSecret);

	String getTerms();

	void setTerms(String terms);

	String getFollowings();

	void setFollowings(String followings);

	String getHost();

	void setHost(String host);

}
