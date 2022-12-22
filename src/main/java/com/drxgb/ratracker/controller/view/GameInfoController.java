package com.drxgb.ratracker.controller.view;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.model.entity.game.Game;
import com.drxgb.ratracker.model.entity.user.User;
import com.drxgb.ratracker.util.annotation.SettingsGroup;
import com.drxgb.ratracker.util.mapping.FieldMapping;

import javafx.scene.image.ImageView;

/**
 * Controller that retrieves the last game info.
 * @author Dr.XGB
 * @version 1.0.0
 * @see SingleViewController
 */
@SettingsGroup("gameInfo")
public final class GameInfoController extends SingleViewController
{	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */	
	
	@Override
	protected ImageView getIcon()
	{
		User user = apiService.getUserService().getUser();
		Game game = user.getLastGame();
		String iconUrl = RATrackerApplication.RA_URL + game.getImage().getIcon();
		return new ImageView(iconUrl);
	}
	

	@Override
	protected String onFieldWrite(String key)
	{
		Game game = apiService
				.getUserService()
				.getUser()
				.getLastGame();

		return FieldMapping.getMappedResult(
						key,
						game,
						game.getConsole()
				)
				.getString();
	}
}
