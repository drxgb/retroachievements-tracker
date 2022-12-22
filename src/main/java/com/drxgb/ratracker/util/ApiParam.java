package com.drxgb.ratracker.util;

/**
 * Utility that has some attributes to represent the query
 * parameter key during the API request building.
 * @author Dr.XGB
 * @version 1.0.0
 */
public interface ApiParam
{
	/**
	 * The achievement ID or a number of achievements.
	 */
	static final String ACHIEVEMENT		= "a";
	
	/**
	 * The game ID.
	 */
	static final String GAME			= "g";
	
	/**
	 * A specified ID.
	 */
	static final String ID				= "i";
	
	/**
	 * The user name.
	 */
	static final String USER			= "u";
	
	/**
	 * The API key.
	 */
	static final String API_KEY			= "y";
	
	/**
	 * The session user name.
	 */
	static final String SESSION_USER	= "z";
}
