package com.drxgb.ratracker.model.service;

import static com.drxgb.ratracker.RATrackerApplication.SETTINGS_FILE;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;

import com.drxgb.ratracker.controller.view.ViewController;

import javafx.stage.Stage;

/**
 * The main application service singleton. It is used to store the user settings
 * and controls the custom views.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class MainService
{	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The singleton instance.
	 */
	private static volatile MainService instance;
	
	/**
	 * The user settings.
	 */
	private JsonObject settings;
	
	 
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The custom views windows.<br>
	 * They must have a identification key and the own window
	 * instance.
	 */
	private Map<String, Stage> stages = new HashMap<>();
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates the singleton instance and load the user settings.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	private MainService() throws IOException
	{
		createSettings(SETTINGS_FILE);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public static MainService getInstance() throws IOException
	{
		if (instance == null)
			instance = new MainService();
		return instance;
	}


	public Map<String, Stage> getStages()
	{
		return stages;
	}


	public JsonObject getSettings()
	{
		return settings;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Refreshes the application window, the custom view windows and
	 * the user settings.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	public void refresh() throws IOException
	{
		createSettings(SETTINGS_FILE);
		for (Stage stage : stages.values())
		{
			ViewController controller = (ViewController) stage
					.getScene()
					.getProperties()
					.get("controller");
			if (controller != null)
				controller.update();
		}
	}
	
	
	/**
	 * Stores the user settings changes.
	 * @param builder The JSON builder entity.
	 * @throws IOException When the settings file is not found or the output stream fails.
	 */
	public void save(JsonObjectBuilder builder) throws IOException
	{
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(SETTINGS_FILE)))
		{
			Map<String, Boolean> config = new HashMap<>();
			config.put(JsonGenerator.PRETTY_PRINTING, true);
			Json.createWriterFactory(config)
				.createWriter(bos)
				.write(builder.build());
		}
		catch (IOException e)
		{
			throw e;
		}
	}
	
	
	/**
	 * Stores the user settings changes but creates
	 * a fresh JSON builder entity.
	 * @throws IOException When the settings file is not found or the output stream fails.
	 */
	public void save() throws IOException
	{
		save(Json.createObjectBuilder(settings));
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */

	/**
	 * Load the JSON settings file.
	 * @param fileName The file name to be loaded.
	 * @throws IOException When the input stream fails.
	 */
	private void createSettings(String fileName) throws IOException
	{
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName)))
		{
			JsonReader reader = Json.createReader(bis);
			settings = reader.readObject();
		}
		catch (FileNotFoundException e)
		{
			createSettingsFile(fileName);
			createSettings(fileName);
		}
		catch (IOException e)
		{
			throw e;
		}
	}
	
	
	/**
	 * Creates the JSON settings file if it doesn't exist.
	 * @param fileName The file name to be loaded.
	 * @throws IOException When the output stream fails.
	 */
	private void createSettingsFile(String fileName) throws IOException
	{
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName)))
		{
			bos.write((int) '{');
			bos.write((int) '}');
		}
		catch (IOException e)
		{
			throw e;
		}
	}
	
}
