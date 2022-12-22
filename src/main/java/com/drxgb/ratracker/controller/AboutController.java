package com.drxgb.ratracker.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.drxgb.ratracker.RATrackerApplication;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * Controller that retrieves informations about the application.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Initializable
 */
public class AboutController implements Initializable
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	@FXML private Label lblAppName;
	@FXML private Label lblVersion;
	@FXML private Label lblJavaVersion;
	@FXML private Label lblJavaFxVersion;
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		lblAppName.setText(RATrackerApplication.NAME);
		lblVersion.setText(RATrackerApplication.VERSION);
		lblJavaVersion.setText(System.getProperty("java.version"));
		lblJavaFxVersion.setText(System.getProperty("javafx.runtime.version"));
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Called when the user clicks on the github link.
	 * @param e The handled event.
	 */
	@FXML
	public void onHlkAuthorSiteAction(ActionEvent e)
	{
		Hyperlink link = (Hyperlink) e.getTarget();
		HostServices hs = RATrackerApplication.getInstance().getHostServices();
		hs.showDocument(link.getText());
	}

}
