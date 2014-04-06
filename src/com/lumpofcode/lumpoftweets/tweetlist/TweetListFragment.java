package com.lumpofcode.lumpoftweets.tweetlist;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_tweet_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		//
		// setup the display adapter with empty array
		//
		final Activity theActivity = getActivity();
		tweetsAdapter = new TweetsAdapter(theActivity, R.layout.tweet_item, new ArrayList<Tweet>());
		final ListView listTweets = (ListView) theActivity.findViewById(R.id.listTweets);
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

		//
		// fire off a request for the home timeline tweetsAdapter
		//
		loadTweets(TwitterClient.TweetPage.CURRENT_PAGE, null);
	}
	
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

	}

}
