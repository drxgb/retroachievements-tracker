package com.drxgb.ratracker.factory;

import javax.json.Json;
import javax.json.JsonObject;

import com.drxgb.javafxutils.ColorParser;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;

/**
 * Make a JSON settings from a layout pane.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class LayoutJsonSettingsPaneFactory implements JsonSettingsPaneFactory
{
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	/**
	 * @see com.drxgb.ratracker.factory.JsonSettingsPaneFactory#makeObject(javax.json.JsonObject, javafx.scene.layout.Pane)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonObject makeObject(JsonObject settings, Pane pane)
	{
		Pane settingsPane = (Pane) pane.getChildren().get(0);
		ComboBox<String> cbFonts = (ComboBox<String>) settingsPane.getChildren().get(1);
		Spinner<Double> spnSize = (Spinner<Double>) settingsPane.getChildren().get(3);
		ColorPicker cpBackground = (ColorPicker) settingsPane.getChildren().get(5);
		ColorPicker cpForeground = (ColorPicker) settingsPane.getChildren().get(7);
		
		return Json.createObjectBuilder(settings)
				.add("font", cbFonts.getSelectionModel().getSelectedItem())
				.add("size", spnSize.getValue())
				.add("background", ColorParser.toCssHexColor(cpBackground.getValue()))
				.add("foreground", ColorParser.toCssHexColor(cpForeground.getValue()))
				.build();
	}
}
