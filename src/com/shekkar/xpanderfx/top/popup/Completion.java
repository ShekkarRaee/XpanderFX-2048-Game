/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx.top.popup;

import com.shekkar.xpanderfx.MainFXMLDocumentController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Shekkar Raee
 */
public class Completion extends Stage{
	/**
	 * The controller of the XpanderFX
	 */
	private MainFXMLDocumentController controller;
	
	/**
	 * The completion pop-up class
	 * @param controller
	 * @param parent
	 */
	public Completion(MainFXMLDocumentController controller, Node parent) {
		this.controller = controller;
		this.initOwner(parent.getScene().getWindow());
		this.initModality(Modality.APPLICATION_MODAL);
		this.setScene(new Scene(this.getBody(), Color.TRANSPARENT));
		this.initStyle(StageStyle.TRANSPARENT);
	}
	
	/**
	 * 
	 * @return root-node
	 */
	@SuppressWarnings("deprecation")
	private BorderPane getBody() {
		return BorderPaneBuilder.create().minHeight(200).minWidth(440).center(
				VBoxBuilder.create().alignment(Pos.CENTER).children(
						LabelBuilder.create()
								.minHeight(10).build(),
						LabelBuilder.create().text("Game Completed.  You scored 2048.")
							.font(Font.font("", FontWeight.BOLD, FontPosture.ITALIC, 20))
								.textFill(Color.WHITE).build(),
						LabelBuilder.create()
								.minHeight(30).build(),
						HBoxBuilder.create().alignment(Pos.CENTER).spacing(20).children(
								ButtonBuilder.create().text("Continue").font(Font.font("", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 18))
										.minWidth(140).textFill(Color.WHITE).style("-fx-base:darkslategrey")
										.onAction(e -> this.close()).build(),
								ButtonBuilder.create().text("Try Again").font(Font.font("", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 18))
										.minWidth(140).textFill(Color.WHITE).style("-fx-base:darkslategrey")
										.onAction(e -> {
											controller.restart();	
											this.close();
										}).build()
						).build()
				)
				.build()
		).style("-fx-background-color: linear-gradient(#000000cc, darkslategrey); -fx-background-radius: 15; -fx-border-radius:15;"
				+ "-fx-border-width:1; -fx-border-color:lightgrey").build();
	}
}
