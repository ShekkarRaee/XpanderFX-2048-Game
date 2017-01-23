/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Shekkar Raee
 */
public class XpanderFX extends Application {
	
	private double gap_x = 0,gap_y = 0;
	
	@Override
	public void start(Stage stage) throws Exception {
		URL location = getClass().getResource("MainFXMLDocument.fxml");
		Parent root = FXMLLoader.load(location);
		
		root.setOnMouseDragged(e -> this.dragStage(e, stage));
		root.setOnMouseMoved(e -> this.calculateGap(e, stage));
		
		Scene scene = new Scene(root, Color.TRANSPARENT);
		scene.getStylesheets().add("/com/shekkar/xpanderfx/mainStyler.css");
		
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.initStyle(StageStyle.TRANSPARENT);		
		stage.show();		
	}
	
	/**
	 * Calculating gap of location between the mouse and the stage on <i>Mouse Moving Event</i>.
	 * @param event
	 * @param stage 
	 */
	private void calculateGap(MouseEvent event, Stage stage) {
		gap_x = event.getScreenX() - stage.getX();
		gap_y = event.getScreenY() - stage.getY();
	}
	
	/**
	 * Dragging of stage on <i>Mouse Dragged Event</i>.
	 * @param event
	 * @param stage 
	 */
	private void dragStage(MouseEvent event, Stage stage) {
		stage.setX(event.getScreenX() - gap_x);
		stage.setY(event.getScreenY() - gap_y);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
