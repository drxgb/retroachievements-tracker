package com.drxgb.ratracker;

import java.util.Locale;

/**
 * The entrypoint application container.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class Main
{
	/**
	 * Starts the application.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args)
	{
		Locale.setDefault(Locale.US);
		RATrackerApplication.start(args);
	}

}
