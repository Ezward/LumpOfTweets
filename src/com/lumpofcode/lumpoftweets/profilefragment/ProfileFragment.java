package com.lumpofcode.lumpoftweets.profilefragment;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class ProfileFragment extends SherlockFragment
{

	private ImageView imageProfile;
	private TextView textProfileScreenName;
	private TextView textProfileTagline;
	private TextView textTweetCount;
	private TextView textFollowingCount;
	private TextView textFollowersCount;
	
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View theView = inflater.inflate(R.layout.fragment_profile, container, false);
		
		// get the parts of the view
		imageProfile = (ImageView)theView.findViewById(R.id.imageProfile);
		textProfileScreenName = (TextView)theView.findViewById(R.id.textProfileScreenName);
		textProfileTagline = (TextView)theView.findViewById(R.id.textProfileTagline);
		textTweetCount = (TextView)theView.findViewById(R.id.textTweetCount);
		textFollowingCount = (TextView)theView.findViewById(R.id.textFollowingCount);
		textFollowersCount = (TextView)theView.findViewById(R.id.textFollowersCount);
		
		loadProfile();

		return theView;
	}
	
	protected final void updateProfileFromUser(final User theUser)
	{
		//if(null == textProfileScreenName) return;	// view has not been created yet.
		
		textProfileScreenName.setText("@" + theUser.getScreenName());
		textProfileTagline.setText(theUser.getTagline());
		textTweetCount.setText(
				this.getString(R.string.template_profile_tweet_count)
					.replace("{TWEET_COUNT}", Integer.toString(theUser.getNumTweets())));
		textFollowersCount.setText(
				this.getString(R.string.template_profile_followers_count)
					.replace("{FOLLOWERS_COUNT}", Integer.toString(theUser.getFollowersCount())));
		textFollowingCount.setText(
				this.getString(R.string.template_profile_following_count)
					.replace("{FOLLOWING_COUNT}", Integer.toString(theUser.getFriendsCount())));
		
		// load the image asynchronously
		ImageLoader.getInstance().displayImage(theUser.getProfileImageUrl(), imageProfile);
	}
	
	protected final void updateProfileFromJson(final JSONObject theJSONObject)
	{
		final User theUser = User.fromJson(theJSONObject);
		final SherlockFragmentActivity theActivity = ProfileFragment.this.getSherlockActivity();
		final ActionBar theActionBar = theActivity.getSupportActionBar();
		
		theActionBar.setTitle("@" + theUser.getScreenName());
		
		// update the values in the profile fragment
		final FragmentManager theFragmentManager = theActivity.getSupportFragmentManager();
		final ProfileFragment theProfileFragment = 
				(ProfileFragment)theFragmentManager.findFragmentById(R.id.fragmentProfile);
		theProfileFragment.updateProfileFromUser(theUser);

	}
	
	/**
	 * Load a User object and set it into the view
	 */
	protected abstract void loadProfile();



}
