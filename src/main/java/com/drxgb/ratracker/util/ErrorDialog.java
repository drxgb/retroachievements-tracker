package com.drxgb.ratracker.util;

import java.util.Arrays;

import com.drxgb.ratracker.RATrackerApplication;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Catches an exception and shows a dialog to the user.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class ErrorDialog
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The singleton instance
	 */
	private static ErrorDialog instance;
	
	/**
	 * The window to place the content.
	 */
	private Stage stage;
	
	/**
	 * The root panel.
	 */
	private Pane root;
	
	/**
	 * The error title.
	 */
	private Label lblTitle = new Label();
	
	/**
	 * The stack trace content.
	 */
	private TextArea txtStackTrace = new TextArea();
	
	/**
	 * Feedback message when the user has copied the stack trace.
	 */
	private Label lblCopiedMessage = new Label("Copied to the clipboard.");
	
	/**
	 * Describes how to contact the author to send an issue.
	 */
	private Label lblReportMessage = new Label(
			"If you see an error like this, please contact to the author on Github, "
			+ "submit an issue and help us to improve this software."
	);
	
	/**
	 * The link to submit an issue found to the author.
	 */
	private Button btnSubmitIssue = new Button("Report issue");
	
	/**
	 * Close the window when it was clicked.
	 */
	private Button btnClose = new Button("Close");
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	private Throwable error; 
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a singleton instance.
	 */
	private ErrorDialog()
	{		
		HBox hb = new HBox(12.0, btnSubmitIssue, btnClose);
		
		root = new VBox(
				8.0,
				lblTitle,
				txtStackTrace,
				lblCopiedMessage,
				lblReportMessage,
				hb
		);
		root.setPadding(new Insets(12, 24, 12, 24));
		root.setPrefWidth(640);
		root.setMaxWidth(Pane.USE_PREF_SIZE);
		
		lblTitle.setStyle("-fx-font-size: 18;");
		lblTitle.setWrapText(true);
		txtStackTrace.setEditable(false);
		txtStackTrace.setStyle("-fx-font-family: Consolas");
		txtStackTrace.setOnMouseClicked(ev -> {			
			if (lblCopiedMessage.isVisible())
				return;
			
			StringBuilder sb = new StringBuilder();
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			Thread th = new Thread(() -> {
				try
				{
					Thread.sleep(1000);
					Platform.runLater(() -> lblCopiedMessage.setVisible(false));
				}
				catch (InterruptedException e) {}
			});
			
			txtStackTrace.selectAll();
			sb.append("Caused by: ")
					.append(error.getClass().getName())
					.append("\n\t")
					.append(error.getMessage())
					.append("\n");
			Arrays.asList(
					txtStackTrace.getText().split("\n")
				).forEach(l -> sb.append("\t\t").append(l).append('\n'));
			content.putString(sb.toString());
			clipboard.setContent(content);
			
			lblCopiedMessage.setVisible(true);
			th.setDaemon(true);
			th.start();
		});
		lblCopiedMessage.setVisible(false);
		lblCopiedMessage.setStyle("-fx-font-weight: 700; -fx-text-fill: orange;");
		lblReportMessage.setWrapText(true);
		
		hb.setAlignment(Pos.CENTER_RIGHT);
		btnSubmitIssue.setPrefWidth(160);
		btnSubmitIssue.setOnAction(ev -> {
			RATrackerApplication
				.getInstance()
				.getHostServices()
				.showDocument(
					"https://github.com/drxgb/retroachievements-tracker/issues/new"
				);
		});
		btnClose.setOnAction(ev -> stage.close());
		btnClose.setPrefWidth(80);
		
		stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.getIcons().add(new Image("/resources/error.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		Styles.getInstance().apply(stage.getScene());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** STATIC PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Get the singleton instance.
	 * @return The singleton instance.
	 */
	public static ErrorDialog getInstance()
	{
		if (instance == null)
			instance = new ErrorDialog();
		return instance;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Updates the error dialog.
	 * @param t The thrown error.
	 */
	public void updateError(Throwable t)
	{
		error = t;
		stage.setTitle("ERROR: " + error.getClass().getName());
		lblTitle.setText(error.getMessage());
		txtStackTrace.clear();
		Arrays.asList(error.getStackTrace())
			.forEach(st -> {
				StringBuilder sb = new StringBuilder();
				
				sb.append(st.toString())
					.append('\n');
				txtStackTrace.appendText(sb.toString());
		});
	}


	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public Stage getStage()
	{
		return stage;
	}	
}
