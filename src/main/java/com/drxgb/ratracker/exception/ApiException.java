package com.drxgb.ratracker.exception;

/**
 * Exception that handles when occour an error during the API request.
 * @author Dr.XGB
 * @version 1.0.0
 * @see RuntimeException
 */
public class ApiException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public ApiException(String msg)
	{
		super(msg);
	}

}
