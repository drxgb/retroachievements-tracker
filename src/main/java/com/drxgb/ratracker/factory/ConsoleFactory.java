package com.drxgb.ratracker.factory;

import javax.json.JsonObject;

import com.drxgb.ratracker.model.entity.game.Console;

/**
 * Utiliy to instanciate a <code>Console</code> object.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Console
 */
public abstract class ConsoleFactory
{
	/**
	 * Creates a instance of <code>Console</code> read from
	 * a JSON object.
	 * @param gameInfo A JSON Object containing the game info.
	 * @return An instance of <code>Console</code>.
	 */
	public static Console create(JsonObject gameInfo)
	{
		return new Console(
				(long) gameInfo.getInt("ConsoleID"),
				 gameInfo.getString("ConsoleName")
		);
	}
}
