package com.drxgb.ratracker.factory;

import javax.json.JsonObject;

import javafx.scene.Parent;

/**
 * Make a panel to load a bunch of settings.
 * @author Dr.XGB
 * @version 1.0.0 
 */
public interface SettingsPanelFactory
{	
	/**
	 * Load the settings panel.
	 * @param settings A JSON object with the settings.
	 * @return An area to place the settings.
	 */
	Parent makePanel(JsonObject settings);
}
