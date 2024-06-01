package com.drxgb.ratracker.controller.view;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.factory.ProgressBarFactory;
import com.drxgb.ratracker.model.entity.game.Achievement;
import com.drxgb.ratracker.util.annotation.SettingsGroup;
import com.drxgb.ratracker.util.mapping.FieldMapping;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller that retrieves the next achievement to be
 * unlocked by the user.
 * @author Dr.XGB
 * @version 1.0.0
 * @see SingleViewController
 */
@SettingsGroup("nextAchievement")
public final class NextAchievementController extends SingleViewController
{	
	/*
	 * ===========================================================
	 * 			*** ASSOCIATIONS ***
	 * ===========================================================
	 */
	
	/**
	 * The target achievement.
	 */
	private Achievement achievement;
	
	
	/*
	 * ===========================================================
	 * 			*** IMPLEMENTED METHODS ***
	 * ===========================================================
	 */

	@Override
	public void onUpdate()
	{
		achievement = apiService.getUserService().getNextAchievement();
		super.onUpdate();
	}


	@Override
	protected ImageView getIcon()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(RATrackerApplication.RA_AWS_URL)
			.append("Badge/")
			.append(achievement.getBadgeId())
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
	protected String onFieldWrite(String key)
	{
		return FieldMapping.getMappedResult(
						key,
						achievement,
						apiService.getUserService()
				)
				.getString();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PROTECTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	protected Node writeValue(Label lblKey, String key)
	{
		if (key.equals("wonPercent") && getViewSettings().getInt("wonByDisplay") == 0)
		{
			Label label = new Label();
			Pane pane = ProgressBarFactory.createWithLabel(
					achievement.unlocksByPlayers(),
					new VBox(),
					label
			);
			
			pane.setPrefWidth(Pane.USE_COMPUTED_SIZE);
			pane.setMinWidth(Pane.USE_PREF_SIZE);
			pane.setMaxWidth(Double.MAX_VALUE);
			label.setTextFill(lblKey.getTextFill());			

			return pane;
		}
		return super.writeValue(lblKey, key);
	}
}
