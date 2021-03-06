package com.lumpofcode.lumpoftweets.tweetlist;

import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.twitter.TweetsAdapter;
import com.lumpofcode.lumpoftweets.twitter.TwitterClient;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class UserTimelineFragment extends TweetListFragment
{
	/**
	 * Call the twitter service and return a list of tweets starting at theStartingTweet
	 * @param thePage
	 * @param theStartingTweet
	 */
	@Override
	protected void loadTweets(final TwitterClient.TweetPage thePage, final Tweet theStartingTweet)
	{
		//
		// load a page of tweetsAdapter and handle with timeline response handler
		//
		// the screen names is passed as a argument in the activity's intent
		//
		TwitterClientApp.getRestClient().getUserTimeline(
				new TweetTimelineResponseHandler(), 
				getActivity().getIntent().getExtras().getString(TweetsAdapter.SCREEN_NAME_ARG),
				TWEET_PAGE_SIZE, 
				thePage, 
				theStartingTweet);

	}

}
