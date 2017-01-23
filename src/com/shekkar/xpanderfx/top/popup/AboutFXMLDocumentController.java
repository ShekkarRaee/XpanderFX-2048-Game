/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx.top.popup;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Shekkar Raee
 */
public class AboutFXMLDocumentController implements Initializable {

	@FXML
	private Button OKAY;
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		//this.OKAY
	}	
	
	public void oakyButtonHandler(MouseEvent event, Popup pp) {
		ScaleTransition st = new ScaleTransition(Duration.seconds(.3), (Node) pp.getContent());
		st.setToX(0);
		st.play();
		
		FadeTransition fd = new FadeTransition(Duration.seconds(.3), (Node) pp.getContent());
		fd.setToValue(.2);
		st.setOnFinished(e -> pp.hide());
	}
}
