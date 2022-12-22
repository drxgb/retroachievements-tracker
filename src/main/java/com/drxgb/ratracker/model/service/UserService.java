package com.drxgb.ratracker.model.service;

import java.text.ParseException;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.drxgb.ratracker.factory.AchievementFactory;
import com.drxgb.ratracker.factory.CompletionProgressFactory;
import com.drxgb.ratracker.factory.ConsoleFactory;
import com.drxgb.ratracker.factory.GameImageFactory;
import com.drxgb.ratracker.model.entity.game.Achievement;
import com.drxgb.ratracker.model.entity.game.CompletionProgress;
import com.drxgb.ratracker.model.entity.game.Game;
import com.drxgb.ratracker.model.entity.user.User;
import com.drxgb.ratracker.util.annotation.FieldKey;

/**
 * The user service layer. It is used for user actions
 * and retrieve its properties.
 * @author Dr.XGB
 * @version 1.0.0
 * @see User
 */
public class UserService
{
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The current user.
	 */
	private User user = new User();
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a fresh service.
	 */
	public UserService() {}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Updates the user data after an API request.
	 * @param userSummary The user data to be updated.
	 * @throws ParseException When the date format is invalid.
	 * @throws NullPointerException When the value found is null.
	 */
	public void updateUser(JsonObject userSummary) throws NullPointerException, ParseException
	{
		JsonObject lastActivity = userSummary.getJsonObject("LastActivity");
		user.setId(Long.valueOf(lastActivity.getString("ID")));
		user.setName(lastActivity.getString("User"));
		user.setPicture(userSummary.getString("UserPic"));
		user.setMotto(userSummary.getString("Motto"));
		user.setRank(userSummary.getInt("Rank"));
		user.setPoints(Integer.valueOf(userSummary.getString("TotalPoints")));
		user.setTruePoints(Integer.valueOf(userSummary.getString("TotalTruePoints")));
		user.setPresenceMessage(userSummary.getString("RichPresenceMsg"));
		user.setStatus(userSummary.getString("Status"));
		user.setRecentAchievements(
				AchievementFactory.createRecentAchievements(
						userSummary.getJsonObject("RecentAchievements")
				)
		);
	}
	
	
	/**
	 * Sets the last game plaed by the current user.
	 * @param gameInfo The JSON object containing the last game info.
	 * @throws ParseException When the date format is invalid. 
	 * @throws NullPointerException When the value found is null. 
	 */
	public void updateLastGame(JsonObject gameInfo) throws NullPointerException, ParseException
	{
		Game game = new Game();
		game.setId((long) gameInfo.getInt("ID"));
		game.setTitle(gameInfo.getString("Title"));
		game.setPublisher(gameInfo.getString("Publisher"));
		game.setDeveloper(gameInfo.getString("Developer"));
		game.setGenre(gameInfo.getString("Genre"));
		game.setReleased(gameInfo.getString("Released"));
		game.setTotalPlayers(Integer.parseInt(gameInfo.getString("NumDistinctPlayersCasual")));
		game.setTotalPlayersHardcore(Integer.parseInt(gameInfo.getString("NumDistinctPlayersHardcore")));
		game.setConsole(ConsoleFactory.create(gameInfo));
		game.setImage(GameImageFactory.create(gameInfo));
		game.setAchievements(
				AchievementFactory.createList(gameInfo.getJsonObject("Achievements"))
		);
		user.setLastGame(game);
	}
	
	
	/**
	 * Refreshes the completed games list by the current user.
	 * @param games A JSON array containing the completed games.
	 * @param progress A JSON object containing the user progress.
	 */
	public void updateCompletedGames(JsonArray games, JsonObject progress)
	{
		user.getGames().clear();
		for (JsonObject game : games.getValuesAs(JsonObject.class))
		{
			user.getGames().add(CompletionProgressFactory.create(game, progress));
		}
	}
	
	
	/**
	 * Get the count of the user awarded achievements.
	 * @return Number of the user awarded achievements.
	 */
	@FieldKey(name = "achievements")
	public int countAwardedAchievements()
	{
		return countGameParameter(
				p -> !p.isHardcoreMode(),
				p -> p.getAwarded()
		);
	}
	
	
	/**
	 * Get the count of the user awarded achievements on Hardcore mode.
	 * @return Number of the user awarded achievements.
	 */
	@FieldKey(name = "hardcoreAchievements")
	public int countHardcoreAwardedAchievements()
	{
		return countGameParameter(
				p -> p.isHardcoreMode(),
				p -> p.getAwarded()
				);
	}
	
	
	/**
	 * Get the count of the user score.
	 * @return Number of the user score.
	 */
	@FieldKey(name = "score")
	public int getScore()
	{
		return countGameParameter(
				p -> !p.isHardcoreMode(),
				p -> p.getScore()
		);
	}
	
	
	/**
	 * Get the count of the user score on Hardcore mode.
	 * @return Number of the user score.
	 */
	@FieldKey(name = "hardcoreScore")
	public int getHardcoreScore()
	{
		return countGameParameter(
				p -> p.isHardcoreMode(),
				p -> p.getScore()
		);
	}
	
	
	/**
	 * Retrieves a list of completed games by the current user.
	 * @return A list of completed games.
	 */
	@FieldKey(name = "awards", count = true)
	public List<Game> getCompletedGames()
	{
		return user.getGames()
				.stream()
				.sorted()
				.distinct()
				.filter(p -> p.wonPercent() == 1.0)
				.map(g -> g.getGame())
				.collect(Collectors.toList());
	}
	
	
	/**
	 * Get the next locked achievement.
	 * @return The next achievement to be unlocked.
	 */
	public Achievement getNextAchievement()
	{
		return user
				.getLastGame()
				.getAchievements()
				.stream()
				.filter(a -> !a.earned() && !a.earnedHardcore())
				.findFirst()
				.get();
	}
	
	
	/**
	 * Write the "Won By" message like the RA web page.
	 * <p>Structure:</p>
	 * <ul>
	 * 	<li><code>u</code> = Players that unlocked the achievement</li>
	 * 	<li><code>h</code> = Players that unlocked the achivement on Hardcore mode</li>
	 * 	<li><code>t</code> = Total players</li>
	 * </ul>
	 * So the message will be: <code>u (h) of t</code><br>
	 * Considering <code>u = 10</code>, <code>h = 5</code> and <code>t = 30</code>,
	 * the message will be <code>10 (5) of 30</code>
	 * @param achievement The target achievement.
	 * @return The "Won By" message.
	 */
	public String writeWonByAchievement(Achievement achievement)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(achievement.getUnlocks())
			.append(" (")
			.append(achievement.getHardcoreUnlocks())
			.append(")")
			.append(" of ")
			.append(achievement.getGame().getTotalPlayers());
		
		return sb.toString();
	}
	
	
	/**
	 * Write "Won By" message using the next achievement.
	 * @return The "Won By" message.
	 */
	@FieldKey(name = "wonBy")
	public String writeByWonAchievement()
	{
		return writeWonByAchievement(getNextAchievement());
	}
	
	
	/**
	 * Write the percentage of the players that unlocked the
	 * achievement.
	 * @param achievement The target achievement.
	 * @return The percentage message.
	 */
	public String writeWonPercent(Achievement achievement)
	{
		return String.format("%.2f", achievement.unlocksByPlayers() * 100.0) + '%';
	}
	
	/**
	 * Write the percentage of the players that unlocked the
	 * next achievement.
	 * @return The percentage message.
	 */
	@FieldKey(name = "wonPercent")
	public String writeWonPercent()
	{
		return writeWonPercent(getNextAchievement());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	public User getUser()
	{
		return user;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Utility to count game parameters.
	 * @param fnFilter Callback to filter games.
	 * @param fnCount Calback to count game parameters
	 * @return A sum of game parameters.
	 */
	private int countGameParameter(
			Predicate<CompletionProgress> fnFilter,
			ToIntFunction<CompletionProgress> fnCount
		)
	{
		return user.getGames()
				.stream()
				.filter(fnFilter)
				.mapToInt(fnCount)
				.sum();
	}
}
