package com.drxgb.ratracker.factory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

/**
 * Utility to instanciate and set a <code>ProgressBar</code> JavaFX node.
 * @author Dr.XGB
 * @version 1.0.0
 * @see ProgressBar
 */
public abstract class ProgressBarFactory
{
	/**
	 * Creates a fresh <code>ProgressBar</code> instance.
	 * @param progress The progress amount from 0 to 1.
	 * @return A <code>ProgressBar</code> node.
	 */
	public static ProgressBar create(double progress)
	{
		ProgressBar pb = new ProgressBar(progress);		
		pb.maxWidth(Double.MAX_VALUE);
		pb.prefWidth(Pane.USE_COMPUTED_SIZE);
		return pb;
	}
	
	
	/**
	 * Creates a fresh <code>ProgressBar</code> instance
	 * starting at 0%.
	 * @return A <code>ProgressBar</code> node.
	 */
	public static ProgressBar create()
	{
		return create(0.0);
	}
	
	
	/**
	 * Creates a <code>ProgressBar</code> instance inside a <code>Pane</code>.
	 * @param <T> Type of <code>Pane</code>.
	 * @param progress The progress amount from 0 to 1.
	 * @param container The pane container to place the <code>ProgressBar</code> node.
	 * @return The container with the progress bar inside.
	 * @see Pane
	 */
	public static <T extends Pane> T createWithContainer(double progress, T container)
	{
		ProgressBar pb = create(progress);
		container.getChildren().add(pb);
		pb.prefWidthProperty().bind(container.widthProperty());
		return container;
	}
	
	
	/**
	 * Creates a <code>ProgressBar</code> instance containing
	 * a progress label. 
	 * @param <T> Type of <code>Pane</code>.
	 * @param progress The progress amount from 0 to 1.
	 * @param container The pane container to place the <code>ProgressBar</code> node.
	 * @param label The <code>Label</code> reference to write the progress amount.
	 * @return The container with the progress bar and a label inside.
	 * @see Pane
	 * @see Label
	 */
	public static <T extends Pane> T createWithLabel(double progress, T container, Label label)
	{
		ProgressBar pg;
		
		label.setText(String.format("%.2f", progress * 100.0) + '%');		
		label.setAlignment(Pos.CENTER);
		label.prefWidthProperty().bind(container.widthProperty());
		container = createWithContainer(progress, container);
		container.getChildren().add(label);
		pg = (ProgressBar) container.getChildren().get(0);
		pg.progressProperty().addListener((obs, oldValue, newValue) -> {
			label.setText(String.format("%.2f", (Double) newValue * 100.0) + '%');
		});

		return container;
	}
}
