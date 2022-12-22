package com.drxgb.ratracker.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.drxgb.javafxutils.DialogBuilder;
import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.exception.ApiException;
import com.drxgb.ratracker.model.entity.Api;
import com.drxgb.ratracker.model.entity.Session;
import com.drxgb.ratracker.model.service.ApiService;
import com.drxgb.ratracker.model.service.MainService;
import com.drxgb.ratracker.model.service.UserService;
import com.drxgb.ratracker.util.ErrorDialog;
import com.drxgb.util.PropertiesManager;
import com.drxgb.util.Report;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller that shows the login view.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Initializable
 */
public class LoginController implements Initializable
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTS ***
	 * ===========================================================
	 */
	
	/**
	 * The session properties file name.
	 */
	private static String SESSION_FILE = "session.properties";
	
	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	@FXML private Pane root;
	@FXML private TextField txtUserName;
	@FXML private PasswordField pwdApiKey;
	@FXML private CheckBox chkStoreSession;
	@FXML private Button btnPaste;
	@FXML private Button btnOk;
	@FXML private Button btnCancel;
	@FXML private Label lblStatus;
	@FXML private ProgressIndicator pgiLoading;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The API service that is consumed by the server.
	 */
	private ApiService apiService;
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		apiService = RATrackerApplication.getApiService();		
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Called when the used clicks on the paste button.<br>
	 * The pasted field must contain the user API key.
	 */
	@FXML
	public void onBtnPasteAction()
	{
		Clipboard clipboard = Clipboard.getSystemClipboard();
		pwdApiKey.setText(clipboard.getString());
	}
	
	
	/**
	 * When the user clicks on the Cancel button.<br>
	 * This action will also try to close the application.
	 * @param ev The handled event.
	 */
	@FXML
	public void onBtnCancelAction(Event ev)
	{
		ev.consume();
		Stage stage = (Stage) root.getScene().getWindow();
		Optional<ButtonType> option = DialogBuilder.show(
				stage,
				AlertType.CONFIRMATION,
				"Close Confirmation",
				"The application will be closed. Proceed?",
				null,
				ButtonType.YES,
				ButtonType.NO
		);
		if (option != null && option.get() == ButtonType.YES)
		{
			stage.close();
		}
	}
	
	
	/**
	 * When the user clocks on the OK button to attempt a connection to
	 * the API to log in.
	 * @throws InvalidKeyException When the encoding key is invalid.
	 * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is not available in the environment.
	 * @throws NoSuchPaddingException When a particular padding mechanism is not available in the environment.
	 * @throws IllegalBlockSizeException When the length of data provided to a blockcipher is incorrect.
	 * @throws BadPaddingException When a input data is not padded properly.
	 */
	@FXML
	public void onBtnOkAction()
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
				IllegalBlockSizeException, BadPaddingException
	{
		final String userName = txtUserName.getText();
		final String apiKey = pwdApiKey.getText();
		final boolean storeSession = chkStoreSession.selectedProperty().get();
		final String raUrl = RATrackerApplication.RA_URL;
		final String raApiUrl = raUrl + "/API/";
		Thread th = new Thread(() -> tryToAuthenticate(userName, apiKey, storeSession));
		
		pgiLoading.setVisible(true);
		lblStatus.setText("Connecting to " + raApiUrl);
		apiService.initialize(
			new Api(raApiUrl, new Session(userName, apiKey)),
			new UserService()
		);		
		toggleControls(false);
		th.setDaemon(true);
		th.start();
	}
	
	
	/**
	 * Loads the properties containg the session user and API key
	 * if the user has saved the login content.
	 */
	public void loadSessionProperties()
	{
		Properties props = PropertiesManager.load(new File(SESSION_FILE));
		if (props.containsKey("user") && props.containsKey("key"))
		{
			txtUserName.setText(props.getProperty("user"));
			pwdApiKey.setText(props.getProperty("key"));
			chkStoreSession.setSelected(true);
		}
	}
	
	
	/**
	 * When the secne is initialized, focus to the OK button.
	 */
	public void initFocus()
	{
		btnOk.requestFocus();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public Parent getRoot()
	{
		return root;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Stores the user session to a properties file to be loaded when
	 * the user opens the application later.
	 * @param userName The user name to be stored.
	 * @param apiKey The API key to be stored.
	 */
	private void saveSessionProperties(String userName, String apiKey)
	{
		File file = new File(SESSION_FILE);
		Properties props = PropertiesManager.load(file);
		props.setProperty("user", userName);
		props.setProperty("key", apiKey);
		PropertiesManager.save(file, props);
	}
	
	
	/**
	 * Enable/disable the controls when the system attempts to
	 * connect to the API to initialize a seesion.
	 * @param b
	 */
	private void toggleControls(boolean b)
	{
		txtUserName.setDisable(!b);
		pwdApiKey.setDisable(!b);
		chkStoreSession.setDisable(!b);
		btnPaste.setDisable(!b);
		btnOk.setDisable(!b);
		btnCancel.setDisable(!b);
	}
	
	
	/**
	 * Callback to attempt an authentication to the server API.
	 * @param userName The user name.
	 * @param apiKey The api key
	 * @param storeSession Store session when succeeds the authentication.<br>
	 * If this value is <code>false</code> then it won't store the session even
	 * the authentication is succeeded.
	 */
	private void tryToAuthenticate(String userName, String apiKey, boolean storeSession)
	{
		try
		{			
			if (apiService.authenticate())
			{
				MainService mainService = MainService.getInstance();
				if (storeSession)
					saveSessionProperties(userName, apiKey);

				Platform.runLater(() -> {
					try
					{
						Stage stage = (Stage) getRoot().getScene().getWindow();
						Stage mainStage = (Stage) stage.getOwner();
						MainController mainController = ((FXMLLoader) mainStage
															.getScene()
															.getUserData())
															.getController();
						mainService.getStages().put("login", stage);
						mainController.update();
						mainStage.show();
						stage.hide();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						Report.writeErrorLog(e);
						Platform.runLater(() -> {				
								ErrorDialog error = ErrorDialog.getInstance();
								error.updateError(e);
								error.getStage().show();
						});
					}
				});
				
			}
			else
			{
				throw new ApiException("The User name or the API key is incorrect.");
			}			
		}
		catch (ApiException e)
		{
			Platform.runLater(() -> {
				DialogBuilder.show(
						AlertType.ERROR,
						"Authentiaction failed",
						e.getMessage()
				);
			});
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
		finally
		{
			Platform.runLater(() -> {
				toggleControls(true);
				lblStatus.setText("");
				pgiLoading.setVisible(false);
			});
		}
	}
}
