package com.drxgb.ratracker.model.entity.game;

import com.drxgb.ratracker.util.annotation.FieldKey;

/**
 * An associative class that links the user to a collection of
 * games containing a progress about each game.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class CompletionProgress implements Comparable<CompletionProgress>
{	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The game is being played on Hardcore mode.
	 */
	@FieldKey(name = "hardcore")
	private Boolean hardcoreMode;
	
	/**
	 * Amount of points earned in the game.
	 */
	private Integer score;
	
	/**
	 * Amount of achievement awared in the game.
	 */
	private Integer awarded;
	
	/**
	 * Total of achievements in the game.
	 */
	private Integer total;
	
	/*
	 * ===========================================================
	 * 			*** ASSOSCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The game that will be linked to the user.
	 */
	private Game game;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */

	/**
	 * Creates a game progress to the user.
	 * @param hardcoreMode The game is being played on Hardcore mode.
	 * @param score Amount of points earned in the game.
	 * @param awarded Amount of achievement awared in the game.
	 * @param total Total of achievements in the game.
	 * @param game The game that will be linked to the user.
	 */
	public CompletionProgress(
			Boolean hardcoreMode,
			Integer score,
			Integer awarded,
			Integer total,
			Game game
		)
	{
		this.hardcoreMode = hardcoreMode;
		this.score = score;
		this.awarded = awarded;
		this.total = total;
		this.game = game;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */	
	
	@Override
	public int compareTo(CompletionProgress o)
	{
		final int cmp = game.compareTo(o.getGame());
		if (cmp == 0)
		{
			final int a = hardcoreMode ? 1 : 0;
			final int b = o.isHardcoreMode() ? 1 : 0;
			return b - a;
		}
		return cmp;
	}
	

	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */	

	/**
	 * Computes the percentage of awarded achievements.
	 * @return The percentage of awarded achievements.
	 */
	public double wonPercent()
	{
		return ((double) awarded / (double) total);
	}
	
	
	/**
	 * Writes a message containing a relation of awarded
	 * achievements to its total.<br>
	 * <p>Example:</p>
	 * <ul>
	 * 	<li><code>a</code> = amount of awarded achievements.</li>
	 * 	<li><code>t</code> = total of achievements.</li>
	 * </ul>
	 * The message will be <code>a/t</code>.<br>
	 * In other words, if the user has awarded 10 achievements and
	 * the game has 30 achievements, so the message will be <code>10/30</code>.
	 * @return A completion message.
	 */
	@FieldKey(name = "completion")
	public String writeCompletion()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(awarded)
			.append('/')
			.append(total);
		
		return sb.toString();
	}
	
	
	/**
	 * Write the percentage of awarded achievement.
	 * @return The percentage of awarded achievement.
	 */
	@FieldKey(name = "wonPercent")
	public String writeWonPercent()
	{
		return String.format("%.2f", wonPercent() * 100.0) + '%';
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	public Boolean isHardcoreMode()
	{
		return hardcoreMode;
	}

	public Integer getAwarded()
	{
		return awarded;
	}

	public Integer getTotal()
	{
		return total;
	}

	public Game getGame()
	{
		return game;
	}

	public Integer getScore()
	{
		return score;
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
		result = prime * result + ((game == null) ? 0 : game.hashCode());
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
		CompletionProgress other = (CompletionProgress) obj;
		if (game == null)
		{
			if (other.game != null)
				return false;
		}
		else if (!game.equals(other.game))
			return false;
		return true;
	}
}
