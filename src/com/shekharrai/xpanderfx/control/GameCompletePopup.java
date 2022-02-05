package com.shekharrai.xpanderfx.control;

import com.shekharrai.xpanderfx.fxml.controller.XpanderFXController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameCompletePopup extends Stage {

    private static final String CONGRATULATIONS_MESSAGE = "Congratulations !! You won the game.";
    private static final String CONTENT_STYLE = "-fx-background-color: linear-gradient(#000000cc, darkslategrey); -fx-background-radius: 15; -fx-border-radius:15;-fx-border-width:1; -fx-border-color:lightgrey";

    private final XpanderFXController controller;

    public GameCompletePopup(XpanderFXController controller, Node parent) {
        this.controller = controller;
        this.initOwner(parent.getScene().getWindow());
        this.initModality(Modality.APPLICATION_MODAL);
        this.setScene(new Scene(this.content(), Color.TRANSPARENT));
        this.initStyle(StageStyle.TRANSPARENT);
    }

    private BorderPane content() {
        BorderPane content = new BorderPane();
        content.setMinSize(200, 440);
        content.setStyle(CONTENT_STYLE);
        content.setCenter(getBody());
        return content;
    }

    private VBox getBody() {
        VBox body = new VBox(
                dummyLabel(10),
                congratulationsMessage(),
                dummyLabel(30),
                decisionBox()
        );
        body.setAlignment(Pos.CENTER);
        return body;
    }

    private Label dummyLabel(int minHeight) {
        Label dummy = new Label();
        dummy.setMinHeight(minHeight);
        return dummy;
    }

    private Label congratulationsMessage() {
        Label message = new Label(CONGRATULATIONS_MESSAGE);
        message.setFont(Font.font("", FontWeight.BOLD, FontPosture.ITALIC, 20));
        message.setTextFill(Color.WHITE);
        return message;
    }

    private HBox decisionBox() {
        HBox decisionBox = new HBox(
                decisionButton("Continue", e -> this.close()),
                decisionButton("Try Again", e -> this.restartGame())
        );
        decisionBox.setAlignment(Pos.CENTER);
        decisionBox.setSpacing(20);
        return decisionBox;
    }

    private void restartGame() {
        controller.restart();
        this.close();
    }

    private Button decisionButton(String text, EventHandler<ActionEvent> action) {
        Button decisionButton = new Button(text);
        decisionButton.minWidth(140);
        decisionButton.setTextFill(Color.WHITE);
        decisionButton.setStyle("-fx-base:darkslategrey");
        decisionButton.setFont(Font.font("", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 18));
        decisionButton.setOnAction(action);
        return decisionButton;
    }
}
