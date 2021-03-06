package com.lumpofcode.lumpoftweets.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lumpofcode.lumpoftweets.R;
import com.lumpofcode.lumpoftweets.models.Tweet;
import com.lumpofcode.lumpoftweets.models.User;
import com.lumpofcode.lumpoftweets.profileactivity.UserProfileActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>
{
	public static final String SCREEN_NAME_ARG = "screen_name";
	
	final int itemLayoutId;
	
	public TweetsAdapter(final Context context, final int theItemLayoutId, final ArrayList<Tweet> theTweetList)
	{
		super(context, 0, theTweetList);
		itemLayoutId = theItemLayoutId;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{
		View theItemView = convertView;
		if(null == theItemView)
		{
			final LayoutInflater theInflator = LayoutInflater.from(getContext());
			theItemView = theInflator.inflate(itemLayoutId, null);
		}
		
		final Tweet theTweet = getItem(position);
		final User theUser = theTweet.getUser();
		
		//
		// make the profile image clickable, so it brings up the user's profile
		//
		final ImageView theImageView = (ImageView)theItemView.findViewById(R.id.imgProfile);
		theImageView.setTag(theUser.getScreenName());	// stash screen name for onClick handler to use
		theImageView.setOnClickListener(new OnClickListener()
		{
			//
			// show user's profile when we click on their profile picture
			//
			@Override
			public void onClick(final View theView)
			{
				final Context theContext = theView.getContext();
				
				// pass the screen  name to the activity
				final Intent i = new Intent(theContext, UserProfileActivity.class);
				i.putExtra(SCREEN_NAME_ARG, theView.getTag().toString());
				theContext.startActivity(i);
			}
		});
		
		ImageLoader.getInstance().displayImage(theTweet.getUser().getProfileImageUrl(), theImageView);
		
		final TextView theNameView = (TextView)theItemView.findViewById(R.id.txtName);
		final String theFormattedName = formatName(theUser);
		theNameView.setText(Html.fromHtml(theFormattedName));
		
		final TextView theWhenView = (TextView)theItemView.findViewById(R.id.txtWhen);
		final String theFormattedWhen = formatTwitterRelativeTime(theTweet.getCreatedAt());
		theWhenView.setText(Html.fromHtml(theFormattedWhen));
		
		final TextView theBodyView = (TextView)theItemView.findViewById(R.id.txtBody);
		theBodyView.setText(Html.fromHtml(theTweet.getBody()));
		

		return theItemView;
	}
	
	private static final String NAME_TEMPLATE = "<b>{NAME}</b><small><font>  @{SCREEN_NAME}</font></small>";
	private static final String NAME = "{NAME}";
	private static final String SCREEN_NAME = "{SCREEN_NAME}";
	
	private final String formatName(final User theUser)
	{
		return NAME_TEMPLATE.replace(NAME, theUser.getName()).replace(SCREEN_NAME, theUser.getScreenName());
	}
	
	private static final String WHEN_TEMPLATE = "<small><font>{TWEET_WHEN}</font></small>";
	private static final String TWEET_WHEN = "{TWEET_WHEN}";
	private final String formatTwitterRelativeTime(final String theTwitterTime)
	{
		//
		// parse out the created_at field into a Date
		// so we can calculate a relative time span from then to now.
		//
		if((null != theTwitterTime) && !theTwitterTime.isEmpty())
		{
			final Date created_at = twitterDate(theTwitterTime);
			if(null != created_at)
			{
				final Date now = new Date();
				final String theFormattedWhen = DateUtils.getRelativeTimeSpanString(
						created_at.getTime(),	// when the tweet was created
				        now.getTime(), 			// The time now
		                DateUtils.MINUTE_IN_MILLIS).toString();
				
				return WHEN_TEMPLATE.replace(TWEET_WHEN, theFormattedWhen);
			}
		}
		return "";
	}
	
	//
	// helper to format twitter date format into a Date object
	//
    static final String TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    static SimpleDateFormat _twitterDateFormat = null;
    public static final SimpleDateFormat twitterDateFormat()
    {
    	if(null == _twitterDateFormat)
    	{
    	    _twitterDateFormat = new SimpleDateFormat(TWITTER_DATE_FORMAT, Locale.ENGLISH);
    	    _twitterDateFormat.setLenient(true);
    	}
    	return _twitterDateFormat;
    }
    public static final Date twitterDate(final String theCreatedAt)
    {
    	try
		{
			return twitterDateFormat().parse(theCreatedAt);
		}
		catch (ParseException e)
		{
			return null;
		}
    }


}
