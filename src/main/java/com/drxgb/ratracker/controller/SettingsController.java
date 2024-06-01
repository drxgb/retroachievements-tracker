package com.drxgb.ratracker.controller;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.drxgb.ratracker.model.service.MainService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * A base controller containing the OK, Cancel and Apply buttons.<br>
 * Usually this controller is applied to settings views.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Initializable
 */
public abstract class SettingsController implements Initializable
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The Apply button.
	 */
	@FXML protected Button btnApply;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The main application service.
	 */
	protected MainService mainService;
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Called when the user clicks on the OK button.
	 * @param ev The handled event.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	@FXML
	public void onBtnOkAction(ActionEvent ev) throws IOException
	{
		save();
		onBtnCancelAction(ev);
	}
	
	
	/**
	 * Called when the user clicks on the Cancel button.
	 * @param ev The handled event.
	 */
	@FXML
	public void onBtnCancelAction(ActionEvent ev)
	{
		Button btn = (Button) ev.getTarget();
		Stage stage = (Stage) btn.getScene().getWindow();
		stage.close();
	}
	
	
	/**
	 * Called when the user clicks on the Apply button.
	 * @param ev The handled event.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	@FXML
	public void onBtnApplyAction(ActionEvent ev) throws IOException
	{
		Button btn = (Button) ev.getTarget();
		btn.setDisable(true);
		save();
	}

	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		try
		{
			mainService = MainService.getInstance();
			onInit();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * ===========================================================
	 * 			*** PROTECTED METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Activate the apply button when a change is made.
	 * @param ev The handled event.
	 */
	protected void activateApplyButton(EventObject ev)
	{
		btnApply.setDisable(false);
	}
	
	
	/**
	 * Activate the apply button when a change is made
	 * but an event is not required.
	 */
	protected void activateApplyButton()
	{
		activateApplyButton(null);
	}
	
	
	/**
	 * Apply action listeners when changes were made to
	 * activate the Apply button.
	 * @param pane The area containing the fields.
	 */
	protected void addApplyActionListener(Pane pane)
	{
		pane.getChildren().forEach(node -> {			
			if (node instanceof ButtonBase)
			{
				ButtonBase btn = (ButtonBase) node;
				btn.setOnAction(this::activateApplyButton);
			}
			if (node instanceof Spinner<?>)
			{
				Spinner<?> spn = (Spinner<?>) node;
				spn.valueProperty().addListener(
						(obs, oldValue, newValue) -> activateApplyButton()
				);
			}
			if (node instanceof ComboBoxBase<?>)
			{
				ComboBoxBase<?> cb = (ComboBoxBase<?>) node;
				cb.valueProperty().addListener(
						(obs, oldValue, newValue) -> activateApplyButton()
				);
			}
			if (node instanceof TextInputControl)
			{
				TextInputControl txt = (TextInputControl) node;
				txt.textProperty()
					.addListener((obs, oldValue, newValue) -> activateApplyButton());
			}
			if (node instanceof CheckBox)
			{
				CheckBox chk = (CheckBox) node;
				chk.selectedProperty()
					.addListener((obs, oldValue, newValue) -> activateApplyButton());
			}
			if (node instanceof Pane)
			{
				addApplyActionListener((Pane) node);
			}			
			if (node instanceof TitledPane)
			{
				TitledPane titledPane = (TitledPane) node;
				addApplyActionListener((Pane) titledPane.getContent());
			}
		});
	}
	
	
	/**
	 * Apply changes and store to a JSON file.
	 * @throws IOException When the settings file is not found or the stream fails. 
	 */
	protected void save() throws IOException
	{		
		JsonObject settings = mainService.getSettings();
		JsonObjectBuilder builder = Json.createObjectBuilder(settings);

		onSave(builder, settings);
		mainService.save(builder);
		mainService.refresh();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** ABSTRACT PROTECTED METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Called when the controller is initialized.
	 */
	protected abstract void onInit();
	
	/**
	 * Called when the changes is being saved.
	 * @param builder The JSON builder.
	 * @param settings The JSON Object conatining the settings.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	protected abstract void onSave(JsonObjectBuilder builder, JsonObject settings)
			throws IOException;
}