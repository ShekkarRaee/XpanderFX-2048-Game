package com.shekharrai.xpanderfx;

import com.shekharrai.xpanderfx.fxml.controller.XpanderFXController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class XpanderFX extends Application {

    private double deltaX = 0, deltaY = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("/resources/fxml/xpanderfx.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        XpanderFXController controller = loader.getController();
        controller.setApplication(this);
        root.setOnMouseDragged(e -> this.dragStage(e, stage));
        root.setOnMouseMoved(e -> this.calculateGap(e, stage));

        Scene scene = new Scene(root, Color.TRANSPARENT);
        scene.getStylesheets().add("/resources/css/xpanderfx.css");

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    private void calculateGap(MouseEvent event, Stage stage) {
        deltaX = event.getScreenX() - stage.getX();
        deltaY = event.getScreenY() - stage.getY();
    }

    private void dragStage(MouseEvent event, Stage stage) {
        stage.setX(event.getScreenX() - deltaX);
        stage.setY(event.getScreenY() - deltaY);
    }

}
