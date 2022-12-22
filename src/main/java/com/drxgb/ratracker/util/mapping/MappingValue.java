package com.drxgb.ratracker.util.mapping;

/**
 * Utility to store a mapped value.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class MappingValue
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The actual value;
	 */
	private Object value;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a mapped value.
	 * @param value The value to be mapped.
	 */
	public MappingValue(Object value)
	{
		this.value = value;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * Retrieves the actual value.
	 * @return The actual value.
	 */
	public Object get()
	{
		return value;
	}
	
	
	/**
	 * Changes the actual value.
	 * @param value The new value.
	 */
	public void set(Object value)
	{
		this.value = value;
	}
	
	
	/**
	 * Casts the actual value to <code>String</code>.
	 * @return A value formatted to string.
	 */
	public String getString()
	{
		return String.valueOf(value);
	}
	
	
	/**
	 * Casts the actual value to <code>char</code>.
	 * @return A value formatted to char.
	 */
	public char getChar()
	{
		return (char) getInt();
	}
	
	
	/**
	 * Casts the actual value to <code>Integer</code>.
	 * @return A value formatted to integer.
	 */
	public int getInt()
	{
		return Integer.parseInt(getString());
	}
	
	
	/**
	 * Casts the actual value to <code>Byte</code>.
	 * @return A value formatted to byte.
	 */
	public byte getByte()
	{
		return Byte.parseByte(getString());
	}
	
	
	/**
	 * Casts the actual value to <code>Long</code>.
	 * @return A value formatted to long.
	 */
	public long getLong()
	{
		return Long.parseLong(getString());
	}
	
	
	/**
	 * Casts the actual value to <code>Float</code>.
	 * @return A value formatted to float.
	 */
	public float getFloat()
	{
		return Float.parseFloat(getString());
	}
	
	
	/**
	 * Casts the actual value to <code>Double</code>.
	 * @return A value formatted to double.
	 */
	public double getDouble()
	{
		return Double.parseDouble(getString());
	}
	
	
	/**
	 * Casts the actual value to <code>Boolean</code>.
	 * @return A value formatted to boolean.
	 */
	public boolean getBoolean()
	{
		return Boolean.parseBoolean(getString());
	}
}
