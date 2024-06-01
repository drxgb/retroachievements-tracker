package com.drxgb.ratracker.factory;

import javax.json.Json;
import javax.json.JsonObject;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;

/**
 * Make a JSON from a window pane.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class WindowJsonSettingsPaneFactory implements JsonSettingsPaneFactory
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
		ObservableList<Node> obsChildren = pane.getChildren();
		ObservableList<Node> obsCustoms = ((Pane)obsChildren.get(1)).getChildren();
		CheckBox chkAutoSize = (CheckBox)((Pane)obsChildren.get(0)).getChildren().get(0);
		Spinner<Integer> spnWidth = (Spinner<Integer>)obsCustoms.get(1);
		Spinner<Integer> spnHeight = (Spinner<Integer>)obsCustoms.get(3);
		ComboBox<Pos> cbAlign = (ComboBox<Pos>)obsCustoms.get(5);
		
		return Json.createObjectBuilder(settings)
				.add("autosize", chkAutoSize.isSelected())
				.add("width", spnWidth.getValue())
				.add("height", spnHeight.getValue())
				.add("align", cbAlign.getValue().name())
				.build();
	}
}
