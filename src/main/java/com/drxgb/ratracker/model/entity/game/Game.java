package com.drxgb.ratracker.model.entity.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import com.drxgb.ratracker.util.annotation.FieldKey;

/**
 * The game that have achievements or not.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class Game implements Comparable<Game>
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The unique ID.
	 */
	private Long id;
	
	/**
	 * The game title.
	 */
	@FieldKey
	@FieldKey(name = "game")
	private String title;
	
	/**
	 * Company name that has published the game.
	 */
	@FieldKey private String publisher;
	
	/**
	 * Company name that has developed the game.
	 */
	@FieldKey private String developer;
	
	/**
	 * The game genre.
	 */
	@FieldKey private String genre;
	
	/**
	 * When the game was released.<br>
	 * This value is a <code>String</code> instead fo <code>Date</code>
	 * because the RetroAchievements API this fields is a string. So the
	 * user that registered this game to RA may use any date format and
	 * this application cannot preview the date format used by the user.
	 */
	@FieldKey private String released;
	
	/**
	 * Total of players that played this game.
	 */
	private Integer totalPlayers;
	
	
	/**
	 * Total of players that played this game
	 * on Hardcore mode.
	 */
	private Integer totalPlayersHardcore;

	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * Console that this game was developed.
	 */
	private Console console;
	
	/**
	 * A set of icons to this game.
	 */
	private GameImage image;
	
	/**
	 * List of achievements.
	 */
	private List<Achievement> achievements = new ArrayList<>();
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a fresh game.
	 */
	public Game() {}
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	public int compareTo(Game o)
	{
		return (int) (this.id - o.getId());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */

	/**
	 * Get the amount of points earned in this game.
	 * @return The sum of the achievements points earned.
	 */
	public int points()
	{
		return countTotals(achievements, a -> (a.earned() ? a.getPoints() : 0));
	}
	
	
	
	/**
	 * Get the amount of points earned in this game on Hardocre mode.
	 * @return The sum of the achievements points earned.
	 */
	public int hardcorePoints()
	{
		return countTotals(achievements, a -> (a.earnedHardcore() ? a.getPoints() : 0));
	}
	
	
	/**
	 * Get the true ratio in this game.
	 * @return The sum of true ratio of each achievement.
	 */
	public int truePoints()
	{
		return countTotals(achievements, a -> (a.earned() ? a.getTrueRatio() : 0));
	}
	
	
	/**
	 * Get total points of each achievement.
	 * @return The sum of the total points of each achievement.
	 */
	public int totalPoints()
	{
		return countTotals(achievements, a -> a.getPoints());
	}
	
	
	/**
	 * Get total true ratio of each achievement.
	 * @return The sum of the total true ratio of each achievement.
	 */
	public int totalTrueRatio()
	{
		return countTotals(achievements, a -> a.getTrueRatio());
	}
	
	
	/**
	 * Get the percentage of the earned achievements including the hardcore
	 * achievements.
	 * @return A percentage of earned achievements.
	 */
	public double progress()
	{
		return (((double) getEarnedAchievements().size()) + ((double) getEarnedHardcoreAchievements().size())) /
				((double) getAchievements().size());
	}
	
	
	/**
	 * Get a list of earned achievements in this game.
	 * @return A list of earned achievements in this game.
	 */
	public List<Achievement> getEarnedAchievements()
	{
		return sublistAchievements(achievements, a -> a.earned());
	}
	
	
	/**
	 * Get a list of earned achievements in this game on Hardcore mode.
	 * @return A list of earned achievements in this game.
	 */
	public List<Achievement> getEarnedHardcoreAchievements()
	{
		return sublistAchievements(achievements, a-> a.earnedHardcore());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PRIVATE METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Utility to count some achievements.
	 * @param achievements A list of achievements.
	 * @param fnCount Callback to count some achievement data.
	 * @return A sum of an achievement data.
	 */
	private static int countTotals(List<Achievement> achievements, ToIntFunction<Achievement> fnCount)
	{
		return achievements.stream()
				.mapToInt(fnCount)
				.sum();
	}
	
	
	/**
	 * Creates a sublist of achievements according to a specified condition.
	 * @param achievements A list of achievements.
	 * @param fnFilter Callback that returns a condition to filter the sublist.
	 * @return A filtered sublist of achievements.
	 */
	private static List<Achievement> sublistAchievements(List<Achievement> achievements, Predicate<Achievement> fnFilter)
	{
		return achievements.stream()
				.filter(fnFilter)
				.collect(Collectors.toList());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	public Long getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public String getDeveloper()
	{
		return developer;
	}

	public String getGenre()
	{
		return genre;
	}

	public String getReleased()
	{
		return released;
	}	

	public Integer getTotalPlayers()
	{
		return totalPlayers;
	}

	public Integer getTotalPlayersHardcore()
	{
		return totalPlayersHardcore;
	}

	public Console getConsole()
	{
		return console;
	}

	public GameImage getImage()
	{
		return image;
	}

	public List<Achievement> getAchievements()
	{
		return achievements;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** SETTERS ***
	 * ===========================================================
	 */

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public void setDeveloper(String developer)
	{
		this.developer = developer;
	}

	public void setGenre(String genre)
	{
		this.genre = genre;
	}

	public void setReleased(String released)
	{
		this.released = released;
	}	

	public void setTotalPlayers(Integer totalPlayers)
	{
		this.totalPlayers = totalPlayers;
	}

	public void setTotalPlayersHardcore(Integer totalPlayersHardcore)
	{
		this.totalPlayersHardcore = totalPlayersHardcore;
	}


	public void setConsole(Console console)
	{
		this.console = console;
	}

	public void setImage(GameImage image)
	{
		this.image = image;
	}

	public void setAchievements(List<Achievement> achievements)
	{
		this.achievements = achievements;
		this.achievements.forEach(a -> a.setGame(this));
	}
	
	
	/*
	 * ===========================================================
	 * 			*** HASHCODE E EQUALS ***
	 * ===========================================================
	 */

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** TO STRING ***
	 * ===========================================================
	 */

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Game [id=").append(id).append(", title=").append(title).append(", publisher=").append(publisher)
				.append(", developer=").append(developer).append(", genre=").append(genre).append(", released=")
				.append(released).append(", console=").append(console).append(", image=").append(image)
				.append(", achievements=").append(achievements).append("]");
		return builder.toString();
	}
}
