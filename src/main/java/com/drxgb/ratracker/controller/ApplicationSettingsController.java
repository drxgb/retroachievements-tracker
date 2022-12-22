package com.drxgb.ratracker.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Abstract controller that retrieves the settings view related to the custom views.<br>
 * Usually this view contain a <code>TabView</code> containing
 * tabs related to each settings group.
 * @author Dr.XGB
 * @version 1.0.0
 * @see SettingsController
 */
public abstract class ApplicationSettingsController extends SettingsController
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	@FXML protected Pane paneGameInfo;
	@FXML protected Pane paneUserStats;
	@FXML protected Pane paneUnlockedAchievements;
	@FXML protected Pane paneNextAchievement;
	@FXML protected Pane paneCompletedGames;
}
