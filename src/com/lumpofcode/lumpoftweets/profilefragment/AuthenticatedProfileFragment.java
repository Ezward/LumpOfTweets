package com.lumpofcode.lumpoftweets.profilefragment;

import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class AuthenticatedProfileFragment extends ProfileFragment
{
	
	/**
	 * Factory to create AuthenticatedProfileFragment
	 * 
	 * @return
	 */
	public static final AuthenticatedProfileFragment newAuthenticatedProfileFragment()
	{
		return new AuthenticatedProfileFragment();
	}
	
	/* (non-Javadoc)
	 * @see com.lumpofcode.lumpoftweets.profilefragment.ProfileFragment#loadProfile()
	 */
	@Override
	protected void loadProfile()
	{
		TwitterClientApp.getRestClient().getAuthenticatedProfile(new JsonHttpResponseHandler()
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
				updateProfileFromJson(theJSONObject);
			}
			
		});
	}

}
