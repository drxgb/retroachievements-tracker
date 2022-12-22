package com.drxgb.ratracker.controller;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Controller to load the Phrases view.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ApplicationSettingsController
 */
public class PhrasesController extends ApplicationSettingsController
{
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Load all the phrases stored in a JSON file and fill
	 * the text inputs respectively.
	 * @param pane The area containing the text inputs.
	 * @param settings A JSON containing the phrases.
	 */
	private void loadPhrases(Pane pane, JsonObject settings)
	{
		List<Node> fields = getTextFields(pane);
		JsonParser parser = Json.createParser(new StringReader(settings.toString()));
		int i = 0;
		while (parser.hasNext() && i < fields.size())
		{
			Event ev = parser.next();
			if (ev == Event.VALUE_STRING)
			{
				TextField txtField = (TextField) fields.get(i++);
				txtField.setText(parser.getString());
			}
		}
		addApplyActionListener(pane);
	}
	
	
	/**
	 * Store the phrases to a JSON file.
	 * @param pane The area containing the text inputs.
	 * @param settings A JSON containing the phrases.
	 * @return A new JSON Object with the updated phrases.
	 */
	private JsonObject savePhrases(Pane pane, JsonObject settings)
	{
		List<Node> fields = getTextFields(pane);
		JsonObjectBuilder builder = Json.createObjectBuilder(settings);
		JsonParser parser = Json.createParser(new StringReader(settings.toString()));
		int i = 0;
		while (parser.hasNext() && i < fields.size())
		{
			Event ev = parser.next();
			if (ev == Event.KEY_NAME)
			{
				TextField txtField = (TextField) fields.get(i++);
				builder.add(parser.getString(), txtField.getText());
			}
		}
		return builder.build();
	}
	
	/*
	 * ===========================================================
	 * 			*** STATIC PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Retrieves the text inputs.
	 * @param pane The area containing the text inputs.
	 * @return A list containing all the pane nodes filtering only the
	 * <code>TextField</code> instances.
	 */
	private static List<Node> getTextFields(Pane pane)
	{
		return pane.getChildren()
				.stream()
				.filter(n -> n instanceof TextField)
				.collect(Collectors.toList());
	}
	
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	protected void onInit()
	{
		JsonObject phrases = mainService.getSettings().getJsonObject("phrases");		
		loadPhrases(paneGameInfo, phrases.getJsonObject("gameInfo"));
		loadPhrases(paneUserStats, phrases.getJsonObject("userStats"));
		loadPhrases(paneUnlockedAchievements, phrases.getJsonObject("unlockedAchievements"));
		loadPhrases(paneNextAchievement, phrases.getJsonObject("nextAchievement"));
		loadPhrases(paneCompletedGames, phrases.getJsonObject("completedGames"));
	}

	@Override
	protected void onSave(JsonObjectBuilder builder, JsonObject settings)
	{
		JsonObject phrases = settings.getJsonObject("phrases");
		builder.add(
				"phrases",
				Json.createObjectBuilder(phrases)
					.add("gameInfo", savePhrases(paneGameInfo, phrases.getJsonObject("gameInfo")))
					.add("userStats", savePhrases(paneUserStats, phrases.getJsonObject("userStats")))
					.add("unlockedAchievements", savePhrases(paneUnlockedAchievements, phrases.getJsonObject("unlockedAchievements")))
					.add("nextAchievement", savePhrases(paneNextAchievement, phrases.getJsonObject("nextAchievement")))
					.add("completedGames", savePhrases(paneCompletedGames, phrases.getJsonObject("completedGames")))
					.build()
			);
	}

}
