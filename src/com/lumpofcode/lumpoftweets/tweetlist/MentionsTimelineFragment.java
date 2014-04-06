package com.lumpofcode.lumpoftweets.tweetlist;

import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.twitter.TwitterClient.TweetPage;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class MentionsTimelineFragment extends TweetListFragment
{

	@Override
	protected void loadTweets(TweetPage thePage, Tweet theStartingTweet)
	{
		//
		// load a page of tweetsAdapter and handle with timeline response handler
		//
		TwitterClientApp.getRestClient().getMentionsIimeline(
				new TweetTimelineResponseHandler(), TWEET_PAGE_SIZE, thePage, theStartingTweet);
	}

}
