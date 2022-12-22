package com.drxgb.ratracker.controller.view;

import java.util.List;
import java.util.stream.Collectors;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.model.entity.game.Achievement;
import com.drxgb.ratracker.util.annotation.SettingsGroup;
import com.drxgb.ratracker.util.mapping.FieldMapping;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller that retrieves the unlocked achievements by the user.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ListViewController
 * @see Achievement
 */
@SettingsGroup("unlockedAchievements")
public final class UnlockedAchievementsController extends ListViewController<Achievement>
{	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	protected ImageView getIcon(Achievement obj)
	{
		StringBuilder sb = new StringBuilder();
		
		sb
			.append(RATrackerApplication.RA_AWS_URL)
			.append("Badge/")
			.append(obj.getBadgeId())
			.append(".png");
		
		return new ImageView(new Image(
				sb.toString(),
				128,
				128,
				true,
				false
		));
	}

	@Override
	protected List<Achievement> populateList()
	{
		final int count = getViewSettings().getInt("showAchievements");
		return apiService.getUserService()
				.getUser()
				.getRecentAchievements()
				.stream()
				.limit(count)
				.collect(Collectors.toList());
	}

	@Override
	protected String onFieldWrite(String key, Achievement obj)
	{
		return 
				FieldMapping.getMappedResult(
						key,
						obj,
						obj.getGame()
				)
				.getString();
	}

}
