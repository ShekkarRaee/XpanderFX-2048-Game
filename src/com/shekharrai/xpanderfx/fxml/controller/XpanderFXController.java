package com.shekharrai.xpanderfx.fxml.controller;

import com.shekharrai.xpanderfx.XpanderFX;
import com.shekharrai.xpanderfx.control.Direction;
import com.shekharrai.xpanderfx.control.GameCompletePopup;
import com.shekharrai.xpanderfx.score.ScoreProperties;
import com.shekharrai.xpanderfx.tile.Tile;
import com.shekharrai.xpanderfx.tile.TileBackground;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XpanderFXController implements Initializable {

    public @FXML
    Button UP, DOWN, LEFT, RIGHT, ICON, MINIMISER, EXITER;

    private @FXML
    BorderPane MAIN_PANE;

    private @FXML
    ContextMenu ABOUT;

    private @FXML
    Label BEST, SCORE, PLUS_SCORE;

    private @FXML
    TilePane GAME_BOARD;

    private Popup about, help;
    private VBox aboutBox, helpBox;
    private BlockingQueue<Direction> moving_queues;
    private boolean isGameComplete = false;
    private TileBackground tileBackgrounds[][];
    private Tile tiles[][];
    private XpanderFX app;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rightSideInit();
        topSideInit();
        centerSideInit();
        controlInit();
        start(Duration.seconds(1.34));
        moving_queues = new ArrayBlockingQueue<>(100);
    }

    private void rightSideInit() {
        UP.setGraphic(this.getImage("/resources/image/up.png", 70));
        DOWN.setGraphic(this.getImage("/resources/image/down.png", 70));
        RIGHT.setGraphic(this.getImage("/resources/image/right.png", 70));
        LEFT.setGraphic(this.getImage("/resources/image/left.png", 70));

        UP.setOnMouseClicked(e -> this.buttonClicked(UP, KeyCode.UP));
        DOWN.setOnMouseClicked(e -> this.buttonClicked(DOWN, KeyCode.DOWN));
        LEFT.setOnMouseClicked(e -> this.buttonClicked(LEFT, KeyCode.LEFT));
        RIGHT.setOnMouseClicked(e -> this.buttonClicked(RIGHT, KeyCode.RIGHT));

        UP.setOnMouseEntered(e -> this.buttonHovered(UP));
        DOWN.setOnMouseEntered(e -> this.buttonHovered(DOWN));
        LEFT.setOnMouseEntered(e -> this.buttonHovered(LEFT));
        RIGHT.setOnMouseEntered(e -> this.buttonHovered(RIGHT));

        UP.setOnMouseExited(e -> this.buttonHoveredOff(UP));
        DOWN.setOnMouseExited(e -> this.buttonHoveredOff(DOWN));
        RIGHT.setOnMouseExited(e -> this.buttonHoveredOff(RIGHT));
        LEFT.setOnMouseExited(e -> this.buttonHoveredOff(LEFT));

        PLUS_SCORE.setMinSize(60, 45);
        PLUS_SCORE.setTextAlignment(TextAlignment.CENTER);

    }

    private void topSideInit() {
        this.ICON.setGraphic(this.getImage("/resources/image/icon.png", 57));
        this.EXITER.setGraphic(this.getImage("/resources/image/exit.png", 57));
        this.MINIMISER.setGraphic(this.getImage("/resources/image/mini.png", 57));
        about = new Popup();
        help = new Popup();

    }

    private void centerSideInit() {
        tileBackgrounds = new TileBackground[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tileBackgrounds[i][j] = new TileBackground();
                GAME_BOARD.getChildren().add(tileBackgrounds[i][j]);
            }
        }
        tiles = new Tile[4][4];
    }

    private void controlInit() {
        MAIN_PANE.setOnKeyReleased(this::keyPressedOnApp);
    }

    public synchronized void start(Duration duration) {
        ScoreProperties properties = new ScoreProperties();
        try {
            this.BEST.setText(String.valueOf(properties.getBest()));
        } catch (IOException e) {
            e.printStackTrace();
            this.BEST.setText("0");
        }
        int[] values = {2, 4};
        for (int value : values) {
            this.addNewTile(String.valueOf(value), duration);
        }
    }

    public synchronized void restart() {
        tiles = new Tile[4][4];
        this.freeGraphics();
        SCORE.setText("0");
        start(Duration.seconds(.001));
    }

    public void iconClicked(MouseEvent event) {
        ABOUT = this.getAboutMenu();
        this.ABOUT.show(this.ICON.getScene().getWindow(), this.getNodeMaxX() - 60, this.getNodeMaxY() - 5);

    }

    private synchronized boolean move(Direction direction) {
        boolean movable = true;
        if (direction.getKey().equals(KeyCode.UP)) movable = this.upMove(direction);
        if (direction.getKey().equals(KeyCode.RIGHT)) movable = this.rightMove(direction);
        if (direction.getKey().equals(KeyCode.DOWN)) movable = this.downMove(direction);
        if (direction.getKey().equals(KeyCode.LEFT)) movable = this.leftMove(direction);
        if (movable) {
            int random_value = ((int) (new Random().nextDouble() * 10)) > 8 ? 4 : 2;
            this.addNewTile(String.valueOf(random_value), Duration.seconds(.2));
        }
        //else new GameOver(this);
        return movable;
    }

    private void isGameComplete(Tile to) {
        if (!isGameComplete) {
            if (to.is2048Score()) {
                isGameComplete = true;
                GameCompletePopup completed = new GameCompletePopup(this, to);
                completed.show();
            }
        }
    }

    private synchronized boolean rightMove(Direction direction) {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            boolean prevent_first_combine = false;
            for (int j = 2; j >= 0; j--) {
                Tile current_tile = tiles[i][j];
                int next = j + 1;
                Tile next_tile = tiles[i][next];
                if (!isThisEmptyTile(current_tile) && isThisEmptyTile(next_tile)) {
                    int where = next;
                    boolean no_transfer = false;
                    next++;
                    while (next != 4) {
                        if (isThisEmptyTile(tiles[i][next])) {
                            where++;
                        } else {
                            if (current_tile.canMerge(tiles[i][next])) {
                                tiles[i][next].merge(current_tile);
                                this.isGameComplete(tiles[i][next]);
                                tileBackgrounds[i][next].setGraphic(tiles[i][next]);
                                tiles[i][j] = null;
                                tileBackgrounds[i][j].setGraphic(tiles[i][j]);
                                moved = true;
                                no_transfer = true;
                                if (j == 0) {
                                    prevent_first_combine = true;
                                }
                            }
                            break;
                        }
                        next++;
                    }
                    if (!no_transfer) {
                        tiles[i][where] = current_tile;
                        tileBackgrounds[i][where].setGraphic(tiles[i][where]);
                        tileBackgrounds[i][j].setGraphic(null);
                        tiles[i][j] = null;
                        moved = true;
                    }
                } else if (!isThisEmptyTile(current_tile) && !isThisEmptyTile(next_tile)) {
                    if (current_tile.canMerge(tiles[i][next])) {
                        tiles[i][next].merge(current_tile);
                        this.isGameComplete(tiles[i][next]);
                        tileBackgrounds[i][next].setGraphic(tiles[i][next]);
                        tiles[i][j] = null;
                        tileBackgrounds[i][j].setGraphic(tiles[i][j]);
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private synchronized boolean leftMove(Direction direction) {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            boolean prevent_first_combine = false;
            for (int j = 1; j < 4; j++) {
                Tile current_tile = tiles[i][j];
                int previous = j - 1;
                Tile previous_tile = tiles[i][previous];
                if (!isThisEmptyTile(current_tile) && isThisEmptyTile(previous_tile)) {
                    int where = previous;
                    boolean no_transfer = false;
                    previous--;
                    while (previous != -1) {
                        if (isThisEmptyTile(tiles[i][previous])) {
                            where--;
                        } else {
                            if (current_tile.canMerge(tiles[i][previous])) {
                                final double multiplier = j - previous;
                                tiles[i][previous].merge(current_tile);
                                this.isGameComplete(tiles[i][previous]);
                                tileBackgrounds[i][previous].setGraphic(tiles[i][previous]);
                                tiles[i][j] = null;
                                tileBackgrounds[i][j].setGraphic(null);
                                moved = true;
                                no_transfer = true;
                                if (j == 3) {
                                    prevent_first_combine = true;
                                }
                            }
                            break;
                        }
                        previous--;
                    }
                    if (!no_transfer) {
                        tiles[i][where] = current_tile;
                        tileBackgrounds[i][where].setGraphic(tiles[i][where]);
                        tileBackgrounds[i][j].setGraphic(null);
                        tiles[i][j] = null;
                        moved = true;
                    }
                } else if (!isThisEmptyTile(current_tile) && !isThisEmptyTile(previous_tile)) {
                    if (current_tile.canMerge(tiles[i][previous])) {
                        tiles[i][previous].merge(current_tile);
                        this.isGameComplete(tiles[i][previous]);
                        tileBackgrounds[i][previous].setGraphic(tiles[i][previous]);
                        tiles[i][j] = null;
                        tileBackgrounds[i][j].setGraphic(tiles[i][j]);
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private synchronized boolean upMove(Direction direction) {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            boolean prevent_first_combine = false;
            for (int j = 1; j < 4; j++) {
                Tile current_tile = tiles[j][i];
                int previous = j - 1;
                Tile previous_tile = tiles[previous][i];
                if (!isThisEmptyTile(current_tile) && isThisEmptyTile(previous_tile)) {
                    int where = previous;
                    boolean no_transfer = false;
                    previous--;
                    while (previous != -1) {
                        if (isThisEmptyTile(tiles[previous][i])) {
                            where--;
                        } else {
                            if (current_tile.canMerge(tiles[previous][i])) {
                                tiles[previous][i].merge(current_tile);
                                this.isGameComplete(tiles[previous][i]);
                                tileBackgrounds[previous][i].setGraphic(tiles[previous][i]);
                                tiles[j][i] = null;
                                tileBackgrounds[j][i].setGraphic(tiles[j][i]);
                                no_transfer = true;
                                moved = true;
                                if (j == 3) {
                                    prevent_first_combine = true;
                                }
                            }
                            break;
                        }
                        previous--;
                    }
                    if (!no_transfer) {
                        tiles[where][i] = current_tile;
                        tileBackgrounds[where][i].setGraphic(tiles[where][i]);
                        tileBackgrounds[j][i].setGraphic(null);
                        tiles[j][i] = null;
                        moved = true;
                    }
                } else if (!isThisEmptyTile(current_tile) && !isThisEmptyTile(previous_tile)) {
                    if (!(prevent_first_combine && j == 2)) {
                        if (current_tile.canMerge(tiles[previous][i])) {
                            tiles[previous][i].merge(current_tile);
                            this.isGameComplete(tiles[previous][i]);
                            tileBackgrounds[previous][i].setGraphic(tiles[previous][i]);
                            tiles[j][i] = null;
                            tileBackgrounds[j][i].setGraphic(tiles[j][i]);
                            moved = true;
                        }
                    }
                }
            }
        }
        return moved;
    }

    private synchronized boolean downMove(Direction direction) {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            boolean prevent_first_combine = false;
            for (int j = 2; j >= 0; j--) {
                Tile current_tile = tiles[j][i];
                int next = j + 1;
                Tile next_tile = tiles[next][i];
                if (!isThisEmptyTile(current_tile) && isThisEmptyTile(next_tile)) {
                    int where = next;
                    boolean no_transfer = false;
                    next++;
                    while (next != 4) {
                        if (isThisEmptyTile(tiles[next][i])) {
                            where++;
                        } else {
                            if (current_tile.canMerge(tiles[next][i])) {
                                tiles[next][i].merge(current_tile);
                                this.isGameComplete(tiles[next][i]);
                                tileBackgrounds[next][i].setGraphic(tiles[next][i]);
                                tiles[j][i] = null;
                                tileBackgrounds[j][i].setGraphic(tiles[j][i]);
                                no_transfer = true;
                                moved = true;
                                if (j == 0) {
                                    prevent_first_combine = true;
                                }
                            }
                            break;
                        }
                        next++;
                    }
                    if (!no_transfer) {
                        tiles[where][i] = current_tile;
                        tileBackgrounds[where][i].setGraphic(tiles[where][i]);
                        tileBackgrounds[j][i].setGraphic(null);
                        tiles[j][i] = null;
                        moved = true;
                    }
                } else if (!isThisEmptyTile(current_tile) && !isThisEmptyTile(next_tile)) {
                    if (!(prevent_first_combine && j == 1)) {
                        if (current_tile.canMerge(tiles[next][i])) {
                            tiles[next][i].merge(current_tile);
                            this.isGameComplete(tiles[next][i]);
                            tileBackgrounds[next][i].setGraphic(tiles[next][i]);
                            tiles[j][i] = null;
                            tileBackgrounds[j][i].setGraphic(tiles[j][i]);
                            moved = true;
                        }
                    }
                }
            }
        }
        return moved;
    }

    private boolean isThisEmptyTile(Tile tile) {
        return tile == null;
    }

    private synchronized void animateMovinement(Direction direction, Tile from, Tile to) {
        TranslateTransition st = new TranslateTransition(Duration.seconds(.3), from);
        st.setFromX(0);
        st.setToX(direction.getX_location());
        st.play();
        st.setOnFinished(e -> {
            to.merge(from);
            this.isGameComplete(to);
            from.remove();
        });
    }

    private void keyPressedOnApp(KeyEvent event) {
        Direction direction = new Direction(event.getCode());
        switch (direction.getKey()) {
            case UP:
                this.keyPressedAnimation(this.UP);
                this.move(direction);
                break;
            case DOWN:
                this.keyPressedAnimation(this.DOWN);
                this.move(direction);
                break;
            case LEFT:
                this.keyPressedAnimation(this.LEFT);
                this.move(direction);
                break;
            case RIGHT:
                this.keyPressedAnimation(this.RIGHT);
                this.move(direction);
                break;
            default:
                break;
        }
    }

    private synchronized void freeGraphics() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tileBackgrounds[i][j].setGraphic(null);
            }
        }
    }

    private boolean isNewBest() {
        return this.convertToInt(SCORE.getText()) > this.convertToInt(BEST.getText());
    }

    private int convertToInt(String value) {
        return Integer.parseInt(value);
    }

    private void addNewTile(String value, Duration duration) {
        boolean exist = true;
        while (exist) {
            int random_row = (int) (new Random().nextDouble() * 4);
            int random_column = (int) (new Random().nextDouble() * 4);
            if (!isGraphicExist(tileBackgrounds[random_row][random_column])) {
                tiles[random_row][random_column] = new Tile();
                tiles[random_row][random_column].setText(value);
                tiles[random_row][random_column].setNewBackground();
                this.tileBackgrounds[random_row][random_column].setGraphic(tiles[random_row][random_column]);
                this.setNewTileAnimation((Label) this.tileBackgrounds[random_row][random_column].getGraphic(), duration);
                exist = false;
            }
        }
    }

    private boolean isGraphicExist(TileBackground background) {
        return !background.graphicProperty().isNull().get();
    }

    private void setNewTileAnimation(Label graphic, Duration duration) {
        FadeTransition fd = new FadeTransition(duration, graphic);
        fd.setFromValue(0);
        fd.setToValue(.001);
        fd.play();
        fd.setOnFinished(e -> {

            FadeTransition fd2 = new FadeTransition(Duration.seconds(.2), graphic);
            fd2.setFromValue(0.2);
            fd2.setToValue(1);
            fd2.play();

            ScaleTransition stt = new ScaleTransition(Duration.seconds(.2), graphic);
            stt.setFromX(.3);
            stt.setToX(1);
            stt.setFromY(.3);
            stt.setToY(1);
            stt.play();

            stt.setOnFinished(ee -> {
                int plus = convertToInt(graphic.getText());
                this.setNewScore(plus);
                setPlusScoreAnimation(String.valueOf(plus));
            });
        });
    }

    private void setNewScore(int plus) {
        this.SCORE.setText(getNewScore(plus));
        if (this.isNewBest()) {
            setNewBest();
        }
    }

    private void setPlusScoreAnimation(String plus) {
        if (plus.length() > 2) {
            double width = 25 * plus.length();
            PLUS_SCORE.setMinSize(width, 45);
        }
        PLUS_SCORE.setText("+" + plus);
        this.setScoreStyle();
        PLUS_SCORE.setTextFill(Color.WHITE);
        FadeTransition ft = new FadeTransition(Duration.seconds(.7), PLUS_SCORE);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(.7), PLUS_SCORE);
        tt.setFromX(55);
        tt.setFromY(-50);
        tt.setToY(50);
        tt.play();
    }

    public void setScoreStyle() {
        PLUS_SCORE.setStyle("-fx-font-size: 30px; -fx-background-color: darkgreen");
    }

    private String getNewScore(int addition) {
        return String.valueOf(convertToInt(SCORE.getText()) + addition);
    }

    private void setNewBest() {
        BEST.setText(SCORE.getText());
        ScoreProperties properties = new ScoreProperties();
        properties.setScore(new Integer(BEST.getText()));
    }

    @SuppressWarnings({"deprecation"})
    private ContextMenu getAboutMenu() {
        ContextMenu cm = new ContextMenu();
        cm.getItems().addAll(
                MenuItemBuilder.create().text("New Game").onAction(e -> restart()).build(),
                MenuItemBuilder.create().text("Help").onAction(e -> this.showHelpContent()).build(),
                MenuItemBuilder.create().text("About").onAction(e -> this.showAboutContent()).build(),
                MenuItemBuilder.create().text("Exit").onAction(e -> this.setExitAnimationToNode(this.EXITER.getScene().getRoot())).build()
        );
        return cm;
    }

    @SuppressWarnings("deprecation")
    private void showHelpContent() {
        try {
            Node content = FXMLLoader.load(getClass().getResource("/resources/fxml/help.fxml"));
            helpBox = new VBox();
            helpBox.getChildren().addAll(content,
                    HBoxBuilder.create().alignment(Pos.CENTER_LEFT)
                            .padding(new Insets(0, 3, 0, 0))
                            .children(
                                    VBoxBuilder.create().minWidth(720).alignment(Pos.CENTER_LEFT).padding(new Insets(0, 0, 0, 10))
                                            .children(
                                                    HyperlinkBuilder.create().text("open-source on GitHub [shekharrai/XpanderFX]")
                                                            .onAction(e -> this.browse("https://github.com/shekharrai/XpanderFX")).build(),
                                                    LabelBuilder.create().text("shekhar.rai.2053@gmail.com").build()
                                            )
                                            .build(),
                                    ButtonBuilder.create().text("OK")
                                            .minHeight(40)
                                            .minWidth(70)
                                            .style("-fx-base:black;"
                                                    + "-fx-border-radius: 7;"
                                                    + "-fx-background-radius: 7;")
                                            .onAction(e -> this.popupCloser(help, helpBox))
                                            .font(Font.font("System", FontWeight.MEDIUM, FontPosture.REGULAR, 20))
                                            .build()
                            ).style("-fx-background-color:white")
                            .build()
            );
            helpBox.setStyle("-fx-background-color: linear-gradient(lightgrey, white, lightgrey);"
                    + "-fx-border-color: white;"
                    + "-fx-border-width: 1;");
            help.getContent().add(helpBox);
        } catch (IOException ex) {
            Logger.getLogger(XpanderFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        help.show(this.ICON.getScene().getWindow(), this.getNodeMaxX() + 20, this.getNodeMaxY() + 40);
        this.popupOpener(helpBox);
    }

    @SuppressWarnings("deprecation")
    private void showAboutContent() {
        try {
            Node content = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/fxml/about.fxml")));
            aboutBox = new VBox();
            aboutBox.getChildren().addAll(content,
                    HBoxBuilder.create().alignment(Pos.CENTER_RIGHT)
                            .padding(new Insets(0, 3, 0, 0))
                            .children(
                                    ButtonBuilder.create().text("OK")
                                            .minHeight(40)
                                            .minWidth(70)
                                            .style("-fx-base:black;"
                                                    + "-fx-border-radius: 7;"
                                                    + "-fx-background-radius: 7;")
                                            .onAction(e -> this.popupCloser(about, aboutBox))
                                            .font(Font.font("System", FontWeight.MEDIUM, FontPosture.REGULAR, 20))
                                            .build()
                            )
                            .build()
            );
            aboutBox.setStyle("-fx-background-color: linear-gradient(black, lightgrey);"
                    + "-fx-background-radius: 7;"
                    + "-fx-border-radius: 7;");
            about.getContent().add(aboutBox);
        } catch (IOException ex) {
            Logger.getLogger(XpanderFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        about.show(this.ICON.getScene().getWindow(), this.getNodeMaxX() - 14, this.getNodeMaxY() + 12);
        this.popupOpener(aboutBox);
    }

    private void browse(String url) {
        app.getHostServices().showDocument(url);
    }

    public void iconHovered(MouseEvent event) {
        this.ICON.setStyle("-fx-base: white");
    }

    public void iconHoveredReleased(MouseEvent event) {
        this.ICON.setStyle("-fx-base: greenyellow");
    }

    public void exitButtonHandler(MouseEvent event) {
        this.setExitAnimationToNode(this.EXITER.getScene().getRoot());
    }

    public void exitHoveredHandler(MouseEvent event) {
        this.EXITER.setStyle("-fx-base: red");
    }

    public void exitHoveredReleased(MouseEvent event) {
        this.EXITER.setStyle("-fx-base: greenyellow");
    }

    public void minimize(MouseEvent event) {
        Stage stage = (Stage) this.ICON.getScene().getWindow();
        stage.setIconified(true);
    }

    public void minimizeHoveredHandler(MouseEvent event) {
        this.MINIMISER.setStyle("-fx-base: darkgreen");
    }

    public void minimizeHoveredReleased(MouseEvent event) {
        this.MINIMISER.setStyle("-fx-base: greenyellow");
    }

    private void setExitAnimationToNode(Node root) {
        if (about.isShowing()) {
            this.popupCloser(about, aboutBox);
        }
        if (help.isShowing()) {
            this.popupCloser(help, helpBox);
        }
        ScaleTransition st = new ScaleTransition(Duration.seconds(.4), root);
        st.setToX(0);
        st.setToY(0);
        st.play();

        FadeTransition fd = new FadeTransition(Duration.seconds(.3), root);
        fd.setToValue(.1);
        fd.play();

        st.setOnFinished(e -> Platform.exit());
    }

    private void popupOpener(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.3), node);
        st.setFromX(.2);
        st.setFromY(.2);
        st.setToX(1);
        st.setToY(1);
        st.play();

        FadeTransition fd = new FadeTransition(Duration.seconds(.2), node);
        fd.setFromValue(.2);
        fd.setToValue(1);
        fd.play();
    }

    private void popupCloser(Popup pp, Node node) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.3), node);
        st.setToX(0);
        st.setToY(0);
        st.play();

        FadeTransition fd = new FadeTransition(Duration.seconds(.3), node);
        fd.setToValue(.2);
        fd.play();
        st.setOnFinished(e -> pp.hide());
    }

    private ImageView getImage(String path, double hw) {
        ImageView image = new ImageView(new Image(path));
        image.setFitHeight(hw);
        image.setFitWidth(hw);
        return image;
    }

    public void buttonHovered(Button b) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
        st.setToX(1.6);
        st.setToY(1.6);
        st.play();
    }

    private void buttonHoveredOff(Button b) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    private void buttonClicked(Button b, KeyCode key) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
        st.setFromX(.8);
        st.setFromY(.8);
        st.setToX(1.6);
        st.setToY(1.6);
        st.play();

        FadeTransition ft = new FadeTransition(Duration.seconds(.2), b);
        ft.setFromValue(.2);
        ft.setToValue(1);
        ft.play();

        this.move(new Direction(key));

    }

    private void keyPressedAnimation(Button b) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
        st.setFromX(.8);
        st.setFromY(.8);
        st.setToX(1.6);
        st.setToY(1.6);
        st.play();
        st.setOnFinished(e -> {
            if (!b.isHover()) {
                ScaleTransition st2 = new ScaleTransition(Duration.seconds(.09), b);
                st2.setToX(1);
                st2.setToY(1);
                st2.play();
            }
        });

        FadeTransition ft = new FadeTransition(Duration.seconds(.2), b);
        ft.setFromValue(.2);
        ft.setToValue(1);
        ft.play();
    }

    private double getNodeMaxY() {
        return this.ICON.localToScreen(this.ICON.getBoundsInLocal()).getMaxY();
    }

    private double getNodeMaxX() {
        return this.ICON.localToScreen(this.ICON.getBoundsInLocal()).getMaxX();
    }

    public void setApplication(XpanderFX app) {
        this.app = app;
    }
}
