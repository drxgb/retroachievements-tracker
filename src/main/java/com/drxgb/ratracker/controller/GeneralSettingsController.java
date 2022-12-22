package com.drxgb.ratracker.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.util.Styles;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Controller to the General Settings view.
 * @author Dr.XGB
 * @version 1.0.0
 * @see SettingsController
 */
public class GeneralSettingsController extends SettingsController
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTS ***
	 * ===========================================================
	 */
	
	/**
	 * The accpeted minimum interval value to the auto refresh.
	 */
	private static final Integer MIN_AUTO_REFRESH_INTERVAL = 1;
	
	/**
	 * The accpeted maximum interval value to the auto refresh.
	 */
	private static final Integer MAX_AUTO_REFRESH_INTERVAL = 60;
	
	/**
	 * The limit to warn the user about a possible slow performance.<br>
	 * The warning must appear when the value is below this constant.
	 */
	private static final Integer WARNING_LABEL_LIMIT = 5;
	
	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	@FXML private Pane paneSettings;
	@FXML private ComboBox<String> cbTheme;
	@FXML private TextField txtDateFormat;
	@FXML private Hyperlink lnkSdf;
	@FXML private Spinner<Integer> spnAutoRefreshInterval;
	@FXML private Label lblWarning;
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Called when the user clicks on the <code>java.text.SimpleDateFormat</code>
	 * documentation link.
	 */
	@FXML
	public void onLnkSdfAction()
	{
		HostServices hs = RATrackerApplication.getInstance().getHostServices();
		hs.showDocument(
			"https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html"
		);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	protected void onInit()
	{
		JsonObject settings = mainService.getSettings().getJsonObject("general");
		Styles styles = Styles.getInstance();
		ObservableList<String> obsStyles = FXCollections.observableList(
				styles.getStyles()
					.keySet()
					.stream()
					.collect(Collectors.toList())
		);
		final Integer INTERVAL = settings.getInt(
				"autoRefreshInterval", 
				RATrackerApplication.FALLBACK_AUTO_REFRESH_INTERVAL
		);
		
		// Theme
		cbTheme.setItems(obsStyles);
		cbTheme.setValue(styles.getSelectedStyle());
		
		// Date Format
		txtDateFormat.setText(
				settings.getString(
					"dateFormat",
					RATrackerApplication.getDateFormat().toPattern()
				)
		);

		// Auto Refresh Interval
		spnAutoRefreshInterval.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(
						MIN_AUTO_REFRESH_INTERVAL,
						MAX_AUTO_REFRESH_INTERVAL,
						INTERVAL,
						1
				)
		);
		spnAutoRefreshInterval.valueProperty().addListener((obs, oldValue, newValue) -> {
			toggleWarningLabel(newValue);
		});
		lblWarning.getStyleClass().add("text-warning");
		toggleWarningLabel(INTERVAL);
		addApplyActionListener(paneSettings);
	}
	

	@Override
	protected void onSave(JsonObjectBuilder builder, JsonObject settings) throws IOException
	{
		Scene scene = RATrackerApplication.getScene();
		
		// Salvar JSON
		builder.add(
				"general",
				Json.createObjectBuilder(settings.getJsonObject("general"))
					.add("theme", cbTheme.getValue())
					.add("dateFormat", txtDateFormat.getText())
					.add("autoRefreshInterval", spnAutoRefreshInterval.getValue())
					.build()
		);
		
		if (scene != null)
		{
			Scene mainScene = RATrackerApplication.getScene();
			FXMLLoader loader = (FXMLLoader) mainScene.getUserData();
			MainController controller = (MainController) loader.getController();
			
			// Aplicar estilo
			Styles.getInstance()
				.selectStyle(cbTheme.getValue())
				.apply(scene)
				.apply(btnApply.getScene());

			// Atualizar janelas
			controller.update();
		}
		
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */	
	
	/**
	 * Checks whether the Auto Refresh Interval value hits below
	 * the reccomended interval to prevent slow performance.<br>
	 * If the value is below the reccomended, a warning text will be shown
	 * to the user. Else, this warning hides again.
	 * @param interval The interval value selected.
	 */
	private void toggleWarningLabel(int interval)
	{
		lblWarning.setVisible(interval < WARNING_LABEL_LIMIT);
	}

}
