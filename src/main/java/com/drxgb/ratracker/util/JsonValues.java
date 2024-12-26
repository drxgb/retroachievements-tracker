package com.drxgb.ratracker.util;

import javax.json.JsonObject;

/**
 * Utility class to retrieve JSON data.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class JsonValues
{
	/*
	 * ===========================================================
	 * 			*** STATIC PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Retrieves the JSON string field from the object.
	 * 
	 * @param obj The JSON object.
	 * @param key The requested key.
	 * @param fallback The default value when the key doesn't exists.
	 * @return The JSON or the fallback value.
	 */
	public static String getString(JsonObject obj, String key, String fallback)
	{
		try
		{
			return obj.getString(key); 
		}
		catch (ClassCastException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Retrieves the JSON string field from the object.
	 * 
	 * @param obj The JSON object.
	 * @param key The requested key.
	 * @return The JSON or an empty string.
	 */
	public static String getString(JsonObject obj, String key)
	{
		return getString(obj, key, "");
	}
	
	
	/**
	 * Retrieves the JSON integer field from the object.
	 * 
	 * @param obj The JSON object.
	 * @param key The requested key.
	 * @param fallback The default value when the key doesn't exists.
	 * @return The JSON or the fallback value.
	 */
	public static Integer getInt(JsonObject obj, String key, Integer fallback)
	{
		try
		{
			return obj.getInt(key); 
		}
		catch (ClassCastException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Retrieves the JSON integer field from the object.
	 * 
	 * @param obj The JSON object.
	 * @param key The requested key.
	 * @return The JSON or the <code>0</code> value.
	 */
	public static Integer getInt(JsonObject obj, String key)
	{
		return getInt(obj, key, 0);
	}
}
