package com.drxgb.ratracker.model.entity.user;

import java.util.ArrayList;
import java.util.List;

import com.drxgb.ratracker.model.entity.game.Achievement;
import com.drxgb.ratracker.model.entity.game.CompletionProgress;
import com.drxgb.ratracker.model.entity.game.Game;
import com.drxgb.ratracker.util.annotation.FieldKey;
import com.drxgb.util.StringFormatter;

/**
 * The current session user.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class User
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
	 * The user name.
	 */
	@FieldKey(name = "user")
	private String name;
	
	/**
	 * Total points earned by the user.
	 */
	@FieldKey(name = "totalPoints")
	private Integer points;
	
	/**
	 * Total of true points earned by the user.
	 */
	@FieldKey(name = "totalTruePoints")
	private Integer truePoints;
	
	/**
	 * The user's motto.
	 */
	@FieldKey private String motto;
	
	/**
	 * The actual user rank in RA.
	 */
	@FieldKey private Integer rank;
	
	/**
	 * The last user state.
	 */
	@FieldKey private String presenceMessage;
	
	
	/**
	 * The avatar file name.
	 */
	private String picture;
	
	/**
	 * The retro ratio.
	 */
	private Double retroRatio;
	
	/**
	 * The user status.
	 */
	private UserStatus status;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * A list of games played by the user.
	 */
	private List<CompletionProgress> games = new ArrayList<>();
	
	/**
	 * A list of recent achievements awared to the user.
	 */
	private List<Achievement> recentAchievements = new ArrayList<>();
	
	/**
	 * The last game played by the user.
	 */
	private Game lastGame;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a fresh user.
	 */
	public User() {}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	public Long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getPicture()
	{
		return picture;
	}

	public String getMotto()
	{
		return motto;
	}

	public Integer getRank()
	{
		return rank;
	}

	public Integer getPoints()
	{
		return points;
	}

	public Integer getTruePoints()
	{
		return truePoints;
	}

	public Double getRetroRatio()
	{
		return retroRatio;
	}

	public String getPresenceMessage()
	{
		return presenceMessage;
	}

	public List<CompletionProgress> getGames()
	{
		return games;
	}
	
	public List<Achievement> getRecentAchievements()
	{
		return recentAchievements;
	}

	public Game getLastGame()
	{
		return lastGame;		
	}
	
	public UserStatus getStatus()
	{
		return status;
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

	public void setName(String name)
	{
		this.name = StringFormatter.unescape(name);
	}

	public void setPicture(String picture)
	{
		this.picture = picture.replace("\\/", "/");
	}

	public void setMotto(String motto)
	{
		this.motto = StringFormatter.unescape(motto);
	}

	public void setRank(Integer rank)
	{
		this.rank = rank;
	}

	public void setPoints(Integer points)
	{
		this.points = points;
	}

	public void setTruePoints(Integer truePoints)
	{
		this.truePoints = truePoints;
	}

	public void setRetroRatio(Double retroRatio)
	{
		this.retroRatio = retroRatio;
	}

	public void setPresenceMessage(String presenceMessage)
	{
		this.presenceMessage = StringFormatter.unescape(presenceMessage);
	}

	public void setGames(List<CompletionProgress> games)
	{
		this.games = games;
	}
	
	public void setRecentAchievements(List<Achievement> recentAchievements)
	{
		this.recentAchievements = recentAchievements;
	}

	
	public void setLastGame(Game lastGame)
	{
		this.lastGame = lastGame;
	}
	
	public void setStatus(UserStatus status)
	{
		this.status = status;
	}
	
	public void setStatus(String status)
	{
		if (status == null)
			return;
		this.status = UserStatus.valueOf(status.toUpperCase());
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
		User other = (User) obj;
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
		builder.append("User [id=").append(id).append(", name=").append(name).append(", picture=").append(picture)
				.append(", motto=").append(motto).append(", rank=").append(rank).append(", points=").append(points)
				.append(", truePoints=").append(truePoints).append(", retroRatio=").append(retroRatio)
				.append(", presenceMessage=").append(presenceMessage).append(", games=").append(games).append("]");
		return builder.toString();
	}	
}
