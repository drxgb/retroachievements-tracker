package com.drxgb.ratracker.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.drxgb.javafxutils.StageFactory;
import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.controller.view.CompletedGamesController;
import com.drxgb.ratracker.controller.view.GameInfoController;
import com.drxgb.ratracker.controller.view.NextAchievementController;
import com.drxgb.ratracker.controller.view.UnlockedAchievementsController;
import com.drxgb.ratracker.controller.view.UserStatsController;
import com.drxgb.ratracker.controller.view.ViewController;
import com.drxgb.ratracker.model.entity.game.Game;
import com.drxgb.ratracker.model.entity.user.User;
import com.drxgb.ratracker.model.service.ApiService;
import com.drxgb.ratracker.model.service.MainService;
import com.drxgb.ratracker.model.service.concurrency.RefreshSessionService;
import com.drxgb.ratracker.util.Styles;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller that shows the main view.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Initializable
 */
public class MainController implements Initializable
{	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	@FXML private ImageView imgAvatar;
	@FXML private Circle crcStatus;
	@FXML private Label lblUserName;
	@FXML private Label lblRank;
	@FXML private Label lblPoints;
	@FXML private Label lblMotto;
	@FXML private Label lblPresenceMessage;
	@FXML private Menu mnThemes;
	
	@FXML private ImageView imgGameIcon;
	@FXML private Label lblTitle;
	@FXML private Label lblConsole;
	@FXML private Label lblAchievements;
	@FXML private Label lblScore;
	@FXML private Label lblProgress;
	
	@FXML private Circle crcUpdate;
	@FXML private ProgressIndicator prgRefresh;
	@FXML private Label lblStatus;
	
	private Color clrOk;
	private Color clrError;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCITAIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The main application service.
	 */
	private MainService mainService;
	
	/**
	 * The API service that consumes the server.
	 */
	private ApiService apiService;
	
	/**
	 * Thread to refresh the session automatically.
	 */
	private RefreshSessionService sessionService;
	
	
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
			apiService = RATrackerApplication.getApiService();
			clrOk = new Color(0, 0.75, 0, 1);
			clrError = Color.RED;
			sessionService = new RefreshSessionService(apiService, this);
			loadThemesMenu();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Refresh the view.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	public void update() throws IOException
	{
		if (
				apiService == null ||
				!apiService.isOpen() ||
				apiService.isConsuming()
			)
			return;
		
		User user = apiService.getUserService().getUser();
		Game lastGame = user.getLastGame();
		String avatarUrl = RATrackerApplication.RA_URL + user.getPicture();
		String gameImageUrl = RATrackerApplication.RA_URL + lastGame.getImage().getIcon();
		
		// User
		imgAvatar.setImage(new Image(avatarUrl));
		lblUserName.setText(user.getName());
		lblRank.setText("#" + user.getRank());
		lblPoints.setText(user.getPoints() + "/" + user.getTruePoints());
		lblMotto.setText(user.getMotto());
		lblPresenceMessage.setText(user.getPresenceMessage());
		setUserStatus(user);
		
		// Game
		imgGameIcon.setImage(new Image(gameImageUrl));
		lblTitle.setText(lastGame.getTitle());
		lblConsole.setText(lastGame.getConsole().getName());
		lblAchievements.setText(
				lastGame.getEarnedAchievements().size() +
				"/" +
				lastGame.getAchievements().size()
		);
		lblScore.setText(
				lastGame.points() +
				"(" + lastGame.hardcorePoints() + ")" +
				"/" +
				lastGame.totalPoints()
		);
		lblProgress.setText(
				((int) (lastGame.progress() * 100)) +
				"% complete"
		);
		
		// Status
		crcUpdate.setVisible(true);
		crcUpdate.setFill(clrOk);
		prgRefresh.setVisible(false);
		lblStatus.setText(getLastUpdateText());
		
		mainService.refresh();
	}
	
	
	/**
	 * Called when the view is shown.
	 * @param ev The handled event.
	 */
	public void onShown(Event ev)
	{
		if (
				sessionService.getState() == State.SUCCEEDED ||
				sessionService.isRunning()
			)
			sessionService.restart();
		else
			sessionService.start();
	}

	
	/**
	 * Called when the user attempts tho close the window.
	 * @param ev The handled event.
	 */
	public void onCloseRequest(Event ev)
	{
		mainService.getStages().forEach((k, v) -> v.close());
		mainService.getStages().clear();
	}
	
	
	/**
	 * Called when the user attempts to refresh the session manually.
	 */
	@FXML
	public synchronized void onMnitRefreshAction()
	{
		if (!apiService.isOpen())
			return;
		
		Thread th = new Thread(() -> {
			try
			{
				apiService.update();
				Platform.runLater(() -> {
					try	{ update();	}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				});
			}
			catch (Throwable e)
			{
				Platform.runLater(() -> {						
					crcUpdate.setFill(clrError);
					prgRefresh.setVisible(false);
					lblStatus.setText("Failed to refresh session.");
				});
			}
		});
		
		// Avisar usuário que sessão está sendo atualizada
		crcUpdate.setVisible(false);
		prgRefresh.setVisible(true);
		lblStatus.setText("Refreshing session...");
		
		// Atualizando sessão
		th.setDaemon(true);
		th.start();
	}
	
	
	/**
	 * Called when the user attempts to close the session.<br>
	 * If the session is being refreshed, it will wait until the
	 * proccess has finished.
	 * @param e The handled event.
	 */
	@FXML
	public synchronized void onMnitEndSessionAction(ActionEvent e)
	{
		Thread th = new Thread(() -> {			
			apiService.endSession();
			Platform.runLater(() -> {				
				Stage stage = (Stage) RATrackerApplication.getScene().getWindow();
				Stage loginStage = mainService.getStages().get("login");
				stage.hide();
				
				if (loginStage != null)
					loginStage.show();
			});
		});
		
		th.setDaemon(true);
		th.start();
		
	}
	
	
	/**
	 * Called when the user opens the General Settings view.
	 * @throws IOException When the FXML Loader cannot find the view file.
	 */
	@FXML
	public void onMnitGeneralSettingsAction() throws IOException
	{
		openSettingsWindow("GeneralSettingsView", "General Settings");
	}

	
	/**
	 * Called when the user opens the View Settings view.
	 * @throws IOException When the FXML Loader cannot find the view file.
	 */
	@FXML
	public void onMnitViewSettingsAction(ActionEvent e) throws IOException
	{
		openSettingsWindow("ViewSettingsView", "View Settings");
	}
	
	
	/**
	 * Called when the user opens the Phrases Settings view.
	 * @throws IOException When the FXML Loader cannot find the view file.
	 */
	@FXML
	public void onMnitPhrasesAction(ActionEvent e) throws IOException
	{
		openSettingsWindow("PhrasesView", "Phrases");
	}
	
	
	/**
	 * Called when the user opens the Game Info view.
	 */
	@FXML
	public void onMnitGameInfoAction() throws IOException
	{		
		openView("gameInfo", "Game Info", new GameInfoController());
	}
	
	
	/**
	 * Called when the user opens the User Stats view.
	 */
	@FXML
	public void onMnitUserStatsAction()
	{
		openView("userStats", "User Stats", new UserStatsController());
	}
	
	
	/**
	 * Called when the user opens the Unlocked Achivements view.
	 */
	@FXML
	public void onMnitUnlockedAchievementsAction()
	{
		openView("unlockedAchievements", "Unlocked Achievements", new UnlockedAchievementsController());
	}
	
	
	/**
	 * Called when the user opens the Next Achievement view.
	 */
	@FXML
	public void onMnitNextAchievementAction()
	{
		openView("nextAchievement", "Next Achievement", new NextAchievementController());
	}
	
	
	/**
	 * Called when the user opens the Completed Games view.
	 */
	@FXML
	public void onMnitCompletedGamesAction()
	{
		openView("completedGames", "Completed Games", new CompletedGamesController());
	}
	
	
	/**
	 * Called when the user opens the About view.
	 * @throws IOException When the FXML Loader cannot find the view file.
	 */
	@FXML
	public void onMnitAboutAction() throws IOException
	{
		openSettingsWindow("AboutView", "About " + RATrackerApplication.NAME);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS***
	 * ===========================================================
	 */
	
	/**
	 * Change the user status between ONLINE and OFFLINE.
	 * @param user The user entity.
	 */
	private void setUserStatus(User user)
	{
		switch (user.getStatus())
		{
		case ONLINE:
			crcStatus.setFill(clrOk);
			break;
		case OFFLINE:
			crcStatus.setFill(clrError);
			break;
		}
	}
	
	
	/**
	 * Assemble the updated session message.
	 * @return A message containing when the session was refreshed.
	 */
	private String getLastUpdateText()
	{
		StringBuilder sb = new StringBuilder();
		String dateFormat = mainService.getSettings()
					.getJsonObject("general")
					.getString("dateFormat", null);
		final SimpleDateFormat sdf = (dateFormat != null)
				? new SimpleDateFormat(dateFormat)
				: RATrackerApplication.getDateFormat();
		final Date now = new Date();
		
		sb.append("Last update at ");
		sb.append(sdf.format(now));
		
		return sb.toString();
	}
	
	
	/**
	 * Creates a custom view window.
	 * @param controller The controller to be loaded.
	 * @param key The window identification key.
	 * @return A window containing the custom view content.
	 */
	private Stage createViewStage(ViewController controller, String key)
	{
		Stage parentStage = (Stage) RATrackerApplication.getScene().getWindow();
		Stage stage = new Stage();
		
		if (key != null)
		{
			mainService.getStages().put(key, stage);
			stage.setOnCloseRequest(ev -> mainService.getStages().remove(key));
		}
		controller.init();		
		Scene scene = controller.getScene();
		stage.resizableProperty().setValue(false);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.getIcons().add(parentStage.getIcons().get(0));
		scene.getProperties().put("controller", controller);
		controller.update();
		return stage;
	}
	
	
	/**
	 * Change the window title according to the application patterns.
	 * @param stage The target window.
	 * @param title The new title.
	 */
	private void setStageTitle(Stage stage, String title)
	{
		Stage parentStage = (Stage) RATrackerApplication.getScene().getWindow();
		stage.setTitle(title + " - " + parentStage.getTitle());
	}
	
	
	/**
	 * Points the services to the custom view controller.
	 * @param controller The target controller.
	 */
	private void setControllerServices(ViewController controller)
	{
		controller.setApiService(apiService);
		controller.setMainService(mainService);
	}
	
	
	/**
	 * Chekc whether the custom view is opened.
	 * @param key The custom view identification key.
	 * @return <code>true</code> if the window is opened or
	 * <code>false</code> if the window was closed or was not opened yet.
	 */
	private boolean viewExists(String key)
	{
		if (mainService.getStages().containsKey(key))
		{
			mainService.getStages().get(key).requestFocus();
			return true;
		}
		return false;
	}
	
	
	/**
	 * Open a fresh settings view.
	 * @param fxmlName The FXML file name without the path.
	 * @param title The window title.
	 * @throws IOException When the FXML file is not found.
	 */
	private void openSettingsWindow(String fxmlName, String title) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		Stage stage = (Stage) RATrackerApplication.getScene().getWindow();
		Stage settingsStage;
		
		sb.append("/resources/view/")
			.append(fxmlName)
			.append(".fxml");
		settingsStage = StageFactory.openWindow(
				stage,
				RATrackerApplication.class.getResource(sb.toString()),
				title,
				true
		);
		settingsStage.getIcons().add(stage.getIcons().get(0));
		settingsStage.initStyle(StageStyle.DECORATED);
		settingsStage.resizableProperty().setValue(false);
		settingsStage.showAndWait();
	}
	
	
	/**
	 * Open a custom view.
	 * @param key The custom view identification key.
	 * @param title The window title.
	 * @param controller The target controller that loads the custom view.
	 */
	private void openView(String key, String title, ViewController controller)
	{
		if (viewExists(key))
			return;
		
		setControllerServices(controller);
		
		Stage stage = createViewStage(controller, key);
		mainService.getStages().put(key, stage);
		setStageTitle(stage, title);
		stage.show();
	}
	
	
	/**
	 * Load the pre-built styles and place on the settings menu.
	 */
	private void loadThemesMenu()
	{
		Styles styles = Styles.getInstance();
		String selectedStyle = styles.getSelectedStyle();
		ToggleGroup group = new ToggleGroup();

		mnThemes.getItems().clear();
		styles.getStyles().forEach((k, v) -> {
			RadioMenuItem item = new RadioMenuItem(k);
			item.setToggleGroup(group);
			item.selectedProperty().addListener((obs, oldValue, newValue) -> {
				try
				{
					Scene scene = RATrackerApplication.getScene();				
					styles.selectStyle(k);				
					if (scene != null)
					{
						styles.apply(scene);
						scene.getWindow().sizeToScene();
						saveTheme(k);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
			item.setSelected(k.equals(selectedStyle));			
			mnThemes.getItems().add(item);
		});
	}
	
	
	/**
	 * Save the selected theme and refresh the windows.
	 * @param name The theme name.
	 * @throws IOException When the settings file is not found or the stream fails.
	 */
	private void saveTheme(String name) throws IOException
	{
		JsonObject settings = mainService.getSettings();
		JsonObjectBuilder builder = Json.createObjectBuilder(settings);
		
		builder.add(
				"general",
				Json.createObjectBuilder(settings.getJsonObject("general"))
					.add("theme", name)
					.build()
		);
		mainService.save(builder);
		mainService.refresh();
	}
}
