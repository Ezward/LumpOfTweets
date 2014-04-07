package com.lumpofcode.lumpoftweets.profilefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileFragment extends SherlockFragment
{

	private ImageView imageProfile;
	private TextView textProfileScreenName;
	private TextView textProfileTagline;
	private TextView textTweetCount;
	private TextView textFollowingCount;
	private TextView textFollowersCount;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View theView = inflater.inflate(R.layout.fragment_profile, container, false);
		
		// get the parts of the view
		imageProfile = (ImageView)theView.findViewById(R.id.imageProfile);
		textProfileScreenName = (TextView)theView.findViewById(R.id.textProfileScreenName);
		textProfileTagline = (TextView)theView.findViewById(R.id.textProfileTagline);
		textTweetCount = (TextView)theView.findViewById(R.id.textTweetCount);
		textFollowingCount = (TextView)theView.findViewById(R.id.textFollowingCount);
		textFollowersCount = (TextView)theView.findViewById(R.id.textFollowersCount);
		
		return theView;
	}
	
	public void updateProfileFromUser(final User theUser)
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

}
