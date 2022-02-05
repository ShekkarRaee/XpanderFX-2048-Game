package com.shekharrai.xpanderfx.fxml.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    private Button OK;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void okButtonHandler(MouseEvent event, Popup popup) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.3), (Node) popup.getContent());
        st.setToX(0);
        st.play();

        FadeTransition fd = new FadeTransition(Duration.seconds(.3), (Node) popup.getContent());
        fd.setToValue(.2);
        st.setOnFinished(e -> popup.hide());
    }
}
