package com.lumpofcode.lumpoftweets.hometimeline;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.lumpofcode.lumpoftweets.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog;
import com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog.TweetDetailDialogListener;
import com.lumpofcode.lumpoftweets.twitter.TweetsAdapter;
import com.lumpofcode.lumpoftweets.twitter.TwitterClient;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;
import com.lumpofcode.lumpoftweets.util.EndlessScrollListener;

public class HomeTimelineActivity extends SherlockFragmentActivity implements TweetDetailDialogListener
{
	private static final int	TWEET_PAGE_SIZE	= 25;

	private TweetsAdapter		tweets;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		final View theView = LayoutInflater.from(this).inflate(R.layout.activity_home_timeline, null);
		setContentView(theView);

		//
		// setup the display adapter with empty array
		//
		tweets = new TweetsAdapter(this, R.layout.tweet_item, new ArrayList<Tweet>());
		final ListView listTweets = (ListView) theView.findViewById(R.id.listView1);
		listTweets.setAdapter(tweets);

		listTweets.setOnScrollListener(new EndlessScrollListener(TWEET_PAGE_SIZE)
		{
			@Override
			public void onLoadMore(int page, int totalItemsCount)
			{
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				loadTweets(TwitterClient.TweetPage.NEXT_PAGE,
						(tweets.getCount() > 0) ? tweets.getItem(tweets.getCount() - 1) : null);
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
		getSupportMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}

	public void onTweetAction(MenuItem item)
	{
		Toast.makeText(this, "What's Happening?", Toast.LENGTH_SHORT).show();
		final FragmentManager fm = getSupportFragmentManager();
		final TweetDetailDialog theDialog = TweetDetailDialog.newInstance(true);
		theDialog.show(fm, "fragment_new_tweet");
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
	
	private void postTweet(final String theTweetText)
	{
		//
		// load a page of tweets
		//
		TwitterClientApp.getRestClient().postUpdate(new JsonHttpResponseHandler()
		{
			@Override
			public void onFailure(Throwable theThrowable, JSONObject theJSONObject)
			{
				Log.d("DEBUG", theThrowable.toString());
				super.onFailure(theThrowable, theJSONObject);
			}

			@Override
			public void onSuccess(JSONObject theJSONObject)
			{
				// insert the tweet into the array, this will update the tweet list
				final Tweet theTweet = Tweet.fromJson(theJSONObject);
				if(null != theTweet)
				{
					tweets.insert(theTweet, 0);
				}
			}

		}, theTweetText);

	}


	@Override
	public void onFinishTweetDetailDialog(String inputText)
	{
		if((null != inputText) && !inputText.isEmpty())
		{
			Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
			postTweet(inputText);
		}
	}

}
