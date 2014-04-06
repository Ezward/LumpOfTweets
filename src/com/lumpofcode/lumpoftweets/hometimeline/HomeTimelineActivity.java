package com.lumpofcode.lumpoftweets.hometimeline;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog;
import com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog.TweetDetailDialogListener;
import com.lumpofcode.lumpoftweets.tweetlist.HomeTimelineFragment;
import com.lumpofcode.lumpoftweets.tweetlist.TweetListFragment;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class HomeTimelineActivity extends SherlockFragmentActivity implements TweetDetailDialogListener
{
	TweetListFragment tweetListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final View theView = getLayoutInflater().inflate(R.layout.activity_home_timeline, null);
		setContentView(theView);

		tweetListFragment = (TweetListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}

	/**
	 * User selected to tweet, so open the dialog to enter it.
	 * 
	 * @param item
	 */
	public void onTweetAction(MenuItem item)
	{
		Toast.makeText(this, "What's Happening?", Toast.LENGTH_SHORT).show();
		final FragmentManager fm = getSupportFragmentManager();
		final TweetDetailDialog theDialog = TweetDetailDialog.newInstance(true);
		theDialog.show(fm, "fragment_new_tweet");
	}


	/* (non-Javadoc)
	 * @see com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog.TweetDetailDialogListener#onFinishTweetDetailDialog(java.lang.String)
	 */
	@Override
	public void onFinishTweetDetailDialog(String inputText)
	{
		// 
		// if inputText is not null, then tweet it
		//
		if((null != inputText) && !inputText.isEmpty())
		{
			Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
			postTweet(inputText);
		}
	}

	/**
	 * Given the text, call the tweet service to tweet it.
	 * 
	 * @param theTweetText
	 */
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
					tweetListFragment.getTweetsAdapter().insert(theTweet, 0);
				}
			}

		}, theTweetText);

	}


}
