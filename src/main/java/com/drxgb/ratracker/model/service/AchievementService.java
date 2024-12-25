package com.drxgb.ratracker.model.service;

import java.util.List;

import com.drxgb.ratracker.model.entity.game.Achievement;
import com.drxgb.ratracker.model.entity.game.Game;
import com.drxgb.ratracker.model.entity.user.User;
import com.drxgb.util.ValueHandler;

/**
 * Used to display the next achievement to the user.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class AchievementService implements TrackerService
{
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The current user.
	 */
	private User user;
	
	/**
	 * The next achievement list to display.
	 */
	private List<Achievement> nextAchievements;
	
	/**
	 * The achievement list offset.
	 */
	private Integer offset;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */

	public AchievementService(User user)
	{
		try
		{
			this.user = user;
			reset();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}



	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED ***
	 * ===========================================================
	 */

	/**
	 * @see com.drxgb.ratracker.model.service.TrackerService#update()
	 */
	@Override
	public void update()
	{
		Game lastGame = user.getLastGame();
		
		if (lastGame != null)
		{
			nextAchievements = lastGame
					.getAchievements()
					.stream()
					.filter(a -> !a.earned() && !a.earnedHardcore())
					.toList();
			
			offset = ValueHandler.clamp(offset, 0, nextAchievements.size() - 1);
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Checks whether has more achievements to this game.
	 * @return The avaliable achievements flag.
	 */
	public boolean hasMoreAchievements()
	{
		return nextAchievements != null &&  ! nextAchievements.isEmpty();
	}

	
	/**
	 * Get the next locked achievement.
	 * @return The next achievement to be unlocked.
	 */
	public Achievement getNextAchievement()
	{
		return hasMoreAchievements()
				? nextAchievements.get(offset)
				: null;
	}

	
	/**
	 * Reset the list offset to the first achievement.
	 */
	public void reset()
	{
		offset = 0;
		update();
	}
	
	
	/**
	 * Move to the previous achievement.
	 */
	public void previous()
	{
		--offset;
		update();
	}
	
	
	/**
	 * Move to the next achievement.
	 */
	public void next()
	{
		++offset;
		update();
	}
	
	
	/**
	 * Checks whether has a previous achievement.
	 * @return The previous achievement flag.
	 */
	public boolean hasPrevious()
	{
		return offset > 0;
	}
	
	
	/**
	 * Checks whether has a next achievement.
	 * @return The next achievement flag.
	 */
	public boolean hasNext()
	{
		return hasMoreAchievements() && offset < nextAchievements.size() - 1;
	}
}
