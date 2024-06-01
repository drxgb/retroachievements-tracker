package com.drxgb.ratracker;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import com.drxgb.javafxutils.StageFactory;
import com.drxgb.ratracker.controller.LoginController;
import com.drxgb.ratracker.controller.MainController;
import com.drxgb.ratracker.model.service.ApiService;
import com.drxgb.ratracker.model.service.MainService;
import com.drxgb.ratracker.util.ErrorDialog;
import com.drxgb.ratracker.util.Styles;
import com.drxgb.util.Report;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The main application container.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class RATrackerApplication extends Application
{
	
	/*
	 * ===========================================================
	 * 			*** CONSTANTS ***
	 * ===========================================================
	 */
	
	/**
	 * The application name.
	 */
	public static final String NAME = "XGB's RA Tracker";
	
	/**
	 * The current application version.
	 */
	public static final String VERSION = "1.0.1";
	
	/**
	 * RetroAchievements web link.
	 */
	public static final String RA_URL = "https://retroachievements.org";
	
	/**
	 * RetroAchievements AWS link.<br>
	 * Used to store achievements badges.
	 */
	public static final String RA_AWS_URL = "https://s3-eu-west-1.amazonaws.com/i.retroachievements.org/";
	
	/**
	 * user setting file name.
	 */
	public static final String SETTINGS_FILE = "settings.json";
	
	/**
	 * Default style.
	 */
	public static final String FALLBACK_THEME = "Light";
	
	/**
	 * Default Auto Refresh interval.
	 */
	public static final Integer FALLBACK_AUTO_REFRESH_INTERVAL = 15;
	
	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The own application instance.
	 */
	private static RATrackerApplication app;
	
	/**
	 * The main scene.
	 */
	private static Scene scene;
	
	/**
	 * Default date format used by the RetroAchievements API.
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The API service.
	 */
	private static volatile ApiService apiService = new ApiService();
	
	
	/*
	 * ===========================================================
	 * 			*** STATIC PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Initialize the application.<br>
	 * Must be called by the main class.
	 * @param args The command lines arguments.
	 */
	public static void start(String[] args)
	{
		launch(args);
	}
	
	
	/**
	 * Gets the application instance.
	 * @return The application instance.
	 */
	public static RATrackerApplication getInstance()
	{
		return app;
	}

	
	/**
	 * Gets the main scene.
	 * @return The main scene.
	 */
	public static Scene getScene()
	{
		return scene;
	}
	
	/**
	 * Gets the API service.
	 * @return The API service.
	 */
	public static ApiService getApiService()
	{
		return apiService;
	}
	
	/**
	 * Gets the RetroAchievements default date format.
	 * @return The default date format.
	 */
	public static SimpleDateFormat getDateFormat()
	{
		return sdf;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	public void start(Stage stage) throws Exception
	{
		app = this;

		try
		{
			Class<RATrackerApplication> appClass = RATrackerApplication.class;			
			String theme = MainService.getInstance()
					.getSettings()
					.getJsonObject("general")
					.getString("theme", FALLBACK_THEME);
			Styles styles = Styles.getInstance()
					.load("resources/style/")
					.selectStyle(theme);
			FXMLLoader mainLoader = new FXMLLoader(appClass.getResource("/resources/view/MainView.fxml"));
			Pane root = (VBox) mainLoader.load();
			InputStream icon = appClass.getResourceAsStream("/resources/favicon.png");
			MainController mainController = mainLoader.getController();			

			// Carregar janela principal
			scene = new Scene(root);
			scene.setUserData(mainLoader);		
			if (icon != null)
				stage.getIcons().add(new Image(icon));
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle(NAME + " - " + VERSION);
			stage.setOnShown(mainController::onShown);
			stage.setOnCloseRequest(mainController::onCloseRequest);
			stage.setOnHiding(mainController::onCloseRequest);
			ErrorDialog.getInstance()
				.getStage()
				.initOwner(stage);
			
			// Carregar estilos		
			styles.apply(scene);
			
			// Janela do login
			Stage loginStage = StageFactory.openWindow(
					stage,
					appClass.getResource("/resources/view/LoginView.fxml"),
					"Login"
			);
			FXMLLoader loginLoader = (FXMLLoader) loginStage.getScene().getUserData();
			LoginController loginController = (LoginController) loginLoader.getController();
			
			loginController.initFocus();
			loginController.loadSessionProperties();
			loginStage.setOnCloseRequest(loginController::onBtnCancelAction);
			if (icon != null)
				loginStage.getIcons().add(stage.getIcons().get(0));
			loginStage.showAndWait();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			Report.writeErrorLog(e);
			Platform.runLater(() -> {				
				ErrorDialog error = ErrorDialog.getInstance();
				error.updateError(e);
				error.getStage().show();
			});
		}
	}
}
