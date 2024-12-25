package com.drxgb.ratracker.controller.view;

/**
 * An interface that implements a custom view controller.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ViewController
 */
public interface ViewInterface
{
	/**
	 * Called when the controller is being to be created.
	 */
	void onCreate();
	
	/**
	 * Called when the controller is being initialized.
	 */
	void onInit();
	
	/**
	 * Called when the controller is being refreshed.
	 */
	void onUpdate();
	
	/**
	 * Check if the view can be updated.
	 * @return The update flag.
	 */
	boolean canUpdate();
}
