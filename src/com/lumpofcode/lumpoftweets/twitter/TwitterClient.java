package com.lumpofcode.lumpoftweets.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.Uri;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lumpofcode.lumpoftweets.models.Tweet;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public final class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "5NeSgbM98NjNbrvVGwjgw";       // Change this
    public static final String REST_CONSUMER_SECRET = "62OL4ZCp0c0ayniWGOugJ15qlJJOhvwLocgfaGhZbNg"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://lumpoftweets"; // Change this (here and in manifest)
    
    public static final String COUNT_PARAMETER = "?count=";
    public static final String SINCE_PARAMETER = "&since_id=";
    public static final String MAX_ID_PARAMETER = "&max_id=";
    public static final String SCREEN_NAME_PARAMETER = "&screen_name=";
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    
    /* 
     * For each end point we care about, create a method to call the end point.
     * 
     * 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
    
    /**
     * Get the authenticated user's profile data via the twitter verify credentials end point
     * @param handler the AsyncHttpResponseHandler called when the service returns with data
     */
    public void getAuthenticatedProfile(final JsonHttpResponseHandler handler)
    {
		client.get(getVerifyCredentialsUrl(), null, handler);
    }
    
    /**
     * Get a given user's profile data via the twitter lookup/user end point.
     * 
     * @param handler the AsyncHttpResponseHandler called when the service returns with data
     * @param theUserId the id of the user to lookup
     */
    public void getUserProfile(final JsonHttpResponseHandler handler, String theScreenName)
    {
    	client.get(getLookupUserUrl(theScreenName), null, handler);
    }
        

    /**
     * Get a page of tweets more recent tweets since a given tweet.
     * 
     * @param handler async callback handler that recieves result
     * @param theTweetId tweet id that bounds the request
     */
    public void getHomeTimeline(
    		final JsonHttpResponseHandler handler, 
    		final int thePageSize, 
    		final TweetPage thePageOp, 
    		final Tweet theTweet)
    {
		client.get(
				getHomeTimelineUrl(thePageSize) + thePageOp.queryParameter(theTweet)
				, null, handler);
    }
    
    /**
     * Get a page of tweets more recent tweets since a given tweet.
     * 
     * @param handler async callback handler that recieves result
     * @param theTweetId tweet id that bounds the request
     */
    public void getMentionsIimeline(
    		final JsonHttpResponseHandler handler, 
    		final int thePageSize, 
    		final TweetPage thePageOp, 
    		final Tweet theTweet)
    {
		client.get(
				getMentionsTimelineUrl(thePageSize) + thePageOp.queryParameter(theTweet)
				, null, handler);
    }
    
    /**
     * Get a page of tweets more recent tweets since a given tweet.
     * 
     * @param handler async callback handler that recieves result
     * @param theTweetId tweet id that bounds the request
     */
    public void getUserTimeline(
    		final JsonHttpResponseHandler handler, 
    		final String theScreenName,
    		final int thePageSize, 
    		final TweetPage thePageOp, 
    		final Tweet theTweet)
    {
		client.get(
				getUserTimelineUrl(theScreenName, thePageSize) + thePageOp.queryParameter(theTweet)
				, null, handler);
    }
    
    public enum TweetPage
    {
    	CURRENT_PAGE(null, 0),
    	PREVIOUS_PAGE(SINCE_PARAMETER, 0),
    	NEXT_PAGE(MAX_ID_PARAMETER, -1);
    	
    	private final String _queryParameter;
    	private final long _idAdjustment;
    	
    	TweetPage(final String theQueryParameter, final long theIdAdjustment)
    	{
    		_queryParameter = theQueryParameter;
    		_idAdjustment = theIdAdjustment;
    	}
    	
    	String queryParameter(Tweet theTweet)
    	{
    		return ((null != _queryParameter) && (null != theTweet)) 
    				? (_queryParameter + Uri.encode(Long.toString(theTweet.getId() + _idAdjustment))) 
    				: "";
    	}
    }

    private final String getHomeTimelineUrl(final int thePageSize)
    {
    	return getApiUrl("statuses/home_timeline.json")
    		+ COUNT_PARAMETER
    		+ Integer.toString(thePageSize);
    }
    
    private final String getMentionsTimelineUrl(final int thePageSize)
    {
    	return getApiUrl("statuses/mentions_timeline.json")
    		+ COUNT_PARAMETER
    		+ Integer.toString(thePageSize);
    }
    
    private final String getUserTimelineUrl(final String theScreenName, final int thePageSize)
    {
    	if((null != theScreenName) && !theScreenName.isEmpty())
    	{
    		// timeline for given screen name
	    	return getApiUrl("statuses/user_timeline.json")
	    		+ COUNT_PARAMETER
	    		+ Integer.toString(thePageSize)
	    		+ SCREEN_NAME_PARAMETER
	    		+ theScreenName;
    	}
    	
    	// default to authenticated user
    	return getApiUrl("statuses/user_timeline.json")
	    		+ COUNT_PARAMETER
	    		+ Integer.toString(thePageSize);
    }
    
    private final String getVerifyCredentialsUrl()
    {
    	return getApiUrl("account/verify_credentials.json");
    }
    
    private final String getLookupUserUrl(final String theScreenName)
    {
    	return getApiUrl("users/lookup.json") 
    			+ "?screen_name=" + Uri.encode(theScreenName);
    }
    
    /**
     * Post an update (a tweet) for the authenticated user.
     * 
     * @param handler
     * @param theTweetText the text of the tweet NOT Uri encoded.
     */
    public final void postUpdate(final AsyncHttpResponseHandler handler, final String theTweetText)
    {
    	final RequestParams theRequestParams = new RequestParams();
    	theRequestParams.put("status", theTweetText);
    	client.post(getUpdateUrl(), theRequestParams, handler);
    }
    
    private final String getUpdateUrl()
    {
    	return getApiUrl("statuses/update.json");
    }

}