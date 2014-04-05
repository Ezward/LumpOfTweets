package com.lumpofcode.lumpoftweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ed
 * 
 * Wrap calls to JSONObject fields and handle exceptions by returning defaults.
 *
 */
public final class JsonObjectAdapter
{
	private JsonObjectAdapter() {};	// enforce singleton
		
	/**
	 * Get a named string field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @return the String value or null if not present.
	 */
	public static final String getString(final JSONObject theJsonObject, final String theName)
	{
		return getString(theJsonObject, theName, null);
	}
	
	/**
	 * Get a named string field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @param theDefault value to return if object does have the field.
	 * @return the String value or theDefault if not present.
	 */
	public static final String getString(final JSONObject theJsonObject, final String theName, final String theDefault)
	{
		try
		{
			return theJsonObject.getString(theName);
		}
		catch (JSONException e)
		{
			return null;
		}
	}
	
	/**
	 * Get a named integer field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @return the int value or 0 if not present.
	 */
	public static final int getInteger(final JSONObject theJsonObject, final String theName)
	{
		return getInteger(theJsonObject, theName, 0);
	}
	
	/**
	 * Get a named int field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @param theDefault value to return if object does have the field.
	 * @return the int value or theDefault if not present.
	 */
	public static final int getInteger(final JSONObject theJsonObject, final String theName, final int theDefault)
	{
		try
		{
			return theJsonObject.getInt(theName);
		}
		catch (JSONException e)
		{
			return theDefault;
		}
	}
	
	/**
	 * Get a named long field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @return the long value or 0 if not present.
	 */
	public static final long getLong(final JSONObject theJsonObject, final String theName)
	{
		return getLong(theJsonObject, theName, 0);
	}
	
	/**
	 * Get a named long field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @param theDefault value to return if object does have the field.
	 * @return the long value or theDefault if not present.
	 */
	public static final long getLong(final JSONObject theJsonObject, final String theName, final long theDefault)
	{
		try
		{
			return theJsonObject.getLong(theName);
		}
		catch (JSONException e)
		{
			return theDefault;
		}
	}
	
	/**
	 * Get a named double field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @return the double value or 0 if not present.
	 */
	public static final double getDouble(final JSONObject theJsonObject, final String theName)
	{
		return getDouble(theJsonObject, theName, 0);
	}
	
	/**
	 * Get a named double field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @param theDefault value to return if object does have the field.
	 * @return the double value or theDefault if not present.
	 */
	public static final double getDouble(final JSONObject theJsonObject, final String theName, final double theDefault)
	{
		try
		{
			return theJsonObject.getDouble(theName);
		}
		catch (JSONException e)
		{
			return theDefault;
		}
	}
	
	/**
	 * Get a named boolean field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @return the boolean value or false if not present.
	 */
	public static final boolean getBoolean(final JSONObject theJsonObject, final String theName)
	{
		return getBoolean(theJsonObject, theName, false);
	}
	
	/**
	 * Get a named boolean field from the JSON object.
	 * 
	 * @param theJsonObject non-null JSONObject to query
	 * @param theName name of the field
	 * @param theDefault value to return if object does have the field.
	 * @return the boolean value or theDefault if not present.
	 */
	public static final boolean getBoolean(final JSONObject theJsonObject, final String theName, final boolean theDefault)
	{
		try
		{
			return theJsonObject.getBoolean(theName);
		}
		catch (JSONException e)
		{
			return theDefault;
		}
	}
}
