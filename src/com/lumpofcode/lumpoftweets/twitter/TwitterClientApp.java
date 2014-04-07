package com.lumpofcode.lumpoftweets.twitter;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = TwitterClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public final class TwitterClientApp extends com.activeandroid.app.Application {
	private static Context context;
	private static Bus eventBus;
	
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterClientApp.context = this;
        
        // event bus singleton
        eventBus = new Bus(ThreadEnforcer.MAIN);
        
        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);
    }
    
    public static final Bus getEventBus()
    {
    	return eventBus;
    }
    
    public static final TwitterClient getRestClient() {
    	return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterClientApp.context);
    }
}