package com.drxgb.ratracker.controller.view;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Abstract controller that retrieve a single component on a view.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ViewController
 */
public abstract class SingleViewController extends ViewController
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */

	/**
	 * The main area that will be rendered the view.
	 */
	protected GridPane vbFields;
	
	/**
	 * Area where the icon wille be rendered.
	 */
	protected VBox vbIcon;
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	public void onInit()
	{
		vbIcon = new VBox();
		vbFields = new GridPane();
		vbIcon.setAlignment(Pos.CENTER);
		vbFields.setAlignment(Pos.CENTER);
	}
	
	
	@Override
	public void onUpdate()
	{
		updateIconPane(vbIcon, getIcon());
		updateFieldsPane(vbFields);
		mountView(paneMain, vbIcon, vbFields);
	}
}
