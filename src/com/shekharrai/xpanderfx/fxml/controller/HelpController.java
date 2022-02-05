package com.shekharrai.xpanderfx.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    private @FXML
    Button OK;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    public @FXML
    void helpClose(ActionEvent event) {
        Stage stage = (Stage) OK.getScene().getWindow();
        stage.close();
    }
}
