package com.lumpofcode.lumpoftweets.profileactivity;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.User;
import com.lumpofcode.lumpoftweets.profilefragment.ProfileFragment;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class ProfileActivity extends SherlockFragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// load the user's profile data
		loadProfile();
	}
	
	private void loadProfile()
	{
		TwitterClientApp.getRestClient().getProfile(new JsonHttpResponseHandler()
		{

			@Override
			protected void handleFailureMessage(Throwable theThrowable, String theMessage)
			{
				Log.d("DEBUG", theThrowable.toString());
				Log.d("DEBUG", theMessage);
				super.handleFailureMessage(theThrowable, theMessage);
			}

			@Override
			public void onFailure(Throwable theThrowable, JSONObject theJSONObject)
			{
				Log.d("DEBUG", theThrowable.toString());
				super.onFailure(theThrowable, theJSONObject);
			}

			@Override
			public void onSuccess(JSONObject theJSONObject)
			{
				super.onSuccess(theJSONObject);
				final User theUser = User.fromJson(theJSONObject);
				getSupportActionBar().setTitle("@" + theUser.getScreenName());
				final FragmentManager theFragmentManager = getSupportFragmentManager();
				
				// update the values in the profile fragment
				final ProfileFragment theProfileFragment = 
						(ProfileFragment)theFragmentManager.findFragmentById(R.id.fragmentProfile);
				theProfileFragment.updateProfileFromUser(theUser);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
