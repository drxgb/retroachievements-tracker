package com.drxgb.ratracker.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

import com.drxgb.ratracker.builder.ParameterBuilder;
import com.drxgb.ratracker.controller.ViewSettingsController;
import com.drxgb.ratracker.exception.ApiException;
import com.drxgb.ratracker.model.entity.Api;
import com.drxgb.ratracker.util.ApiParam;

/**
 * The API service layer. It can communicate to the API server.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Api
 * @see TrackerService
 */
public class ApiService implements TrackerService
{	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The API entity.
	 */
	private Api api;
	
	/**
	 * The user service.
	 */
	private UserService userService;
	
	/**
	 * Used when the service is attempting to consume the API.
	 */
	private boolean consuming = false;


	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED ***
	 * ===========================================================
	 */
	
	@Override
	public synchronized void update() throws Throwable
	{
		JsonObject userSummary,
			gameInfoAndUserProgress,
			userProgress;
		JsonArray completedGames;
		Map<String, String> params = new HashMap<>();
		
		consuming = true;
			
		// User summary
		params.put(ApiParam.USER, api.getSession().getUserName());
		params.put(ApiParam.ACHIEVEMENT, String.valueOf(ViewSettingsController.MAX_UNLOCKED_ACHIVEMENTS));
		userSummary = (JsonObject) api.consume("API_GetUserSummary.php", params);
		
		// GameInfo and UserProgress
		final String lastGameId = userSummary.getString("LastGameID");
		params.put(ApiParam.GAME, lastGameId);
		params.put(ApiParam.USER, api.getSession().getUserName());	
		gameInfoAndUserProgress = (JsonObject) api.consume("API_GetGameInfoAndUserProgress.php", params);
		params.remove(ApiParam.GAME);
		
		// Completed Games
		completedGames = (JsonArray) api.consume("API_GetUserCompletedGames.php", params);
		
		// UserProgress
		params.put(
				ApiParam.ID,
				ParameterBuilder.listToCsv(
						completedGames.getValuesAs(JsonObject.class), 
						obj -> obj.getString("GameID"))
				);
		userProgress = (JsonObject) api.consume("API_GetUserProgress.php", params);
		params.clear();
		
		userService.updateUser(userSummary);
		userService.updateLastGame(gameInfoAndUserProgress);
		userService.updateCompletedGames(completedGames, userProgress);
		
		consuming = false;
		notifyAll();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Set the API and the user service.
	 * @param api The API entity.
	 * @param userService The user service.
	 */
	public void initialize(Api api, UserService userService)
	{
		this.api = api;
		this.userService = userService;
	}
	
	
	/**
	 * Attempts to authenticate the user.
	 * @return <code>true</code> if the authentication succeeds or
	 * <code>false</code> if the authentication failed.
	 * @throws Throwable When the authentication throws another exception.
	 */
	public boolean authenticate() throws Throwable
	{
		try
		{
			update();
			return true;
		}
		catch (ApiException | JsonParsingException e)
		{
			return false;
		}
	}
	
	
	/**
	 * Finish the current session.
	 */
	public synchronized void endSession()
	{
		if (isOpen() && consuming)
		{
			try { wait(); }
			catch (InterruptedException e) {}
		}
		api = null;
		userService = null;
	}
	
	
	/**
	 * Check whether the session is opened.
	 * @return If the the session is opoened or not.
	 */
	public boolean isOpen()
	{
		return api != null;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	public Api getApi()
	{
		return api;
	}


	public UserService getUserService()
	{
		return userService;
	}
	
	
	public boolean isConsuming()
	{
		return consuming;
	}
}
