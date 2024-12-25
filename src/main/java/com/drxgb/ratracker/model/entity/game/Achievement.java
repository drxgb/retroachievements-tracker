package com.drxgb.ratracker.model.entity.game;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.model.service.MainService;
import com.drxgb.ratracker.util.annotation.FieldKey;

/**
 * The game achievement.
 * @author Dr.XGB
 * @see 1.0.0
 */
public class Achievement implements Comparable<Achievement>
{	
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The unique ID
	 */
	private Long id;
	
	/**
	 * The badge image ID to be loaded from the server.
	 */
	private Long badgeId;
	
	/**
	 * The achievement title.
	 */
	@FieldKey private String title;
	
	/**
	 * The achievement description
	 */
	@FieldKey private String description;
	
	/**
	 * Worth of points.
	 */
	@FieldKey private Integer points;
	
	/**
	 * Worth of Hardcore points 
	 */
	@FieldKey(name = "truePoints")
	private Integer trueRatio;
	
	/**
	 * Amount of players that have unlocked this achievement.
	 */
	private Integer unlocks;
	
	/**
	 * Amount of players that have unlocked this achievement
	 * on Hardcore mode.
	 */
	private Integer hardcoreUnlocks;
	
	/**
	 * The name of achievement's author.
	 */
	private String author;
	
	/**
	 * The order value to be displayed in the list.
	 */
	private Integer displayOrder;
	
	/**
	 * When the achievement was created.
	 */
	private Date createdAt;
	
	/**
	 * When the achievement was modified from the last time.
	 */
	private Date modifiedAt;
	
	/**
	 * When the user has earned this achievement.<br>
	 * If wasn't, the value must be <code>null</code>.
	 */
	private Date earnedAt;
	
	/**
	 * When the user has earned this achievement on Hardcore mode.<br>
	 * If wasn't, the value must be <code>null</code>.
	 */
	private Date earnedHardcoreAt;	
	

	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The game that this achievement belongs.
	 */
	private Game game;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a fresh achievement.
	 */
	public Achievement() {}
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	public int compareTo(Achievement o)
	{
		return displayOrder - o.getDisplayOrder();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	public String getBadgePath()
	{
		return new StringBuilder()
				.append(RATrackerApplication.RA_AWS_URL)
				.append("Badge/")
				.append(badgeId)
				.append(".png")
				.toString();
	}
	

	/**
	 * Check whether this achievement was earned by the current user.
	 * @return <code> if was earned or <code>false</code> if wasn't.
	 */
	public boolean earned()
	{
		return earnedAt != null;
	}
	
	
	/**
	 * Check whether this achievement was earned on Hardcore mode
	 * by the current user.
	 * @return <code> if was earned or <code>false</code> if wasn't.
	 */
	public boolean earnedHardcore()
	{
		return earnedHardcoreAt != null;
	}
	
	
	/**
	 * Computes a percentage of players that unlocked
	 * this achievement.
	 * @return The percent value of players that unlocked
	 */
	public double unlocksByPlayers()
	{
		return (double) unlocks / (double) game.getTotalPlayers();
	}

	
	/**
	 * Write a message containing the moment when this achievement
	 * was unlocked by the current user.<br>
	 * The data of this message will be formatted according to the
	 * user settings.
	 * @return The message with the award date.
	 * @throws IOException When the main service is not loaded.
	 */
	@FieldKey
	public String awardedAt() throws IOException
	{
		String dateFormat = MainService.getInstance()
				.getSettings()
				.getJsonObject("general")
				.getString("dateFormat", null);
		Date awardedAt = (earnedHardcore()) 
			? getEarnedHardcoreAt()
			: getEarnedAt();
		SimpleDateFormat sdf = dateFormat != null
				? new SimpleDateFormat(dateFormat)
				: RATrackerApplication.getDateFormat();
	
		if (awardedAt == null)
			return "";
		return sdf.format(awardedAt);
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

	public Long getBadgeId()
	{
		return badgeId;
	}

	public String getTitle()
	{
		return title;
	}

	public String getDescription()
	{
		return description;
	}

	public Integer getPoints()
	{
		return points;
	}

	public Integer getTrueRatio()
	{
		return trueRatio;
	}

	public Integer getUnlocks()
	{
		return unlocks;
	}

	public Integer getHardcoreUnlocks()
	{
		return hardcoreUnlocks;
	}

	public String getAuthor()
	{
		return author;
	}

	public Integer getDisplayOrder()
	{
		return displayOrder;
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}
	
	public Date getModifiedAt()
	{
		return modifiedAt;
	}

	public Date getEarnedAt()
	{
		return earnedAt;
	}

	public Date getEarnedHardcoreAt()
	{
		return earnedHardcoreAt;
	}
	
	public Game getGame()
	{
		return game;
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

	public void setBadgeId(Long badgeId)
	{
		this.badgeId = badgeId;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setPoints(Integer points)
	{
		this.points = points;
	}

	public void setTrueRatio(Integer truePoints)
	{
		this.trueRatio = truePoints;
	}

	public void setUnlocks(Integer unlocks)
	{
		this.unlocks = unlocks;
	}

	public void setHardcoreUnlocks(Integer hardcoreUnlocks)
	{
		this.hardcoreUnlocks = hardcoreUnlocks;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public void setDisplayOrder(Integer displayOrder)
	{
		this.displayOrder = displayOrder;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}
	
	public void setModifiedAt(Date modifiedAt)
	{
		this.modifiedAt = modifiedAt;
	}

	public void setEarnedAt(Date earnedAt)
	{
		this.earnedAt = earnedAt;
	}

	public void setEarnedHardcoreAt(Date earnedHardcoreAt)
	{
		this.earnedHardcoreAt = earnedHardcoreAt;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
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
		Achievement other = (Achievement) obj;
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
		builder.append("Achievement [id=").append(id).append(", badgeId=").append(badgeId).append(", title=")
				.append(title).append(", description=").append(description).append(", points=").append(points)
				.append(", trueRatio=").append(trueRatio).append(", unlocks=").append(unlocks)
				.append(", hardcoreUnlocks=").append(hardcoreUnlocks).append(", author=").append(author)
				.append(", displayOrder=").append(displayOrder).append(", createdAt=").append(createdAt)
				.append(", modifiedAt=").append(modifiedAt).append(", earnedAt=").append(earnedAt)
				.append(", earnedHardcoreAt=").append(earnedHardcoreAt).append(", game=").append(game).append("]");
		return builder.toString();
	}	
}
