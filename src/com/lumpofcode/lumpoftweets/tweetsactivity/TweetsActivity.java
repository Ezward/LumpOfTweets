package com.lumpofcode.lumpoftweets.tweetsactivity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.profileactivity.ProfileActivity;
import com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog;
import com.lumpofcode.lumpoftweets.tweetdetail.TweetDetailDialog.TweetDetailDialogListener;
import com.lumpofcode.lumpoftweets.tweetlist.HomeTimelineFragment;
import com.lumpofcode.lumpoftweets.tweetlist.MentionsTimelineFragment;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class TweetsActivity 
		extends SherlockFragmentActivity 
		implements TweetDetailDialogListener, TabListener
{
	private static final Integer HOME_TAB = R.string.action_home;
	private static final Integer MENTIONS_TAB = R.string.action_mentions;
	
	private ActionBar _actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final View theView = getLayoutInflater().inflate(R.layout.activity_tweets, null);
		setContentView(theView);
		
		setupNavigationTabs();
	}

	private void setupNavigationTabs()
	{
		final ActionBar theActionBar = this.getSupportActionBar();
		
		// show tabs and title
		theActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		theActionBar.setDisplayShowTitleEnabled(true);	// show title
		
		// create the home tab
		final Tab theHomeTab = theActionBar.newTab()
				.setTag(HOME_TAB)	// just to switch on
				.setIcon(R.drawable.ic_action_home)
				.setText(R.string.action_home)
				.setTabListener(this);
		theActionBar.addTab(theHomeTab);
		
		final Tab theMentionsTab = theActionBar.newTab()
				.setTag(MENTIONS_TAB)	// just to switch on
				.setIcon(R.drawable.ic_action_mentions)
				.setText(R.string.action_mentions)
				.setTabListener(this);
		theActionBar.addTab(theMentionsTab);
		
		theActionBar.selectTab(theMentionsTab);
		
		_actionBar = theActionBar;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_tweets, menu);
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
	
	public void onProfileAction(MenuItem item)
	{
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
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
				// only do this if the home timeline tab is showing
				if(HOME_TAB.equals(_actionBar.getSelectedTab().getTag()))
				{
					// insert the tweet into the array, this will update the tweet list
					final Tweet theTweet = Tweet.fromJson(theJSONObject);
					if(null != theTweet)
					{
						final FragmentManager theFragmentManager = getSupportFragmentManager();
						final HomeTimelineFragment homeTimelineFragment = 
								(HomeTimelineFragment)theFragmentManager.findFragmentById(R.id.tweetsFrame);
						homeTimelineFragment.getTweetsAdapter().insert(theTweet, 0);
					}
				}
			}

		}, theTweetText);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		final FragmentManager theFragmentManager = getSupportFragmentManager();
		final FragmentTransaction theTransaction = theFragmentManager.beginTransaction();
		if(tab.getTag().equals(HOME_TAB))
		{
			theTransaction.replace(R.id.tweetsFrame, new HomeTimelineFragment());
		}
		else	// its the mentions tab
		{
			theTransaction.replace(R.id.tweetsFrame, new MentionsTimelineFragment());
		}
		theTransaction.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		// we don't unselect tabs
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
		// we don't reselect tabs
		
	}


}
