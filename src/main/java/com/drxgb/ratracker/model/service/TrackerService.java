package com.drxgb.ratracker.model.service;

/**
 * Interface that has functions that implements a tracker service.
 * @author Mikinho
 *
 */
public interface TrackerService
{
	/**
	 * Refreshes the tracker.
	 * @throws Throwable When occours an error during the refresh.
	 */
	void update() throws Throwable;
}
