package com.drxgb.ratracker.controller;

import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.drxgb.ratracker.factory.JsonSettingsPaneFactory;
import com.drxgb.ratracker.factory.LayoutJsonSettingsPaneFactory;
import com.drxgb.ratracker.factory.LayoutSettingsPanelFactory;
import com.drxgb.ratracker.factory.SettingsPanelFactory;
import com.drxgb.ratracker.factory.WindowJsonSettingsPaneFactory;
import com.drxgb.ratracker.factory.WindowSettingsPanelFactory;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller to load the custom view settings.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ApplicationSettingsController
 */
public class ViewSettingsController extends ApplicationSettingsController
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTS ***
	 * ===========================================================
	 */
	
	/**
	 * The minimum accepted unlocked achievements.
	 */
	public static final int MIN_UNLOCKED_ACHIVEMENTS = 1;
	
	/**
	 * The maximum accepted unlocked achievements.
	 */
	public static final int MAX_UNLOCKED_ACHIVEMENTS = 20;
	
	/**
	 * The minimum accepted completed games.
	 */
	public static final int MIN_COMPLETED_GAMES = 1;
	
	/**
	 * The maximum accepted completed games.
	 */
	public static final int MAX_COMPLETED_GAMES = 20;	
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Load the Game Info tab panel.
	 * @param settings The JSON Object settings.
	 * @param pane The area containing the tab content.
	 */
	private void loadGameInfo(JsonObject settings, Pane pane)
	{
		ObservableList<Toggle> iconDisplayOptions = ((RadioButton) pane.getChildren().get(1))
					.getToggleGroup()
					.getToggles();
		iconDisplayOptions.get(settings.getInt("display", 0)).setSelected(true);
	}
	
	
	/**
	 * Load the User Stats tab panel.
	 * @param settings The JSON Object settings.
	 * @param pane The area containing the tab content.
	 */
	private void loadUserStats(JsonObject settings, Pane pane)
	{
		ObservableList<Toggle> avatarDisplayOptions = ((RadioButton) pane.getChildren().get(1))
				.getToggleGroup()
				.getToggles();		
		avatarDisplayOptions.get(settings.getInt("display", 0)).setSelected(true);
	}

	
	/**
	 * Load the Unlocked Achievements tab panel.
	 * @param settings The JSON Object settings.
	 * @param pane The area containing the tab content.
	 */
	@SuppressWarnings("unchecked")
	private void loadUnlockedAchievements(JsonObject settings, Pane pane)
	{
		ObservableList<Toggle> badgeDisplayOptions = ((RadioButton) pane.getChildren().get(1))
				.getToggleGroup()
				.getToggles();
		Spinner<Integer> spnAchievements = (Spinner<Integer>) pane.getChildren().get(5);
		final int showAchievements = settings.getInt("showAchievements");
		
		spnAchievements.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(
						MIN_UNLOCKED_ACHIVEMENTS, 
						MAX_UNLOCKED_ACHIVEMENTS, 
						showAchievements, 
						1
				)
		);
		badgeDisplayOptions.get(settings.getInt("display", 0)).setSelected(true);
	}

	
	/**
	 * Load the Next Achievement tab panel.
	 * @param settings The JSON Object settings.
	 * @param pane The area containing the tab content.
	 */
	private void loadNextAchievement(JsonObject settings, Pane pane)
	{
		ObservableList<Toggle> badgeDisplayOptions = ((RadioButton) pane.getChildren().get(1))
				.getToggleGroup()
				.getToggles();
		ObservableList<Toggle> wonDisplayOptions = ((RadioButton) pane.getChildren().get(5))
				.getToggleGroup()
				.getToggles();

		badgeDisplayOptions.get(settings.getInt("display", 0)).setSelected(true);
		wonDisplayOptions.get(settings.getInt("wonByDisplay", 0)).setSelected(true);
	}

	
	/**
	 * Load the Completed Games tab panel.
	 * @param settings The JSON Object settings.
	 * @param pane The area containing the tab content.
	 */
	@SuppressWarnings("unchecked")
	private void loadCompletedGames(JsonObject settings, Pane pane)
	{
		ObservableList<Toggle> iconDisplayOptions = ((RadioButton) pane.getChildren().get(1))
				.getToggleGroup()
				.getToggles();
		ObservableList<Toggle> wonDisplayOptions = ((RadioButton) pane.getChildren().get(8))
			.getToggleGroup()
			.getToggles();
		Spinner<Integer> spnGames = (Spinner<Integer>) pane.getChildren().get(5);
		CheckBox chkMastered = (CheckBox) pane.getChildren().get(7);
		final int showGames = settings.getInt("showGames");
		
		iconDisplayOptions.get(settings.getInt("display", 0)).setSelected(true);
		wonDisplayOptions.get(settings.getInt("wonByDisplay", 0)).setSelected(true);
		spnGames.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(
						MIN_COMPLETED_GAMES,
						MAX_COMPLETED_GAMES,
						showGames,
						1
				)
		);
		chkMastered.setSelected(settings.getBoolean("mastered"));
	}	
	
	
	/**
	 * Store the Game Info settings.
	 * @param obj The settings object.
	 * @return A new JSON object with the changes.
	 */
	private JsonObject saveGameInfo(JsonObject obj)
	{
		ObservableList<Node> panes = paneGameInfo.getChildren();
		Pane displayPane = (Pane) panes.get(0);		
		ToggleGroup group = ((RadioButton) displayPane.getChildren().get(1)).getToggleGroup();
		JsonObjectBuilder builder = Json.createObjectBuilder(obj);
		
		builder.add("display", group.getToggles().indexOf(group.getSelectedToggle()))
			.add("fields", getSelectedFields((Pane) panes.get(1)));
		addCommonSettings(builder, obj, panes);
		
		return builder.build();
	}
	
	
	/**
	 * Store the User Stats settings.
	 * @param obj The settings object.
	 * @return A new JSON object with the changes.
	 */
	private JsonObject saveUserStats(JsonObject obj)
	{
		ObservableList<Node> panes = paneUserStats.getChildren();
		Pane displayPane = (Pane) panes.get(0);		
		ToggleGroup group = ((RadioButton) displayPane.getChildren().get(1)).getToggleGroup();
		JsonObjectBuilder builder = Json.createObjectBuilder(obj);
		
		builder.add("display", group.getToggles().indexOf(group.getSelectedToggle()))
			.add("fields", getSelectedFields((Pane) panes.get(1)));
		addCommonSettings(builder, obj, panes);
		
		return builder.build();
	}
	
	
	/**
	 * Store the Unlocked Achievements settings.
	 * @param obj The settings object.
	 * @return A new JSON object with the changes.
	 */
	@SuppressWarnings("unchecked")
	private JsonObject saveUnlockedAchievements(JsonObject obj)
	{
		ObservableList<Node> panes = paneUnlockedAchievements.getChildren();
		Pane displayPane = (Pane) panes.get(0);		
		ToggleGroup group = ((RadioButton) displayPane.getChildren().get(1)).getToggleGroup();
		Spinner<Integer> spnAchievements = (Spinner<Integer>) displayPane.getChildren().get(5);
		JsonObjectBuilder builder = Json.createObjectBuilder(obj);
		
		builder.add("display", group.getToggles().indexOf(group.getSelectedToggle()))
			.add("showAchievements", spnAchievements.getValue())
			.add("fields", getSelectedFields((Pane) panes.get(1)));
		addCommonSettings(builder, obj, panes);
		
		return builder.build();
	}
	
	
	/**
	 * Store the Next Achievement settings.
	 * @param obj The settings object.
	 * @return A new JSON object with the changes.
	 */
	private JsonObject saveNextAchievement(JsonObject obj)
	{
		ObservableList<Node> panes = paneNextAchievement.getChildren();
		Pane displayPane = (Pane) panes.get(0);		
		ToggleGroup group = ((RadioButton) displayPane.getChildren().get(1)).getToggleGroup();
		ToggleGroup wonGroup = ((RadioButton) displayPane.getChildren().get(5)).getToggleGroup();
		JsonObjectBuilder builder = Json.createObjectBuilder(obj);
		
		builder.add("display", group.getToggles().indexOf(group.getSelectedToggle()))
			.add("wonByDisplay", wonGroup.getToggles().indexOf(wonGroup.getSelectedToggle()))
			.add("fields", getSelectedFields((Pane) panes.get(1)));
		addCommonSettings(builder, obj, panes);

		return builder.build();
	}
	
	
	/**
	 * Store the Completed Games settings.
	 * @param obj The settings object.
	 * @return A new JSON object with the changes.
	 */
	@SuppressWarnings("unchecked")
	private JsonObject saveCompletedGames(JsonObject obj)
	{
		ObservableList<Node> panes = paneCompletedGames.getChildren();
		Pane displayPane = (Pane) panes.get(0);		
		ToggleGroup group = ((RadioButton) displayPane.getChildren().get(1)).getToggleGroup();
		Spinner<Integer> spnGames = (Spinner<Integer>) displayPane.getChildren().get(5);
		ToggleGroup wonGroup = ((RadioButton) displayPane.getChildren().get(8)).getToggleGroup();
		CheckBox chkMastered = (CheckBox) displayPane.getChildren().get(7);
		JsonObjectBuilder builder = Json.createObjectBuilder(obj);
			
		builder.add("display", group.getToggles().indexOf(group.getSelectedToggle()))
			.add("showGames", spnGames.getValue())
			.add("wonByDisplay", wonGroup.getToggles().indexOf(wonGroup.getSelectedToggle()))
			.add("mastered", chkMastered.isSelected())
			.add("fields", getSelectedFields((Pane) panes.get(1)));
		addCommonSettings(builder, obj, panes);

		return builder.build();
	}
	
	
	/**
	 * Add common settings from the tabs.
	 * @param builder The JSON object builder.
	 * @param obj The JSON object.
	 * @param children The pane where the settings are.
	 */
	private void addCommonSettings(JsonObjectBuilder builder, JsonObject obj, ObservableList<Node> children)
	{
		JsonSettingsPaneFactory layoutObjectFactory = new LayoutJsonSettingsPaneFactory();
		JsonSettingsPaneFactory windowObjectFactory = new WindowJsonSettingsPaneFactory();
		JsonObject layoutSettings = layoutObjectFactory.makeObject(
				obj.getJsonObject("layout"),
				(Pane) ((TitledPane) children.get(2)).getContent()
		);
		JsonObject windowSettings = windowObjectFactory.makeObject(
				obj.getJsonObject("window"),
				(Pane) ((TitledPane) children.get(3)).getContent()
				);
		
		builder.add("layout", layoutSettings);
		builder.add("window", windowSettings);
	}

	
	/**
	 * Load the pane containing the selected fields.
	 * @param settings A JSON object with settings.
	 * @param pane Area to place the fields list.
	 * @param fields A list containing the fields
	 */
	@SuppressWarnings("unchecked")
	private void loadFieldsPane(JsonArray settings, Pane pane, List<FieldItem> fields)
	{
		Queue<Integer> selectedFields = generateSelectedFieldsStack(settings);
		ListView<FieldItem> lstSelected = (ListView<FieldItem>) pane.getChildren().get(1);
		ListView<FieldItem> lstUnselected = (ListView<FieldItem>) pane.getChildren().get(2);
		VBox vbActions = (VBox) pane.getChildren().get(3);
		Button btnUp = (Button) vbActions.getChildren().get(0);
		Button btnAdd = (Button) vbActions.getChildren().get(1);
		Button btnRemove = (Button) vbActions.getChildren().get(2);		
		Button btnDown = (Button) vbActions.getChildren().get(3);
		
		Consumer<Integer> swapItems = n -> {
			final int index = lstSelected.getSelectionModel().getSelectedIndex();
			final int next = index + n;
			ObservableList<FieldItem> obsList = lstSelected.getItems();
			
			if (index >= 0 && next >= 0 && next < obsList.size())
			{
				Collections.swap(obsList, index, next);
				lstSelected.getSelectionModel().select(next);
				activateApplyButton(null);
			}
		};
		
		while (!selectedFields.isEmpty())
		{
			final int selectedIndex = selectedFields.poll();
			Optional<FieldItem> opItem = fields.stream()
					.filter(f -> f.getId() == selectedIndex)
					.findFirst();
			if (opItem.isPresent())
			{
				FieldItem item = opItem.get();
				fields.remove(item);
				lstSelected.getItems().add(item);
			}
		}
		
		fields.forEach(field -> lstUnselected.getItems().add(field));		
		btnAdd.setOnAction(e -> {
			final int index = lstUnselected.getSelectionModel().getSelectedIndex();
			
			if (index >= 0)
			{			
				FieldItem item = lstUnselected.getItems().get(index);				
				lstUnselected.getItems().remove(item);
				lstSelected.getItems().add(item);
				activateApplyButton(null);
			}
		});
		btnRemove.setOnAction(e -> {
			final int index = lstSelected.getSelectionModel().getSelectedIndex();
			
			if (index >= 0)
			{
				FieldItem item = lstSelected.getItems().get(index);
				lstSelected.getItems().remove(item);
				lstUnselected.getItems().add(item);
				Collections.sort(lstUnselected.getItems());
				activateApplyButton(null);
			}
		});		
		btnUp.setOnAction(e -> swapItems.accept(-1));
		btnDown.setOnAction(e -> swapItems.accept(1));
	}

	
	/**
	 * Load the tab custom fields.
	 * @param settings A JSON Object with settings.
	 * @param phrases A JSON Object with phrases.
	 * @param pane Area to place these custom fields. 
	 * @param loadBasicSettings Callback to place custom fields.
	 */
	private void loadTabSettings(
			JsonObject settings,
			JsonObject phrases,
			Pane pane,
			BiConsumer<JsonObject, Pane> loadBasicSettings
		)
	{
		ObservableList<Node> subPanes = pane.getChildren();
		List<FieldItem> fields = new ArrayList<>();
		JsonParser parser = Json.createParser(new StringReader(phrases.toString()));
		SettingsPanelFactory layoutPanelFactory = new LayoutSettingsPanelFactory();
		SettingsPanelFactory windowPanelFactory = new WindowSettingsPanelFactory();
		
		int i = 0;
		while (parser.hasNext())
		{
			Event event = parser.next();
			if (event == Event.VALUE_STRING)
				fields.add(new FieldItem(i++, parser.getString()));
		}
		
		loadBasicSettings.accept(settings, (Pane) subPanes.get(0));
		loadFieldsPane(settings.getJsonArray("fields"), (Pane) subPanes.get(1), fields);		
		pane.getChildren().add(layoutPanelFactory.makePanel(settings.getJsonObject("layout")));
		pane.getChildren().add(windowPanelFactory.makePanel(settings.getJsonObject("window")));
		addApplyActionListener(pane);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** STATIC PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Populate a queue containing the selected fields finding by their IDs.
	 * @param fields A JSON Object with the selected fields IDs.
	 * @return A <code>Queue</code> containing the selected fields
	 */
	private static Queue<Integer> generateSelectedFieldsStack(JsonArray fields)
	{
		Queue<Integer> queue = new ArrayDeque<>();
		
		for (JsonNumber f : fields.getValuesAs(JsonNumber.class))
			queue.offer(f.intValue());
		
		return queue;
	}
	
	
	/**
	 * Retrieves the selected fields from a <code>ListView</code> to an array.
	 * @param pane Area where the list is placed.
	 * @return A <code>JsonArray</code> containing the selected fields IDs.
	 */
	@SuppressWarnings("unchecked")
	private static JsonArray getSelectedFields(Pane pane)
	{
		ListView<FieldItem> lstSelected = (ListView<FieldItem>) pane.getChildren().get(1);		
		return Json.createArrayBuilder(
					lstSelected
						.getItems()
						.stream()
						.map(f -> f.getId())
						.collect(Collectors.toList())
				)
				.build();
	}

	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	protected void onInit()
	{		
		JsonObject viewSettings = mainService.getSettings().getJsonObject("view");
		JsonObject phrases = mainService.getSettings().getJsonObject("phrases");
		
		loadTabSettings(
				viewSettings.getJsonObject("gameInfo"),
				phrases.getJsonObject("gameInfo"),
				paneGameInfo,
				this::loadGameInfo
		);
		loadTabSettings(
				viewSettings.getJsonObject("userStats"),
				phrases.getJsonObject("userStats"),
				paneUserStats,
				this::loadUserStats
		);
		loadTabSettings(
				viewSettings.getJsonObject("unlockedAchievements"),
				phrases.getJsonObject("unlockedAchievements"),
				paneUnlockedAchievements,
				this::loadUnlockedAchievements
		);
		loadTabSettings(
				viewSettings.getJsonObject("nextAchievement"),
				phrases.getJsonObject("nextAchievement"),
				paneNextAchievement,
				this::loadNextAchievement
		);
		loadTabSettings(
				viewSettings.getJsonObject("completedGames"),
				phrases.getJsonObject("completedGames"),
				paneCompletedGames,
				this::loadCompletedGames
		);
	}
	
	@Override
	protected void onSave(JsonObjectBuilder builder, JsonObject settings)
	{
		JsonObject viewSettings = settings.getJsonObject("view");
		builder.add("view",
				Json.createObjectBuilder(viewSettings)
					.add("gameInfo", saveGameInfo(viewSettings.getJsonObject("gameInfo")))
					.add("userStats", saveUserStats(viewSettings.getJsonObject("userStats")))
					.add("unlockedAchievements", saveUnlockedAchievements(viewSettings.getJsonObject("unlockedAchievements")))
					.add("nextAchievement", saveNextAchievement(viewSettings.getJsonObject("nextAchievement")))
					.add("completedGames", saveCompletedGames(viewSettings.getJsonObject("completedGames")))
					.build()
		);
	}


	/*
	 * ===========================================================
	 * 			*** PRIVATE CLASSES ***
	 * ===========================================================
	 */
	
	/**
	 * Utility class that represents a field item.
	 * @author Dr.XGB
	 * @version 1.0.0
	 * @see Comparable
	 * @see FieldItem
	 */
	private class FieldItem implements Comparable<FieldItem>
	{
		/**
		 * The field ID.
		 */
		private Integer id;
		
		/**
		 * The field title.
		 */
		private String title;
		
		
		/**
		 * Create a field item.
		 * @param id The ID.
		 * @param title The title.
		 */
		public FieldItem(Integer id, String title)
		{
			this.id = id;
			this.title = title;
		}
		
		public Integer getId()
		{
			return id;
		}

		@Override
		public String toString()
		{
			return title;
		}

		@Override
		public int compareTo(FieldItem o)
		{			
			return id - o.getId();
		}		
	}
}
