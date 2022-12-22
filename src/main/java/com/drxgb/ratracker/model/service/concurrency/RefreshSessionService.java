package com.drxgb.ratracker.model.service.concurrency;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.controller.MainController;
import com.drxgb.ratracker.model.service.ApiService;
import com.drxgb.ratracker.model.service.MainService;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Thread that works on the auto refresh in the application.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Service
 * @see Task
 */
public class RefreshSessionService extends Service<Void>
{
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The API service.
	 */
	private ApiService apiService;
	
	/**
	 * The main application controller.
	 */
	private MainController mainController;
	

	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates the thread.
	 * @param apiService The API service.
	 * @param mainController The main application service.
	 */
	public RefreshSessionService(ApiService apiService, MainController mainController)
	{
		super();
		this.apiService = apiService;
		this.mainController = mainController;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				while (apiService.isOpen())
				{
					try
					{
						int interval = MainService.getInstance()
								.getSettings()
								.getJsonObject("general")
								.getInt("autoRefreshInterval", RATrackerApplication.FALLBACK_AUTO_REFRESH_INTERVAL);
						
						Platform.runLater(() -> mainController.onMnitRefreshAction());
						Thread.sleep(interval * 1000);
					}
					catch (InterruptedException e)
					{
						break;
					}
				}
				return null;
			}
		};

	}
}
