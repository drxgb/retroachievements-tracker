package com.drxgb.ratracker.controller.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;

import com.drxgb.ratracker.model.service.ApiService;
import com.drxgb.ratracker.model.service.MainService;
import com.drxgb.ratracker.util.annotation.SettingsGroup;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The base controller to all the application custom views.<br>
 * When we say <em>"custom view"</em>, we refer to the custom windows called
 * from the main application view.<br>
 * In this version we have the following custom views:
 * <ul>
 * 	<li>Game Info</li>
 * 	<li>User Stats</li>
 * 	<li>Unlocked Achievements</li>
 * 	<li>Next Achievement</li>
 * 	<li>Completed Games</li>
 * </ul>
 * These views could be changed along the time either adding or removing
 * a custom view.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ViewInterface
 */
public abstract class ViewController implements ViewInterface
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * A name to be mapped to a setting object.
	 */
	protected String settingsGroup;
	
	/**
	 * A scene where this controller will be placed.
	 */
	protected Scene scene;
	
	/**
	 * The main area to render the view. It also must be the scene root.
	 */
	protected GridPane paneMain = new GridPane();
	
	/**
	 * List of keys retrieved from its settings group.
	 */
	protected List<String> keys = new ArrayList<>();
	
	 
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The API service to estabilish communication to the server.
	 */
	protected ApiService apiService;
	
	/**
	 * The application service.
	 */
	protected MainService mainService;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Create the controller but it's still not initialized.
	 */
	public ViewController()
	{
		defineSettingsGroup();
		onCreate();
		scene = new Scene(paneMain);
		scene.setUserData(this);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	@Override
	public void onCreate()
	{}

	
	/**
	 * Prepare the controller before the view rendering.
	 */
	public void init()
	{
		initializeKeys();
		onInit();
	}
	
	
	/**
	 * Called when the view needs to be refreshed.
	 */
	public void update()
	{
		paneMain.getChildren().clear();
		paneMain.setHgap(12.0);
		paneMain.setVgap(8.0);
		onUpdate();
		if (scene != null)
		{
			Stage stage = (Stage) scene.getWindow();
			if (stage != null)
				stage.sizeToScene();
		}
	}	
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public Scene getScene()
	{
		return scene;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** SETTERS ***
	 * ===========================================================
	 */
	
	public void setApiService(ApiService apiService)
	{
		this.apiService = apiService;
	}
	

	public void setMainService(MainService mainService)
	{
		this.mainService = mainService;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Initialize the keys related to its settings group.
	 */
	private void initializeKeys()
	{
		Iterator<String> it = getPhrases().keySet().iterator();
		while (it.hasNext())
			keys.add(it.next());
	}
	
	
	/**
	 * Set dynamically the settings group name to map the JSON properties
	 * and the model instance.
	 */
	private void defineSettingsGroup()
	{
		if (getClass().isAnnotationPresent(SettingsGroup.class))
		{
			settingsGroup = 
					((SettingsGroup) getClass().getAnnotation(SettingsGroup.class)).value();
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PROTECTED METHODS ***
	 * ===========================================================
	 */	
	
	/**
	 * Retrieves the settings from the JSON to its settings group.
	 * @return A JSON containing the view settings scoped by a group.
	 */
	protected JsonObject getViewSettings()
	{
		return mainService.getSettings()
				.getJsonObject("view")
				.getJsonObject(settingsGroup);
	}
	
	
	/**
	 * Retrieves the phrases from the JSON to its settings group.
	 * @return A JSON containing the phrases scoped by a group.
	 */
	protected JsonObject getPhrases()
	{
		return mainService.getSettings()
				.getJsonObject("phrases")
				.getJsonObject(settingsGroup);
	}
	
	
	/**
	 * Retrieves the layout view from its settings group.
	 * @return A JSON containing the layout scoped by a settings group.
	 */
	protected JsonObject getLayout()
	{
		return getViewSettings().getJsonObject("layout");
	}
	
	
	/**
	 * Retrieves an array of IDs that represents a field that will be
	 * shown on the custom view.
	 * @return The selected fields IDs that will show on the view.
	 */
	protected JsonArray getSelectedFields()
	{
		return getViewSettings().getJsonArray("fields");
	}
	
	
	/**
	 * Called when the icon area needs to be refreshed.
	 * @param pane The area placed the icon.
	 * @param icon The icon to be refreshed.
	 */
	protected void updateIconPane(Pane pane, ImageView icon)
	{
		pane.getChildren().clear();
		pane.getChildren().add(icon);
		icon.setPreserveRatio(true);
	}
	

	/**
	 * Called when the fields needes to de refreshed on the view.
	 * @param pane The area placed the fields.
	 */
	protected void updateFieldsPane(GridPane pane)
	{
		JsonObject phrases = getPhrases();
		JsonObject layout = getLayout();
		JsonArray selectedFields = getSelectedFields();
		
		pane.getChildren().clear();
		pane.setHgap(24.0);		
		for (JsonNumber n : selectedFields.getValuesAs(JsonNumber.class))
		{
			final int index = n.intValue();
			final String key = keys.get(index);

			if (phrases.containsKey(key))
			{
				writeField(key, pane.getRowCount(), pane);
			}
		}
		pane.getChildren().forEach(n -> {
			if (GridPane.getColumnIndex(n) % 2 == 1)
				GridPane.setHgrow(n, Priority.ALWAYS);
		});
		paneMain.setStyle("-fx-background-color: " + layout.getString("background") + ";");
	}
	
	
	/**
	 * Write the field name inside an area.
	 * @param key The field key.
	 * @param row The row index where the field will be written.
	 * @param gridPane The area that the field will be written.
	 */
	protected void writeField(String key, int row, GridPane gridPane)
	{
		JsonObject phrases = getPhrases();
		JsonObject layout = getLayout();
		Label lblKey = new Label();
		StringBuilder sb = new StringBuilder();
		final String fontName = layout.getString("font");
		final double fontSize = layout.getJsonNumber("size").doubleValue();
		final String keyName = phrases.getString(key);
		
		sb.append(keyName)
			.append(keyName != null ? ": " : "");

		lblKey.setText(sb.toString());
		lblKey.setTextFill(Color.web(layout.getString("foreground")));
		lblKey.setFont(new Font(fontName, fontSize));

		Node value = writeValue(lblKey, key);
		GridPane.setHgrow(value, Priority.ALWAYS);
		gridPane.addRow(row, lblKey, value);
	}
	
	
	/**
	 * Create a <code>Node</code> to be placed on the view.
	 * @param lblKey The label node that contains the field key name written.
	 * You may use it to get its styles like text fill, color, fonts, etc.
	 * @param key The field key.
	 * @return A rendered node to be placed on the view that represents the
	 * field value.
	 */
	protected Node writeValue(Label lblKey, String key)
	{
		Label lblValue = new Label();
		lblValue.setText(onFieldWrite(key));
		lblValue.setTextFill(lblKey.getTextFill());
		lblValue.setFont(lblKey.getFont());
		return lblValue;
	}
	
	
	/**
	 * Creates and render the view.
	 * @param parent The main area to place the view.
	 * @param pnIcon Area that places the icon.
	 * @param pnFields Area that places the fields.
	 */
	protected void mountView(GridPane parent, Pane pnIcon, Pane pnFields)
	{	
		final int displayPosition = getViewSettings().getInt("display");
		final int row = parent.getRowCount();
		
		switch (displayPosition)
		{
		case 0:
			parent.addRow(row, pnIcon, pnFields);
			break;
		case 1:
			parent.addRow(row, pnFields, pnIcon);
			break;
		case 2:
			parent.addRow(row, pnFields);
			break;
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** ABSTRACT PROTECTED METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Retrieve the icon to be rendered on the view.
	 * @return An icon to be placed on the view.
	 */
	protected abstract ImageView getIcon();
	
	/**
	 * Called when the field is being written on the view.
	 * @param key The field key.
	 * @return A text assembled with the field content.
	 */
	protected abstract String onFieldWrite(String key);
}
