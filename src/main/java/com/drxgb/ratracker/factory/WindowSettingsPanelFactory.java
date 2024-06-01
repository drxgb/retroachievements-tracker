package com.drxgb.ratracker.factory;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import javax.json.JsonObject;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Loads a window settings panel.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class WindowSettingsPanelFactory implements SettingsPanelFactory
{
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
		TitledPane titledPane = new TitledPane("Window", vbContent);
		
		HBox hbAutoSize = new HBox(12.0);
		GridPane grdCustoms = new GridPane();
		
		Label lblAutoSize = new Label("Auto size");
		Label lblWidth = new Label("Width:");
		Label lblHeight = new Label("Height:");
		Label lblAlign = new Label("Alignment:");
		
		CheckBox chkAutoSize = new CheckBox();
		Spinner<Integer> spnWidth = new Spinner<>();
		Spinner<Integer> spnHeight = new Spinner<>();
		ComboBox<Pos> cbAlign = new ComboBox<>();
		
		ColumnConstraints columnConstraints = new ColumnConstraints();
		
		ObservableList<Node> obsContents = vbContent.getChildren();
		ObservableList<Node> obsAutoSize = hbAutoSize.getChildren();
		ObservableList<Pos> obsAlignItems = cbAlign.getItems();
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		final int maxWidth = gd.getDisplayMode().getWidth();
		final int maxHeight = gd.getDisplayMode().getHeight();
		
		final boolean autoSize = settings.getBoolean("autosize");
		final int width = settings.getInt("width");
		final int height = settings.getInt("height");
		final Pos hAlign = Pos.valueOf(settings.getString("align"));

		titledPane.setContent(vbContent);
		titledPane.setCollapsible(false);
		obsContents.add(hbAutoSize);
		obsContents.add(grdCustoms);
		obsAutoSize.add(chkAutoSize);
		obsAutoSize.add(lblAutoSize);
		
		columnConstraints.setPercentWidth(25.0);
		grdCustoms.add(lblWidth, 0, 0);
		grdCustoms.add(spnWidth, 1, 0);
		grdCustoms.add(lblHeight, 2, 0);
		grdCustoms.add(spnHeight, 3, 0);
		grdCustoms.add(lblAlign, 0, 1);
		grdCustoms.add(cbAlign, 1, 1);
		grdCustoms.getColumnConstraints().addAll(
				columnConstraints,
				columnConstraints,
				columnConstraints,
				columnConstraints
		);
		grdCustoms.setHgap(4.0);
		grdCustoms.setVgap(8.0);		
		
		obsAlignItems.addAll(Arrays.asList(Pos.values()));
		
		chkAutoSize.selectedProperty().addListener((obs, oldValue, newValue) -> grdCustoms.setDisable(newValue));
		lblAutoSize.setOnMouseClicked(ev -> chkAutoSize.fire());
		spnWidth.setEditable(true);
		spnHeight.setEditable(true);
		
		chkAutoSize.setSelected(autoSize);
		spnWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(400, maxWidth, width));
		spnHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, maxHeight, height));
		cbAlign.setValue(hAlign);
		
		return titledPane;
	}
}
