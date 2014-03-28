package com.codepath.apps.restclienttemplate;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineActivity extends Activity
{
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
		
		//
		// fire off a request for the home timeline tweets
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
				Log.d("DEBUG", theJSONArray.toString());
			}
			
		});
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}

}
