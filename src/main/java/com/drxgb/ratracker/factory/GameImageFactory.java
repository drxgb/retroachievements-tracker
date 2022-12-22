package com.drxgb.ratracker.factory;

import javax.json.JsonObject;

import com.drxgb.ratracker.model.entity.game.GameImage;

/**
 * Utility to instanciate a <code>GameImage</code> object.
 * @author Dr.XGB
 * @version 1.0.0
 * @see GameImage
 */
public abstract class GameImageFactory
{
	/**
	 * Creates an instance of <code>GameImage</code> object
	 * read from a JSON object. 
	 * @param gameInfo A JSON Object containing the game info.
	 * @return An instance of <code>GameImage</code>.
	 */
	public static GameImage create(JsonObject gameInfo)
	{
		return new GameImage(
				gameInfo.getString("ImageIcon"),
				gameInfo.getString("ImageTitle"),
				gameInfo.getString("ImageIngame"),
				gameInfo.getString("ImageBoxArt")
		);
	}
}
