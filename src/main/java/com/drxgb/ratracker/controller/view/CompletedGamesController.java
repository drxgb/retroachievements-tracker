package com.drxgb.ratracker.controller.view;

import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import com.drxgb.ratracker.RATrackerApplication;
import com.drxgb.ratracker.factory.ProgressBarFactory;
import com.drxgb.ratracker.model.entity.game.CompletionProgress;
import com.drxgb.ratracker.util.annotation.SettingsGroup;
import com.drxgb.ratracker.util.mapping.FieldMapping;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Controller that retrieves all the completed games by the user.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ListViewController
 * @see CompletionProgress
 */
@SettingsGroup("completedGames")
public final class CompletedGamesController extends ListViewController<CompletionProgress>
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
		return true;
	}
	

	@Override
	protected ImageView getIcon(CompletionProgress obj)
	{
		String raUrl = RATrackerApplication.RA_URL;
		String imgPath = obj.getGame()
				.getImage()
				.getIcon();
		
		return new ImageView(raUrl + imgPath);
	}


	@Override
	protected String onFieldWrite(String key, CompletionProgress obj)
	{
		return FieldMapping.getMappedResult(
						key,
						obj,
						obj.getGame(),
						obj.getGame().getConsole()
				).getString();
	}	


	@Override
	protected List<CompletionProgress> populateList()
	{
		JsonObject settings = getViewSettings();
		final boolean showOnlyMastered = settings.getBoolean("mastered");
		
		return apiService.getUserService()
				.getUser()
				.getGames()
				.stream()
				.sorted()
				.distinct()
				.filter(p -> (showOnlyMastered && p.wonPercent() == 1.0) || !showOnlyMastered)
				.limit(settings.getInt("showGames"))
				.collect(Collectors.toList());
	}
	
	
	/*
	 * ===========================================================
	 * 			*** PROTECTED METHODS ***
	 * ===========================================================
	 */
	
	@Override
	protected void writeField(String key, int row, GridPane gridPane)
	{
		if (key.equals("hardcore"))
		{
			JsonObject phrases = getPhrases();
			JsonObject layout = getLayout();
			final String fontName = layout.getString("font");
			final double fontSize = layout.getJsonNumber("size").doubleValue();
			Label label = new Label(phrases.getString(key));
			Font font = Font.font(fontName, FontWeight.EXTRA_BOLD, fontSize);
			
			label.setTextFill(Color.web(layout.getString("foreground")));
			label.setFont(font);
			
			gridPane.addRow(row, label);
			GridPane.setColumnSpan(label, 2);
			GridPane.setHalignment(label, HPos.RIGHT);
			return;
		}
		super.writeField(key, row, gridPane);
	}
	
	
	@Override
	protected Node writeValue(Label lblKey, String key)
	{
		if (key.equals("wonPercent") && getViewSettings().getInt("wonByDisplay") == 0)
		{
			CompletionProgress progress = elements.get(iterator.nextIndex() - 1);
			Label label = new Label();
			Pane pane = ProgressBarFactory.createWithLabel(
					progress.wonPercent(),
					new VBox(),
					label
			);
			label.setTextFill(lblKey.getTextFill());			
			return pane;
		}
		return super.writeValue(lblKey, key);
	}
}
