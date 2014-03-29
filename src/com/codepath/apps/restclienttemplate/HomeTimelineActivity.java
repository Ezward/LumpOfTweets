package com.codepath.apps.restclienttemplate;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineActivity extends Activity
{
	private static final int TWEET_PAGE_SIZE = 25;
	
	private TweetsAdapter tweets;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		final View theView = LayoutInflater.from(this).inflate(R.layout.activity_home_timeline, null);
		setContentView(theView);
		
		//
		// setup the display adapter with empty array
		//
		tweets = new TweetsAdapter(this, R.layout.tweet_item, new ArrayList<Tweet>());
		final ListView listTweets = (ListView)theView.findViewById(R.id.listView1);
		listTweets.setAdapter(tweets);
		
		listTweets.setOnScrollListener(new EndlessScrollListener(TWEET_PAGE_SIZE)
		{
			@Override
			public void onLoadMore(int page, int totalItemsCount)
			{
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				loadTweets(TwitterClient.TweetPage.NEXT_PAGE, (tweets.getCount() > 0) ? tweets.getItem(tweets.getCount() - 1) : null);
			}
		});

		
		//
		// fire off a request for the home timeline tweets
		//
		loadTweets(TwitterClient.TweetPage.CURRENT_PAGE, null);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}
	
	private void loadTweets(final TwitterClient.TweetPage thePage, final Tweet theStartingTweet)
	{
		//
		// load a page of tweets
		//
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler()
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
				tweets.addAll(theTweets);
			}
			
		}, TWEET_PAGE_SIZE, thePage, theStartingTweet);

	}

}
