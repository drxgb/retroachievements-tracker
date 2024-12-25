package com.drxgb.ratracker.controller.view;

import java.util.List;
import java.util.ListIterator;

import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Abstract controller to retrieves a bunch of elements from a model.
 * @author Dr.XGB
 * @version 1.0.0
 * @param <T> Type of data that represents an element.
 * @see ViewController
 */
public abstract class ListViewController<T> extends ViewController
{	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */

	/**
	 * Elements to be listed on a view.
	 */
	protected List<T> elements;
	
	/**
	 * An entitiy to control an iteration through the elements.
	 */
	protected ListIterator<T> iterator;
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */	

	@Override
	public void onInit()
	{
		freshList();
	}
	

	@Override
	public void onUpdate()
	{
		freshList();
		
		while (iterator.hasNext())
		{
			T element = iterator.next();
			VBox vbIcon = new VBox();
			GridPane vbFields = new GridPane();
			Separator separator = new Separator();
			ColumnConstraints constraints = new ColumnConstraints();
			
			constraints.setFillWidth(true);					
			vbIcon.setAlignment(Pos.CENTER);
			vbFields.setAlignment(Pos.CENTER_LEFT);
			vbFields.getColumnConstraints().add(constraints);
			
			updateIconPane(vbIcon, getIcon(element));
			updateFieldsPane(vbFields);
			mountView(paneMain, vbIcon, vbFields);
			GridPane.setHgrow(vbFields, Priority.ALWAYS);
			paneMain.addRow(paneMain.getRowCount(), separator);
			
			GridPane.setColumnSpan(separator, 2);
			separator.setMaxWidth(Double.MAX_VALUE);
			separator.setStyle(
					"-fx-background-color:" + getLayout().getString("foreground") + ";"
			);
		}
		
		updateBackground();
	}
	
	
	@Override
	protected final ImageView getIcon()
	{
		return getIcon(null);
	}
	
	
	@Override
	protected final String onFieldWrite(String key)
	{
		return onFieldWrite(key, elements.get(iterator.nextIndex() - 1));
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a fresh list of elements.
	 */
	private void freshList()
	{
		elements = populateList();
		iterator = elements.listIterator();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** ABSTRACT PROTECTED METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Retrieves an element icon.
	 * @param obj The required element.
	 * @return An icon represented by an element.
	 */
	protected abstract ImageView getIcon(T obj);
	
	/**
	 * Fill a list of elements.
	 * @return A list of elements.
	 */
	protected abstract List<T> populateList();
	
	/**
	 * Called when a field is being written on a view.
	 * @param key The field key.
	 * @param obj The required element.
	 * @return The value to be written on a view.
	 */
	protected abstract String onFieldWrite(String key, T obj);
	
}
