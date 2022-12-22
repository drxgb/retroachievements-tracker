package com.drxgb.ratracker.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.util.StringFormatter;

import javafx.scene.Scene;

/**
 * Utility to ease the styles settings.
 * @author Dr.XGB
 * @version 1.0.0
 */
public class Styles
{
	/*
	 * ===========================================================
	 * 			*** ATTRIBUTES ***
	 * ===========================================================
	 */
	
	/**
	 * The singleton instance.
	 */
	private static Styles self = new Styles();
	
	/**
	 * The loaded styles.
	 */
	private Map<String, String> styles = new HashMap<>();
	
	/**
	 * The current style name.
	 */
	private String selectedStyle;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUCTORS ***
	 * ===========================================================
	 */
	
	/**
	 * Creates a fresh singleton instance.
	 */
	private Styles()
	{}
	
	
	/*
	 * ===========================================================
	 * 			*** PUBLIC METHODS ***
	 * ===========================================================
	 */	
	
	/**
	 * Change to the current style.
	 * @param scene The scene to apply the style.
	 * @return The own instance.
	 */
	public Styles apply(Scene scene)
	{
		if (styles.containsKey(selectedStyle))
		{
			scene.getStylesheets().clear();
			scene.getStylesheets().add(styles.get(selectedStyle));
		}
		return this;
	}

	
	/**
	 * Load the CSS files to be used as themes.
	 * @param basePath The folder to find the CSS files.
	 * @return The won instance.
	 * @throws IOException When the path is not found.
	 * @throws URISyntaxException 
	 */
	public Styles load(String basePath) throws IOException
	{
		Class<RATrackerApplication> clazz = RATrackerApplication.class;
		
		// Production
		try (JarFile jar = new JarFile("XGB RetroAchievements Tracker.jar"))
		{
			Enumeration<JarEntry> e = jar.entries();
			while (e.hasMoreElements())
			{
				JarEntry file = e.nextElement();
				String name = file.getName();
				if (name.startsWith(basePath) && name.endsWith(".css"))
				{
					name = name.replaceFirst(basePath, "").replace(".css", "");
					styles.put(
							StringFormatter.capitalize(name),
							file.getRealName()
					);
				}
			}
		}
		// Development
		catch (NoSuchFileException e)
		{
			basePath = "/" + basePath;
			File dir = new File(clazz.getResource(basePath).getFile());
			
			for (File file : dir.listFiles())
			{
				String name = file.getName().split("\\.")[0];
				styles.put(
						StringFormatter.capitalize(name),
						clazz.getResource(basePath + file.getName()).toExternalForm()
				);
			}
		}
		return this;
	}
	
	
	
	/**
	 * Changes the current style.
	 * @param selectedStyle The new style
	 * @return The own instance.
	 */
	public Styles selectStyle(String selectedStyle)
	{
		this.selectedStyle = selectedStyle;
		return this;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */	
	
	public Map<String, String> getStyles()
	{
		return styles;
	}
	
	
	/**
	 * Gets the singleton instance.
	 * @return The singleton instance.
	 */
	public static Styles getInstance()
	{
		return self;
	}
	
	
	public String getSelectedStyle()
	{
		return selectedStyle;
	}	
}
