package com.drxgb.ratracker.model.entity.game;

/**
 * A set of images that belongs a game.
 * @author Dr.XGB
 * @version 1.0.0
 * @see Game
 */
public class GameImage
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The main icon.
	 */
	private String icon;
	
	/**
	 * Usually it is the game title screenshot.
	 */
	private String title;
	
	/**
	 * A screenshot of the gameplay.
	 */
	private String inGame;
	
	/**
	 * The game box art.
	 */
	private String boxArt;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a full set of images.
	 * @param icon The main icon.
	 * @param title The title icon.
	 * @param inGame Gameplay screenshot.
	 * @param boxArt The box art.
	 */
	public GameImage(String icon, String title, String inGame, String boxArt)
	{
		this.icon = icon;
		this.title = title;
		this.inGame = inGame;
		this.boxArt = boxArt;
	}
	
	
	/**
	 * Creates a set of images containing only the main icon.
	 * @param icon The main icon.
	 */
	public GameImage(String icon)
	{
		this(icon, null, null, null);
	}

	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	public String getIcon()
	{
		return icon;
	}

	public String getTitle()
	{
		return title;
	}

	public String getInGame()
	{
		return inGame;
	}

	public String getBoxArt()
	{
		return boxArt;
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
		builder.append("GameImage [icon=").append(icon).append(", title=").append(title).append(", inGame=")
				.append(inGame).append(", boxArt=").append(boxArt).append("]");
		return builder.toString();
	}
}
