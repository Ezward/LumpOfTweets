package com.codepath.apps.restclienttemplate.tweetdetail;

import com.codepath.apps.restclienttemplate.R;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class TweetDetailDialog extends DialogFragment implements OnClickListener, OnEditorActionListener
{
	private static final String	CREATE_TWEET_KEY	= "CREATE_TWEET";

	private EditText			mEditText;

	/**
	 * Dialog Fragment listener pattern. Activities that call this dialog must implement this interface.
	 */
	public interface TweetDetailDialogListener
	{
		/**
		 * Called with the TweetDetailDailog finishes.
		 * 
		 * @param inputText
		 *            the text that was entered into the dialog
		 */
		void onFinishTweetDetailDialog(String inputText);
	}

	public TweetDetailDialog()
	{
		// Empty constructor required for DialogFragment
	}

	/**
	 * Factory method to create a dialog instance.
	 * 
	 * @param isNewTweet
	 * @return the dialog instance.
	 */
	public static TweetDetailDialog newInstance(boolean isNewTweet)
	{
		TweetDetailDialog frag = new TweetDetailDialog();
		Bundle args = new Bundle();
		args.putBoolean(CREATE_TWEET_KEY, isNewTweet);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		final Dialog theDialog = getDialog();
		final View theView = inflater.inflate(R.layout.fragment_new_tweet, container);
		mEditText = (EditText) theView.findViewById(R.id.editTweet);
		final boolean isNewTweet = getArguments().getBoolean(CREATE_TWEET_KEY);
		theDialog.setTitle(this.getString(R.string.new_tweet_title));

		final Button theOkButton = (Button) theView.findViewById(R.id.okTweetDetail);
		theOkButton.setOnClickListener(this);

		// Show soft keyboard automatically
		mEditText.requestFocus();
		theDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return theView;
	}

	public void onTweetOk(View theView)
	{

		final String theTweet = mEditText.getText().toString().trim();
		if ((null != theTweet) && !theTweet.isEmpty())
		{
			// Return input text to activity
			TweetDetailDialogListener listener = (TweetDetailDialogListener) getActivity();
			listener.onFinishTweetDetailDialog(theTweet);
			dismiss();
		}
		else // no tweet, just quit
		{
			dismiss();
		}
	}

	@Override
	public void onClick(View arg0)
	{
		onTweetOk(arg0);
	}

	//
	// Fires whenever the textfield has an action performed
	// In this case, when the "Done" button is pressed
	//
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		if (EditorInfo.IME_ACTION_DONE == actionId)
		{
			onTweetOk(mEditText);
			return true;
		}
		return false;
	}

}
