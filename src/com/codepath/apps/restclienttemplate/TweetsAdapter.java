package com.codepath.apps.restclienttemplate;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>
{
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
		
		final ImageView theImageView = (ImageView)theItemView.findViewById(R.id.imgProfile);
		ImageLoader.getInstance().displayImage(theTweet.getUser().getProfileBackgroundImageUrl(), theImageView);
		
		final TextView theNameView = (TextView)theItemView.findViewById(R.id.txtName);
		final String theFormattedName = formatName(theUser);
		theNameView.setText(Html.fromHtml(theFormattedName));
		
		final TextView theBodyView = (TextView)theItemView.findViewById(R.id.txtBody);
		theBodyView.setText(Html.fromHtml(theTweet.getBody()));
		
		return theItemView;
	}
	
	private static final String NAME_TEMPLATE = "<b>{NAME}</b><small><font>@{SCREEN_NAME}</font></small>";
	private static final String NAME = "{NAME}";
	private static final String SCREEN_NAME = "{SCREEN_NAME}";
	
	private final String formatName(final User theUser)
	{
		return NAME_TEMPLATE.replace(NAME, theUser.getName()).replace(SCREEN_NAME, theUser.getScreenName());
	}

}
