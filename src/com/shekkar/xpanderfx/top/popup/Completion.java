/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx.top.popup;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Shekkar Raee
 */
public class Completion extends Stage{
	
	/**
	 * The completion popup class
	 * @param parent 
	 */
	public Completion(Node parent) {
		this.initOwner(parent.getScene().getWindow());
		this.initModality(Modality.APPLICATION_MODAL);
		this.setScene(new Scene(this.getBody(), Color.TRANSPARENT));
	}
	
	@SuppressWarnings("deprecation")
	private BorderPane getBody() {
		return BorderPaneBuilder.create().children(
				VBoxBuilder.create().build()
		).style("-fx-background-color: linear-gradient(").build();
	}
}
