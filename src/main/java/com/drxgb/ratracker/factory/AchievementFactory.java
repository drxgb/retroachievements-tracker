package com.drxgb.ratracker.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.model.entity.game.Achievement;
import com.drxgb.ratracker.model.entity.game.Game;

/**
 * Utiliy to ease achievements instanciations.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Achievement
 */
public abstract class AchievementFactory
{	
	/**
	 * Creates a list of achievements.
	 * @param achievements A JSON Object containing all the achievements.
	 * @return A list of achievements.
	 * @throws ParseException When the date format is invalid. 
	 * @throws NullPointerException When the value found is <code>null</code>.
	 */
	public static List<Achievement> createList(JsonObject achievements)
		throws ParseException, NullPointerException
	{
		List<Achievement> list = new ArrayList<>();
		SimpleDateFormat sdf = RATrackerApplication.getDateFormat();
		
		for (JsonValue el : achievements.values())
		{
			JsonObject obj = (JsonObject) el;
			Objects.requireNonNull(obj);
			Achievement achievement = new Achievement();
			
			try
			{
				achievement.setId(Long.valueOf(obj.getInt("ID")));
				achievement.setBadgeId(Long.parseLong(obj.getString("BadgeName")));
				achievement.setTitle(obj.getString("Title"));
				achievement.setDescription(obj.getString("Description"));
				achievement.setPoints(obj.getInt("Points"));
				achievement.setTrueRatio(obj.getInt("TrueRatio"));
				achievement.setUnlocks(obj.getInt("NumAwarded"));
				achievement.setHardcoreUnlocks(obj.getInt("NumAwardedHardcore"));
				achievement.setAuthor(obj.getString("Author"));
				achievement.setDisplayOrder(obj.getInt("DisplayOrder"));
				achievement.setCreatedAt(sdf.parse(obj.getString("DateCreated")));
				achievement.setModifiedAt(sdf.parse(obj.getString("DateModified")));
				achievement.setEarnedAt(sdf.parse(obj.getString("DateEarned")));
				achievement.setEarnedHardcoreAt(sdf.parse(obj.getString("DateEarnedHardcore")));
			}
			catch (NullPointerException e) 
			{}
			
			list.add(achievement);
		}
		
		return list;
	}
	
	
	/**
	 * Creates a list of recent achievements by the current user.
	 * @param achievements A JSON conatining the recent achievements.
	 * @return A list of recent achievements.
	 * @throws ParseException When the date format is invalid. 
	 * @throws NullPointerException When the value found is <code>null</code>.
	 */
	public static List<Achievement> createRecentAchievements(JsonObject achievements)
			throws ParseException, NullPointerException
	{
		List<Achievement> list = new ArrayList<>();
		SimpleDateFormat sdf = RATrackerApplication.getDateFormat();
		
		for (JsonValue ids : achievements.values())
		{
			JsonObject ao = (JsonObject) ids;
			for (JsonValue a : ao.values())
			{
				JsonObject obj = (JsonObject) a;
				Objects.requireNonNull(obj);
				Achievement achievement = new Achievement();
				Game game = new Game();
				
				game.setId((long) obj.getInt("GameID"));
				game.setTitle(obj.getString("GameTitle"));
				achievement.setId((long) obj.getInt("ID"));
				achievement.setBadgeId(Long.parseLong(obj.getString("BadgeName")));
				achievement.setTitle(obj.getString("Title"));
				achievement.setDescription(obj.getString("Description"));
				achievement.setPoints(obj.getInt("Points"));
				achievement.setEarnedAt(sdf.parse(obj.getString("DateAwarded")));
				achievement.setGame(game);
				
				list.add(achievement);
			}
		}
		
		return list.stream()
				.sorted(new Comparator<Achievement>()
					{
						@Override
						public int compare(Achievement a, Achievement b)
						{
							return b.getEarnedAt().compareTo(a.getEarnedAt());
						}						
					})
				.collect(Collectors.toList());
	}
}
