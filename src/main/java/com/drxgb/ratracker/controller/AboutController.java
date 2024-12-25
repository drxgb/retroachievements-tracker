package com.drxgb.ratracker.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	@FXML private Label lblAuthor;
	@FXML private Label lblVersion;
	@FXML private Label lblJavaVersion;
	@FXML private Label lblJavaFxVersion;
	@FXML private Label lblReleaseDate;
	@FXML private Label lblCopyright;
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		final Calendar releaseDate = RATrackerApplication.getReleaseDate();
		final String author = RATrackerApplication.AUTHOR;
		final DateFormat df = new SimpleDateFormat("MMM dd, YYYY");
		
		lblAppName.setText(RATrackerApplication.NAME);
		lblAuthor.setText(author);
		lblVersion.setText(RATrackerApplication.VERSION);
		lblJavaVersion.setText(System.getProperty("java.version"));
		lblJavaFxVersion.setText(System.getProperty("javafx.runtime.version"));
		lblReleaseDate.setText(df.format(releaseDate.getTime()));
		lblCopyright.setText(new StringBuilder()
				.append('©').append(' ')
				.append(releaseDate.get(Calendar.YEAR))
				.append(" - ")
				.append(author)
				.toString()
		);
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
