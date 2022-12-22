package com.drxgb.ratracker.model.entity.game;

import com.drxgb.ratracker.util.annotation.FieldKey;

/**
 * The game console.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class Console
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
	 * The console name.
	 */
	@FieldKey(name = "console")
	private String name;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Create a console containing its unique ID and a name.
	 * @param id The unique ID.
	 * @param name The console name.
	 */
	public Console(Long id, String name)
	{
		this.id = id;
		this.name = name;
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

	public String getName()
	{
		return name;
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
		this.name = name;
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
		Console other = (Console) obj;
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
		builder.append("Console [id=").append(id).append(", name=").append(name).append("]");
		return builder.toString();
	}
}
