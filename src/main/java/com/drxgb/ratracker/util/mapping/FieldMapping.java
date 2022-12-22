package com.drxgb.ratracker.util.mapping;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Function;

import com.drxgb.ratracker.util.annotation.FieldKey;
import com.drxgb.ratracker.util.annotation.FieldKeyGroup;


/**
 * Utility to map members to a settings field key.
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class FieldMapping
{
	/**
	 * Get a mapped value from a field key.
	 * @param fieldKey The field key.
	 * @param objects a bunch of objects to be mapped.
	 * @return The found value.
	 */
	public static MappingValue getMappedResult(String fieldKey, Object...objects)
	{
		for (Object obj : objects)
		{
			for (Field field : obj.getClass().getDeclaredFields())
			{
				MappingValue out = findAnnotationAndFetch(
						fieldKey,
						field,
						key -> {
							try
							{
								field.setAccessible(true);
								return createValue(field.get(obj), key);
							}
							catch (IllegalArgumentException | IllegalAccessException e)
							{
								e.printStackTrace();
							}
							return null;
						}
				);
				
				if (out != null)
					return out;
			}
			
			for (Method method : obj.getClass().getDeclaredMethods())
			{
				MappingValue out = findAnnotationAndFetch(
						fieldKey,
						method,
						key -> {
							try
							{
								return createValue(method.invoke(obj), key);
							}
							catch (
									InvocationTargetException | 
									IllegalAccessException | 
									IllegalArgumentException e
								)
							{
								e.printStackTrace();
							}
							return null;
						}
				);
				
				if (out != null)
					return out;
			}
		}	
		return null;
	}
	
	
	/**
	 * Find the annotation through the class members.
	 * @param fieldKey The field key.
	 * @param member The requested member.
	 * @param fnFetch Callback to fetch the search results.
	 * @return The first value found.
	 */
	private static MappingValue findAnnotationAndFetch(
			String fieldKey,
			AccessibleObject member,
			Function<FieldKey, MappingValue> fnFetch
		)
	{
		Function<FieldKey, MappingValue> fnFetchKey = key -> {
			if (fieldKeyExists(fieldKey, key, (Member) member))
				return fnFetch.apply(key);
			return null;
		};
		
		if (member.isAnnotationPresent(FieldKeyGroup.class))
		{
			for (FieldKey key : member.getAnnotation(FieldKeyGroup.class).value())
			{
				MappingValue value = fnFetchKey.apply(key);
				if (value != null)
					return value;
			}
		}		
		if (member.isAnnotationPresent(FieldKey.class))
			return fnFetchKey.apply(member.getAnnotation(FieldKey.class));
		return null;
	}
	
	
	/**
	 * Check whether the field key exists.
	 * @param <T> Type of member. Usually a parameter or a method.
	 * @param fieldKey The field key.
	 * @param key The field key annotation.
	 * @param member The member.
	 * @return If the field key exists.
	 */
	private static <T extends Member> boolean fieldKeyExists(String fieldKey, FieldKey key, T member)
	{
		if (key.name().equals(fieldKey))
			return true;
		if (key.name().equals("") && member.getName().equals(fieldKey))
			return true;
		return false;
	}
	
	
	/**
	 * Creates a mapping value.<br>
	 * If the field is a collection and the mapper requests
	 * the collection size (when <code>FieldKey.count()</code> is <code>true</code>),
	 * so it will return the size value instead the collection.
	 * @param value The object value.
	 * @param key The field key.
	 * @return The value.
	 */
	private static MappingValue createValue(Object value, FieldKey key)
	{
		MappingValue mv = new MappingValue(value);
		
		if (key.count() && value instanceof Collection<?>)
			mv.set(((Collection<?>) value).size());
		
		return mv;
	}
}
