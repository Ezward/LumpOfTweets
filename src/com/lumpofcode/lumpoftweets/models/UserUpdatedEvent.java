package com.lumpofcode.lumpoftweets.models;

public class UserUpdatedEvent
{
	private final User _user;
	
	public UserUpdatedEvent(final User theUser)
	{
		_user = theUser;
	}
	
	public User getUser()
	{
		return _user;
	}
}
