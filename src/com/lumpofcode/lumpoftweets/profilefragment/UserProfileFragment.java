package com.lumpofcode.lumpoftweets.profilefragment;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumpofcode.lumpoftweets.twitter.TweetsAdapter;
import com.lumpofcode.lumpoftweets.twitter.TwitterClientApp;

public class UserProfileFragment extends ProfileFragment
{
	/* (non-Javadoc)
	 * @see com.lumpofcode.lumpoftweets.profilefragment.ProfileFragment#loadProfile()
	 */
	@Override
	protected void loadProfile()
	{
		//
		// the screen names is passed as a argument in the activity's intent
		//
		TwitterClientApp.getRestClient().getUserProfile(new JsonHttpResponseHandler()
		{

			@Override
			protected void handleFailureMessage(Throwable theThrowable, String theMessage)
			{
				Log.d("DEBUG", theThrowable.getMessage());
				Log.d("DEBUG", theMessage);
				super.handleFailureMessage(theThrowable, theMessage);
			}

			@Override
			public void onFailure(Throwable theThrowable, JSONArray theJSONArray)
			{
				Log.d("DEBUG", theThrowable.getMessage());
				super.onFailure(theThrowable, theJSONArray);
			}

			@Override
			public void onSuccess(JSONArray theJSONArray)
			{
				super.onSuccess(theJSONArray);
				try
				{
					updateProfileFromJson(theJSONArray.getJSONObject(0));
				}
				catch (JSONException e)
				{
					Log.d("DEBUG", e.getMessage());

					e.printStackTrace();
				}
			}
			
		}, getActivity().getIntent().getExtras().getString(TweetsAdapter.SCREEN_NAME_ARG));
	}

}
