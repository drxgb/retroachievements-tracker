package com.drxgb.ratracker.model.entity;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParsingException;

import com.drxgb.ratracker.builder.ParameterBuilder;
import com.drxgb.ratracker.exception.ApiException;
import com.drxgb.ratracker.util.ApiParam;

/**
 * The API entity that communicates to a server.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class Api
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The API base URL.
	 */
	private String baseUrl;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The current session.
	 */
	private Session session;
	
	 
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates an API instance.
	 * @param baseUrl The base URL to connect the API.
	 * @param session The current session to be authenticated.
	 */
	public Api(String baseUrl, Session session)
	{
		this.baseUrl = baseUrl;
		this.session = session;
	}	
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Makes a communication to the server's API and delivers its
	 * resposne.
	 * @param page The API page name.
	 * @param params A set of query parameters to be included to the URL.
	 * @return A JSON Object containing the API response.
	 * @throws InvalidKeyException When the session encoding is invalid.
	 * @throws IllegalBlockSizeException When the length of data provided to a blockcipher is incorrect.
	 * @throws BadPaddingException When a particular padding mechanism data is not padded properly.
	 * @throws IOException When the URL is incorrect.
	 * @throws JsonParsingException When an incorrect JSON isbeing parsed.
	 */
	public JsonStructure consume(String page, Map<String, String> params)
			throws InvalidKeyException, IllegalBlockSizeException,
				BadPaddingException, IOException, JsonParsingException
	{		
		StringBuilder sb = new StringBuilder();		
		params.put(ApiParam.SESSION_USER, session.getUserName());
		params.put(ApiParam.API_KEY, session.getApiKey());		
		sb.append(baseUrl)
			.append(page)
			.append("?")
			.append(ParameterBuilder.toString(params));
		
		URL url = URI.create(sb.toString()).toURL();
		JsonReader jsonReader = Json.createReader(url.openStream());
		JsonStructure json = jsonReader.read();
		
		if (json == null)
			throw new ApiException("Failed to connect to API.");
		return json;
	}
	
	
	/**
	 * Makes a communication to the server's API and delivers its
	 * resposne but without query parameters.
	 * @param page The API page name.
	 * @return A JSON Object containing the API response.
	 * @throws InvalidKeyException When the session encoding is invalid.
	 * @throws IllegalBlockSizeException When the length of data provided to a blockcipher is incorrect.
	 * @throws BadPaddingException When a particular padding mechanism data is not padded properly.
	 * @throws IOException When the URL is incorrect.
	 * @throws JsonParsingException When an incorrect JSON isbeing parsed.
	 */
	public JsonStructure consume(String page)
			throws InvalidKeyException, IllegalBlockSizeException,
				BadPaddingException, IOException, JsonParsingException
	{
		return consume(page, new HashMap<String, String>());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public String getBaseUrl()
	{
		return baseUrl;
	}


	public Session getSession()
	{
		return session;
	}
	
	
}
