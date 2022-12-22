package com.drxgb.ratracker.factory;

import javax.json.JsonObject;

import com.drxgb.ratracker.model.entity.game.CompletionProgress;
import com.drxgb.ratracker.model.entity.game.Console;
import com.drxgb.ratracker.model.entity.game.Game;
import com.drxgb.ratracker.model.entity.game.GameImage;

/**
 * Utility to instanciate user's progress.
 * @author Dr.XGB
 * @version 1.0.0
 * @see CompletionProgress
 */
public abstract class CompletionProgressFactory
{
	/**
	 * Create an instance of <code>CompletionProgress</code> read from a
	 * JSON object.
	 * @param gameInfo A JSON Object containing the game info.
	 * @param progress A JSON Object containing the game progress.
	 * @return An instance of <code>CompletionProgress</code>.
	 */
	public static CompletionProgress create(JsonObject gameInfo, JsonObject progress)
	{
		final String id = gameInfo.getString("GameID");
		
		Game game = new Game();
		game.setId(Long.parseLong(id));
		game.setTitle(gameInfo.getString("Title"));
		game.setImage(new GameImage(gameInfo.getString("ImageIcon")));
		game.setConsole(
				new Console(
					Long.parseLong(gameInfo.getString("ConsoleID")),
					gameInfo.getString("ConsoleName")
				)
		);
		
		return new CompletionProgress(
				gameInfo.getString("HardcoreMode").equals("1"),
				Integer.parseInt(progress.getJsonObject(id).getString("ScoreAchieved")),
				Integer.parseInt(gameInfo.getString("NumAwarded")),
				Integer.parseInt(gameInfo.getString("MaxPossible")),
				game
		);
	}
}
