package com.drxgb.ratracker.controller.view;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.model.entity.user.User;
import com.drxgb.ratracker.model.service.UserService;
import com.drxgb.ratracker.util.annotation.SettingsGroup;
import com.drxgb.ratracker.util.mapping.FieldMapping;

import javafx.scene.image.ImageView;

/**
 * Controller that retrieves the user stats.
 * @author Dr.XGB
 * @version 1.0.0
 * @see SingleViewController
 */
@SettingsGroup("userStats")
public final class UserStatsController extends SingleViewController
{	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */
	
	/**
	 * @see com.drxgb.ratracker.controller.view.ViewInterface#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		return apiService.getUserService().getUser() != null;
	}
	
	
	@Override
	protected ImageView getIcon()
	{
		User user = apiService.getUserService().getUser();
		String iconUrl = RATrackerApplication.RA_URL + user.getPicture();
		return new ImageView(iconUrl);
	}


	@Override
	protected String onFieldWrite(String key)
	{
		UserService service = apiService.getUserService();		
		return 
				FieldMapping.getMappedResult(
						key,
						service,
						service.getUser()
				)
				.getString();
	}
}
