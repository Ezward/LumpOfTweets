package com.lumpofcode.lumpoftweets.tweetlist;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.twitter.TweetsAdapter;
import com.lumpofcode.lumpoftweets.twitter.TwitterClient;
import com.lumpofcode.lumpoftweets.util.EndlessScrollListener;

public abstract class TweetListFragment extends SherlockFragment
{
	protected static final int	TWEET_PAGE_SIZE	= 25;

	private TweetsAdapter		tweetsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View theView = inflater.inflate(R.layout.fragment_tweet_list, container, false);
		
		//
		// setup the display adapter with empty array
		//
		tweetsAdapter = new TweetsAdapter(getActivity(), R.layout.tweet_item, new ArrayList<Tweet>());
		final ListView listTweets = (ListView) theView.findViewById(R.id.listTweets);
		listTweets.setAdapter(tweetsAdapter);
		
		listTweets.setOnScrollListener(new EndlessScrollListener(TWEET_PAGE_SIZE)
		{
			@Override
			public void onLoadMore(int page, int totalItemsCount)
			{
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				final TweetsAdapter theTweetsAdapter = getTweetsAdapter();
				loadTweets(TwitterClient.TweetPage.NEXT_PAGE,
						(theTweetsAdapter.getCount() > 0) ? theTweetsAdapter.getItem(theTweetsAdapter.getCount() - 1) : null);
			}
		});

		// load the tweets
		loadTweets(TwitterClient.TweetPage.CURRENT_PAGE, null);
		
		return theView;
	}
	
	/**
	 * @return the array adapter for this tweets list.
	 */
	public final TweetsAdapter getTweetsAdapter()
	{
		return tweetsAdapter;
	}
	
	/**
	 * Call the twitter service and return a list of tweets starting at theStartingTweet
	 * @param thePage
	 * @param theStartingTweet
	 */
	protected abstract void loadTweets(final TwitterClient.TweetPage thePage, final Tweet theStartingTweet);

	protected class TweetTimelineResponseHandler extends  JsonHttpResponseHandler
	{

		@Override
		public void onFailure(Throwable theThrowable, JSONArray theJSONArray)
		{
			Log.d("DEBUG", theThrowable.toString());
			super.onFailure(theThrowable, theJSONArray);
		}

		@Override
		public void onSuccess(JSONArray theJSONArray)
		{
			final ArrayList<Tweet> theTweets = Tweet.fromJson(theJSONArray);
			getTweetsAdapter().addAll(theTweets);
		}

		@Override
		protected void handleFailureMessage(Throwable theThrowable, String theMessage)
		{
			Log.d("DEBUG", theThrowable.toString());
			Log.d("DEBUG", theMessage);
			super.handleFailureMessage(theThrowable, theMessage);
		}
	}

}
