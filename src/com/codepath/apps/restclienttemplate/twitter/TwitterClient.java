package com.codepath.apps.restclienttemplate.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.Uri;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
     * Get first page of tweets in Twitter home timeline
     * 
     * @param handler async callback handler that recieves result
     */
    public void getHomeTimeline(final AsyncHttpResponseHandler handler, final int thePageSize)
    {
    	client.get(getHomeTimelineUrl(thePageSize), null, handler);
    }
    
    /**
     * Get a page of tweets more recent tweets since a given tweet.
     * 
     * @param handler async callback handler that recieves result
     * @param theTweetId tweet id that bounds the request
     */
    public void getHomeTimeline(
    		final AsyncHttpResponseHandler handler, 
    		final int thePageSize, 
    		final TweetPage thePageOp, 
    		final Tweet theTweet)
    {
		client.get(
				getHomeTimelineUrl(thePageSize) + thePageOp.queryParameter(theTweet)
				, null, handler);
    }
    
    public enum TweetPage
    {
    	CURRENT_PAGE(null),
    	PREVIOUS_PAGE(SINCE_PARAMETER),
    	NEXT_PAGE(MAX_ID_PARAMETER);
    	
    	private final String _queryParameter;
    	
    	TweetPage(final String theQueryParameter)
    	{
    		_queryParameter = theQueryParameter;
    	}
    	
    	String queryParameter(Tweet theTweet)
    	{
    		return ((null != _queryParameter) && (null != theTweet)) 
    				? (_queryParameter + Uri.encode(Long.toString(theTweet.getId()))) 
    				: "";
    	}
    }

    private final String getHomeTimelineUrl(final int thePageSize)
    {
    	return getApiUrl("statuses/home_timeline.json")
    		+ COUNT_PARAMETER
    		+ Integer.toString(thePageSize);
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