package com.drxgb.ratracker.model.entity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.drxgb.util.security.Password;

/**
 * The current session that authenticates the user containing its
 * credentials.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class Session
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The user name.
	 */
	private String userName;
	
	/**
	 * The user API key.
	 */
	private Password apiKey;	
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a session with an existing password entity.
	 * @param userName The user name.
	 * @param apiKey The protected API key.
	 */
	public Session(String userName, Password apiKey)
	{
		this.userName = userName;
		this.apiKey = apiKey;
	}
	
	
	/**
	 * Creates a session and creates a password entity.
	 * @param userName The user name.
	 * @param apiKey The protected API key.
	 */
	public Session(String userName, String apiKey)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
				IllegalBlockSizeException, BadPaddingException
	{
		this(userName, new Password(apiKey));
	}	
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public String getUserName()
	{
		return userName;
	}

	public String getApiKey()
			throws InvalidKeyException, IllegalBlockSizeException,
				BadPaddingException, UnsupportedEncodingException
	{
		return apiKey.getDecryptedMessage();
	}
}
