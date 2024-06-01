package com.drxgb.ratracker.factory;

import javax.json.JsonObject;

import javafx.scene.layout.Pane;

/**
 * Make a JSON object containing settings from a pane.
 * @author Dr.XGB
 * @version 1.0.0
 */
public interface JsonSettingsPaneFactory
{
	/**
	 * Retrieve the object from a settings panel.
	 * @param settings The JSON Object with settings.
	 * @param pane Area when the tab is placed.
	 * @return A <code>JsonObject</code> containing the new settings.
	 */
	JsonObject makeObject(JsonObject settings, Pane pane);
}
