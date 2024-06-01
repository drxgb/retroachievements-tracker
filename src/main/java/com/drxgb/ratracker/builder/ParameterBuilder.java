package com.drxgb.ratracker.builder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility for parameter building. It's used to assemble a query parameter
 * on a request and send it to the some server.
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class ParameterBuilder
{
	/**
	 * Fetch all the parameters and build a string to form a request query parameter.
	 * @param params Set of parameters to be built on the query.
	 * @param separator Character to split the parameters.
	 * @return An output containing a formed <code>String</code>.
	 */
	public static String toString(Map<String, String> params, String separator)
	{
		StringBuilder sb = new StringBuilder(); 
		for (String key : params.keySet())
		{
			sb.append(separator)
				.append(key)
				.append("=")
				.append(params.get(key));
		}
		return sb.toString().substring(1);
	}
	
	
	/**
	 * Fetch all the parameters and build a string to form a request query parameter.<br>
	 * The default <code>String</code> for a separator is a <code>&</code> character.
	 * @param params Set of parameters to be built on the query.
	 * @return An output containing a formed <code>String</code>.
	 */
	public static String toString(Map<String, String> params)
	{
		return toString(params, "&");
	}
	
	
	/**
	 * Fetch a set of parameters and form a <code>String</code> containing the parameters
	 * separated by commas.
	 * @param <T> Type of data in a list.
	 * @param list A list to write a CSV.
	 * @param fnGetValue Retrieve a value from a callback to append a text.
	 * @return An output containing a formed <code>String</code>.
	 */
	public static <T> String listToCsv(List<T> list, Function<T, Object> fnGetValue)
	{
		StringBuilder sb = new StringBuilder();
		for (T e : list)
		{
			sb.append(fnGetValue.apply(e))
				.append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
