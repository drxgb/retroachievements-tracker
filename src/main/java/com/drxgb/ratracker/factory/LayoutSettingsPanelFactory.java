package com.drxgb.ratracker.factory;

import javax.json.JsonObject;

import com.drxgb.javafxutils.ColorParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Loads a layout settings panel.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class LayoutSettingsPanelFactory implements SettingsPanelFactory
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTS ***
	 * ===========================================================
	 */

	/**
	 * Sample text used to test a layout before saving.
	 */
	private static final String SAMPLE_TEXT = "This is a sample text!";
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	/**
	 * @see com.drxgb.ratracker.factory.SettingsPanelFactory#makePanel(javax.json.JsonObject)
	 */
	@Override
	public Parent makePanel(JsonObject settings)
	{
		VBox vbContent = new VBox(8.0);
		TitledPane titledPane = new TitledPane("Layout", vbContent);		
		HBox hbFont = new HBox(8.0);
		HBox hbPreview = new HBox();
		ComboBox<String> cbFont = new ComboBox<>();
		Spinner<Double> spnSize = new Spinner<>();
		ColorPicker cpBackground = new ColorPicker();
		ColorPicker cpForeground = new ColorPicker();
		TextField txtTest = new TextField();
		Label lblPreview = new Label();
		ObservableList<Node> obsContentPane = vbContent.getChildren();
		ObservableList<Node> obsFontPane = hbFont.getChildren();
		final double size = settings.getJsonNumber("size").doubleValue();
		
		obsContentPane.add(hbFont);
		obsContentPane.add(new Label("Test:"));
		obsContentPane.add(txtTest);
		obsContentPane.add(hbPreview);
		obsFontPane.add(new Label("Font:"));
		obsFontPane.add(cbFont);
		obsFontPane.add(new Label("Size:"));
		obsFontPane.add(spnSize);
		obsFontPane.add(new Label("Background:"));
		obsFontPane.add(cpBackground);
		obsFontPane.add(new Label("Foreground:"));
		obsFontPane.add(cpForeground);
		titledPane.setCollapsible(false);
		hbFont.setAlignment(Pos.CENTER_LEFT);
		hbPreview.getChildren().add(lblPreview);
		hbPreview.setPadding(new Insets(12.0));
		hbPreview.setAlignment(Pos.CENTER);
		
		cbFont.getSelectionModel().selectedItemProperty().addListener((obs, oldvalue, newValue) -> {
			final double fontSize = lblPreview.getFont().getSize();
			lblPreview.setFont(new Font(newValue, fontSize));
		});
		spnSize.valueProperty().addListener((obs, oldValue, newValue) -> {
			final String name = lblPreview.getFont().getName();
			lblPreview.setFont(new Font(name, newValue));
		});
		txtTest.textProperty().addListener((obs, oldValue, newValue) -> {
			lblPreview.setText(newValue);
		});
		cpBackground.valueProperty().addListener((obs, oldValue, newValue) -> {
			hbPreview.setStyle("-fx-background-color:" + ColorParser.toCssHexColor(newValue));
		});
		cpForeground.valueProperty().addListener((obs, oldValue, newValue) -> {
			lblPreview.setTextFill(newValue);
		});
		
		cbFont.setItems(FXCollections.observableList(Font.getFamilies()));
		cbFont.getSelectionModel().select(settings.getString("font"));
		spnSize.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 100, size, 1));
		spnSize.setPrefWidth(64);
		cpBackground.setValue(null);
		cpForeground.setValue(null);
		cpBackground.setValue(Color.web(settings.getString("background")));
		cpForeground.setValue(Color.web(settings.getString("foreground")));
		txtTest.setText(SAMPLE_TEXT);
		
		return titledPane;
	}

}
