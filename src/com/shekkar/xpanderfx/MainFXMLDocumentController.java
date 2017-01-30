/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx;

import com.shekkar.xpanderfx.center.tile.Tile;
import com.shekkar.xpanderfx.center.tile.TileBackground;
import com.shekkar.xpanderfx.top.popup.Completion;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.HyperlinkBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Shekkar Raee
 */
public class MainFXMLDocumentController implements Initializable {
	
	private @FXML BorderPane MAIN_PANE;	
	public @FXML Button UP, DOWN, LEFT, RIGHT;
	private @FXML Button ICON, MINIMISER, EXITER;	
	private @FXML ContextMenu ABOUT;	
	private @FXML Label BEST, SCORE, PLUS_SCORE;	
	private @FXML TilePane GAME_BOARD;		
	
	private Popup about, help;	
	private VBox about_box, help_box;	
	public TileBackground TILE_BACKGROUND[][];	
	public Tile tiles[][];	
	private BlockingQueue<Direction> moving_queues;
	
	/**
	 * Initializes the controller class.
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		rightSideInit();		
		topSideInit();		
		centerSideInit();		
		controlInit();		
		start(Duration.seconds(1.34));
		moving_queues = new ArrayBlockingQueue<>(100);		
	}

	/**
	 * Initialize the right side of the Application.
	 */
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
	
	/**
	 * initialize the top side of the app
	 */
	private void topSideInit() {
		
		this.ICON.setGraphic(this.getImage("/resources/image/icon.png", 57));
		this.EXITER.setGraphic(this.getImage("/resources/image/exit.png", 57));
		this.MINIMISER.setGraphic(this.getImage("/resources/image/mini.png", 57));
		about = new Popup();
		help = new Popup();
		
	}
	
	/**
	 * initialize the center side of the app
	 */
	private void centerSideInit() {
		TILE_BACKGROUND = new TileBackground[4][4];
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				TILE_BACKGROUND[i][j] = new TileBackground();
				GAME_BOARD.getChildren().add(TILE_BACKGROUND[i][j]);
			}			
		}
		tiles = new Tile[4][4];		
	}
	
	/**
	 * initialize the controls of the game-board.
	 */
	private void controlInit() {
		MAIN_PANE.setOnKeyReleased(e -> this.keyPressedOnApp(e));
	}
	
	/**
	 * starts new game.
	 * @param duration
	 */
	public synchronized void start(Duration duration) {
		ScoreProperties properties = new ScoreProperties();
		this.BEST.setText(String.valueOf(properties.getBest()));
		int values[] = {2,4};
		for(int i = 0; i<values.length; i++) {
			this.addNewTile(String.valueOf(values[i]), duration);
		}
	}
	/**
	 * equivalent to start() method, but it checks whether the current score is more than best or not.
	 */
	public synchronized void restart() {
		tiles = new Tile[4][4];
		this.freeGraphics();
		SCORE.setText("0");
		start(Duration.seconds(.001));
	}
	
	/**
	 * Moves tiles within the game-board.
	 * @param direction
	 * @return cannot_move
	 */
	public synchronized boolean move(Direction direction) {
		boolean movable = true;
		if(direction.getKey().equals(KeyCode.UP)) movable = this.upMove(direction);
		if(direction.getKey().equals(KeyCode.RIGHT)) movable = this.rightMove(direction);
		if(direction.getKey().equals(KeyCode.DOWN)) movable = this.downMove(direction);
		if(direction.getKey().equals(KeyCode.LEFT)) movable = this.leftMove(direction);
		if(movable) {	
			int random_value = ((int)(new Random().nextDouble() * 10)) > 8 ? 4 : 2;
			this.addNewTile(String.valueOf(random_value), Duration.seconds(.2));
		}
		//else new GameOver(this);
		return movable;
	}
	
	/**
	 * 
	 * @param to 
	 */
	private void checkCompletion(Tile to) {		
		if(!is_game_complete) {
			if(to.is2048Score()){
				is_game_complete = true;
				Completion completed = new Completion(this, to);
				completed.show();
			}
		}
	}
	
	/**
	 * right move of tiles
	 * @param direction 
	 */
	
	private synchronized boolean rightMove(Direction direction) {
		boolean moved = false;
		for(int i = 0; i < 4; i++) {
			boolean prevent_first_combine = false;
			for(int j = 2; j >= 0; j--) {
				Tile current_tile = tiles[i][j];
				int next = j + 1;
				Tile next_tile = tiles[i][next];
				if(!isThisEmptyTile(current_tile) && isThisEmptyTile(next_tile)) {
					int where = next;
					boolean no_transfer = false;
					next++;
					while(next != 4) {
						if(isThisEmptyTile(tiles[i][next])) {
							where++;
						}
						else{
							if(current_tile.isCombinable(tiles[i][next])) {
								tiles[i][next].combine(current_tile);
								this.checkCompletion(tiles[i][next]);
								TILE_BACKGROUND[i][next].setGraphic(tiles[i][next]);
								tiles[i][j] = null;
								TILE_BACKGROUND[i][j].setGraphic(tiles[i][j]);
								moved = true;
								no_transfer = true;
								if(j == 0) {
									prevent_first_combine = true;
								}
							}
							break;
						}
						next++;
					}
					if(!no_transfer) {
						tiles[i][where] = current_tile;
						TILE_BACKGROUND[i][where].setGraphic(tiles[i][where]);
						TILE_BACKGROUND[i][j].setGraphic(null);
						tiles[i][j] = null;
						moved = true;
					}
				}
				else if(!isThisEmptyTile(current_tile) && !isThisEmptyTile(next_tile)) {
					if(!(prevent_first_combine && j==1)) {
						if(current_tile.isCombinable(tiles[i][next])) {
							tiles[i][next].combine(current_tile);
							this.checkCompletion(tiles[i][next]);
							TILE_BACKGROUND[i][next].setGraphic(tiles[i][next]);
							tiles[i][j] = null;
							TILE_BACKGROUND[i][j].setGraphic(tiles[i][j]);
							moved = true;
						}
					}
				}
			}
		}
		return moved;		
	}
	/**
	 * 
	 * @param direction 
	 */
	private synchronized boolean leftMove(Direction direction) {
		boolean moved = false;
		for(int i = 0; i < 4; i++) {
			boolean prevent_first_combine = false;
			for(int j = 1; j < 4; j++) {
				Tile current_tile = tiles[i][j];
				int previous = j - 1;
				Tile previous_tile = tiles[i][previous];
				if(!isThisEmptyTile(current_tile) && isThisEmptyTile(previous_tile)) {
					int where = previous;
					boolean no_transfer = false;
					previous--;
					while(previous != -1) {
						if(isThisEmptyTile(tiles[i][previous])) {
							where--;
						}
						else{
							if(current_tile.isCombinable(tiles[i][previous])) {
								final double multiplier = j - previous;
								tiles[i][previous].combine(current_tile);
								this.checkCompletion(tiles[i][previous]);
								TILE_BACKGROUND[i][previous].setGraphic(tiles[i][previous]);
								tiles[i][j] = null;
								TILE_BACKGROUND[i][j].setGraphic(null);
								moved = true;
								no_transfer = true;
								if(j == 3) {
									prevent_first_combine = true;
								}
							}
							break;
						}
						previous--;
					}
					if(!no_transfer) {
						tiles[i][where] = current_tile;
						TILE_BACKGROUND[i][where].setGraphic(tiles[i][where]);
						TILE_BACKGROUND[i][j].setGraphic(null);
						tiles[i][j] = null;
						moved = true;
					}
				}
				else if(!isThisEmptyTile(current_tile) && !isThisEmptyTile(previous_tile)) {
					if(!(prevent_first_combine && j == 2)) {
						if(current_tile.isCombinable(tiles[i][previous])) {
							tiles[i][previous].combine(current_tile);
							this.checkCompletion(tiles[i][previous]);
							TILE_BACKGROUND[i][previous].setGraphic(tiles[i][previous]);
							tiles[i][j] = null;
							TILE_BACKGROUND[i][j].setGraphic(tiles[i][j]);
							moved = true;
						}
					}
				}
			}
		}	
		return moved;
	}
	/**
	 * 
	 * @param direction 
	 */
	private synchronized boolean upMove(Direction direction) {
		boolean moved = false;
		for(int i = 0; i<4; i++) {
			boolean prevent_first_combine = false;
			for(int j = 1; j < 4; j++) {
				Tile current_tile = tiles[j][i];
				int previous = j - 1;
				Tile previous_tile = tiles[previous][i];
				if(!isThisEmptyTile(current_tile) && isThisEmptyTile(previous_tile)) {
					int where = previous;
					boolean no_transfer = false;
					previous--;
					while(previous != -1) {
						if(isThisEmptyTile(tiles[previous][i])) {
							where--;
						}
						else{
							if(current_tile.isCombinable(tiles[previous][i])) {
								tiles[previous][i].combine(current_tile);
								this.checkCompletion(tiles[previous][i]);
								TILE_BACKGROUND[previous][i].setGraphic(tiles[previous][i]);
								tiles[j][i] = null;
								TILE_BACKGROUND[j][i].setGraphic(tiles[j][i]);
								no_transfer = true;
								moved = true;
								if(j == 3) {
									prevent_first_combine = true;
								}
							}
							break;
						}
						previous--;
					}
					if(!no_transfer) {
						tiles[where][i] = current_tile;
						TILE_BACKGROUND[where][i].setGraphic(tiles[where][i]);
						TILE_BACKGROUND[j][i].setGraphic(null);
						tiles[j][i] = null;
						moved = true;
					}
				}
				else if(!isThisEmptyTile(current_tile) && !isThisEmptyTile(previous_tile)) {
					if(!(prevent_first_combine && j == 2)) {
						if(current_tile.isCombinable(tiles[previous][i])) {
							tiles[previous][i].combine(current_tile);
								this.checkCompletion(tiles[previous][i]);
							TILE_BACKGROUND[previous][i].setGraphic(tiles[previous][i]);
							tiles[j][i] = null;
							TILE_BACKGROUND[j][i].setGraphic(tiles[j][i]);
							moved = true;
						}
					}
				}
			}
		}
		return moved;
	}
	/**
	 * 
	 * @param direction 
	 */
	private synchronized boolean downMove(Direction direction) {
		boolean moved = false;
		for(int i = 0; i<4; i++) {
			boolean prevent_first_combine = false;
			for(int j = 2; j >= 0; j--) {
				Tile current_tile = tiles[j][i];
				int next = j + 1;
				Tile next_tile = tiles[next][i];
				if(!isThisEmptyTile(current_tile) && isThisEmptyTile(next_tile)) {
					int where = next;
					boolean no_transfer = false;
					next++;
					while(next != 4) {
						if(isThisEmptyTile(tiles[next][i])) {
							where++;
						}
						else{
							if(current_tile.isCombinable(tiles[next][i])) {
								tiles[next][i].combine(current_tile);
								this.checkCompletion(tiles[next][i]);
								TILE_BACKGROUND[next][i].setGraphic(tiles[next][i]);
								tiles[j][i] = null;
								TILE_BACKGROUND[j][i].setGraphic(tiles[j][i]);
								no_transfer = true;
								moved = true;
								if(j == 0) {
									prevent_first_combine = true;
								}
							}
							break;
						}
						next++;
					}
					if(!no_transfer) {
						tiles[where][i] = current_tile;
						TILE_BACKGROUND[where][i].setGraphic(tiles[where][i]);
						TILE_BACKGROUND[j][i].setGraphic(null);
						tiles[j][i] = null;
						moved = true;
					}
				}
				else if(!isThisEmptyTile(current_tile) && !isThisEmptyTile(next_tile)) {
					if(!(prevent_first_combine && j == 1)) {
						if(current_tile.isCombinable(tiles[next][i])) {
							tiles[next][i].combine(current_tile);
							this.checkCompletion(tiles[next][i]);
							TILE_BACKGROUND[next][i].setGraphic(tiles[next][i]);
							tiles[j][i] = null;
							TILE_BACKGROUND[j][i].setGraphic(tiles[j][i]);
							moved = true;
						}
					}
				}
			}
		}
		return moved;
	}
	
	/**
	 * 
	 * @param tile
	 * @return 
	 */
	private boolean isThisEmptyTile(Tile tile) {
		return tile == null;
	}
	
	private boolean is_game_complete = false;
	
	/**
	 * 
	 * @param direction
	 * @param from
	 * @param to 
	 */
	private synchronized void animateMovinement(Direction direction, Tile from, Tile to) {
		TranslateTransition st = new TranslateTransition(Duration.seconds(.3), from);
		st.setFromX(0);
		st.setToX(direction.getX_location());
		st.play();
		st.setOnFinished(e -> {
			to.combine(from);
			this.checkCompletion(to);
			from.remove();
		});
	}
	
	/**
	 * 
	 * @param event 
	 */
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
			default:	break;
		}
	}
	
	/**
	 * Sets null graphic to all tile-backgrounds.
	 */
	private synchronized void freeGraphics() {
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				TILE_BACKGROUND[i][j].setGraphic(null);
			}
		}
	}
	
	/**
	 * 
	 * @return is_new_best
	 */
	private boolean isNewBest() {
		return this.convertToInt(SCORE.getText()) > this.convertToInt(BEST.getText());
	}
	
	/**
	 * Converts string value to int value.
	 * @param value
	 * @return int value
	 */
	private int convertToInt(String value) {
		return Integer.parseInt(value);
	}
	/**
	 * Adds new tile in a random location.
	 * @param value
	 * @param duration 
	 */
	public void addNewTile(String value, Duration duration) {	
		boolean exist = true;
		while(exist) {
			int random_row = (int) (new Random().nextDouble() * 4);
			int random_column = (int) (new Random().nextDouble() * 4);
			if(!isGraphicExist(TILE_BACKGROUND[random_row][random_column])) {
				tiles[random_row][random_column] = new Tile();
				tiles[random_row][random_column].setText(value);
				tiles[random_row][random_column].setNewBackground();
				this.TILE_BACKGROUND[random_row][random_column].setGraphic(tiles[random_row][random_column]);
				this.setNewTileAnimation((Label) this.TILE_BACKGROUND[random_row][random_column].getGraphic(), duration);
				exist = false;
			}
		}
	}
	
	/**
	 * Checks whether a graphic is attached within the provided tile-background or not.
	 * @param background
	 * @return is_graphic_exist
	 */
	private boolean isGraphicExist(TileBackground background) {
		return !background.graphicProperty().isNull().get();
	}
	
	/**
	 * Animates for the provided new tile(graphic).
	 * @param graphic
	 * @param duration 
	 */
	private void setNewTileAnimation(Label graphic, Duration duration) {
		FadeTransition fd = new FadeTransition(duration, graphic);
		fd.setFromValue(0);
		fd.setToValue(.001);
		fd.play();
		fd.setOnFinished(e-> {
			
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
	
	/**
	 * Sets new score and also checks for if the score is more than current best score.
	 * @param plus 
	 */
	private void setNewScore(int plus) {
		this.SCORE.setText(getNewScore(plus));
		if(this.isNewBest()) {
			setNewBest();
		}
	}
	
	/**
	 * Animates a label with the plus(new additional value) text.
	 * @param plus 
	 */
	private void setPlusScoreAnimation(String plus) {
		if(plus.length() > 2 ) {
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
	
	/**
	 * styles to the label(PLUS_SCORE).
	 */
	public void setScoreStyle() {
		PLUS_SCORE.setStyle("-fx-font-size: 30px; -fx-background-color: darkgreen");
	}
	
	/**
	 * 
	 * @param addition
	 * @return new score value
	 */
	private String getNewScore(int addition) {
		return String.valueOf(convertToInt(SCORE.getText()) + addition);
	}
	
	/**
	 * Sets new best score
	 */
	private void setNewBest() {
		BEST.setText(SCORE.getText());		
		ScoreProperties properties = new ScoreProperties();
		properties.setScore(new Integer(BEST.getText()));
	}
	
	/**
	 * 
	 * @return ABOUT
	 */
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
	
	/**
	 * Help content of XpanderFX
	 */
	@SuppressWarnings("deprecation")
	private void showHelpContent() {
		try {
			Node content = FXMLLoader.load(getClass().getResource("/com/shekkar/xpanderfx/top/popup/HelpFXML.fxml"));
			help_box = new VBox();
			help_box.getChildren().addAll(content,
					HBoxBuilder.create().alignment(Pos.CENTER_LEFT)
					.padding(new Insets(0,3,0,0))
					.children(
							VBoxBuilder.create().minWidth(720).alignment(Pos.CENTER_LEFT).padding(new Insets(0,0,0,10))
									.children(
											HyperlinkBuilder.create().text("open-source on GitHub [ShekkarRaee/XpanderFX]")
													.onAction(e -> this.browse("https://github.com/ShekkarRaee/XpanderFX-2048-Game-JavaFX")).build(),
											LabelBuilder.create().text("shekkar.raee@hotmail.com").build()
									)
							.build(),
							ButtonBuilder.create().text("OK")
							.minHeight(40)
							.minWidth(70)
							.style("-fx-base:black;"
							+ "-fx-border-radius: 7;"
							+ "-fx-background-radius: 7;")
							.onAction(e -> this.popupCloser(help, help_box))
							.font(Font.font("System", FontWeight.MEDIUM, FontPosture.REGULAR, 20))
							.build()
					).style("-fx-background-color:white")
					.build()
				 );
			help_box.setStyle("-fx-background-color: linear-gradient(lightgrey, white, lightgrey);"
				 + "-fx-border-color: white;"
				 + "-fx-border-width: 1;");
			help.getContent().add(help_box);
		} catch (IOException ex) {
			Logger.getLogger(MainFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		help.show(this.ICON.getScene().getWindow(), this.getNodeMaxX() + 20, this.getNodeMaxY() + 40);
		this.popupOpener(help_box);				
	}
	
	/**
	 * About content
	 */
	@SuppressWarnings("deprecation")
	private void showAboutContent() {
		try {
			Node content = FXMLLoader.load(getClass().getResource("/com/shekkar/xpanderfx/top/popup/AboutFXMLDocument.fxml"));
			about_box = new VBox();
			about_box.getChildren().addAll(content,
					HBoxBuilder.create().alignment(Pos.CENTER_RIGHT)
					.padding(new Insets(0,3,0,0))
					.children(
					      ButtonBuilder.create().text("OK")
					      .minHeight(40)
					      .minWidth(70)
					      .style("-fx-base:black;"
						+ "-fx-border-radius: 7;"
						+ "-fx-background-radius: 7;")
					      .onAction(e -> this.popupCloser(about, about_box))
					      .font(Font.font("System", FontWeight.MEDIUM, FontPosture.REGULAR, 20))
					      .build()
					)
					.build()
				 );
			about_box.setStyle("-fx-background-color: linear-gradient(black, lightgrey);"
				 + "-fx-background-radius: 7;"
				 + "-fx-border-radius: 7;");
			about.getContent().add(about_box);
		} catch (IOException ex) {
			Logger.getLogger(MainFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		about.show(this.ICON.getScene().getWindow(), this.getNodeMaxX() - 14, this.getNodeMaxY() + 12);
		this.popupOpener(about_box);		
	}
	
	/**
	 * opens browser with provided url
	 */
	private void browse(String url) {	
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException ex) {
			Logger.getLogger(MainFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	/**
	 * 
	 * @param event 
	 */
	public void iconHovered(MouseEvent event) {
		this.ICON.setStyle("-fx-base: white");
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void iconHoveredReleased(MouseEvent event) {
		this.ICON.setStyle("-fx-base: greenyellow");		
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void exitButtonHandler(MouseEvent event) {
		this.setExitAnimationToNode(this.EXITER.getScene().getRoot());
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void exitHoveredHandler(MouseEvent event) {
		this.EXITER.setStyle("-fx-base: red");
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void exitHoveredReleased(MouseEvent event) {
		this.EXITER.setStyle("-fx-base: greenyellow");
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void minimize(MouseEvent event) {
			Stage stage  = (Stage)this.ICON.getScene().getWindow();
		stage.setIconified(true);
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void minimizeHoveredHandler(MouseEvent event) {
		this.MINIMISER.setStyle("-fx-base: darkgreen");
	}
	
	/**
	 * 
	 * @param event 
	 */
	public void minimizeHoveredReleased(MouseEvent event) {
		this.MINIMISER.setStyle("-fx-base: greenyellow");
	}
		
	/**
	 * 
	 * @param root 
	 */
	private void setExitAnimationToNode(Node root) {
		if(about.isShowing()) {
			this.popupCloser(about, about_box);
		}
		if(help.isShowing()) {
			this.popupCloser(help, help_box);
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
	
	
	/**
	 * 
	 * @param pp
	 * @param node 
	 */
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
	
	/**
	 * 
	 * @param pp 
	 */
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
	
	/**
	 * 
	 * @param path
	 * @return image
	 */
	private ImageView getImage(String path, double hw) {
		ImageView image = new ImageView(new Image(path));
		image.setFitHeight(hw);
		image.setFitWidth(hw);
		return image;
	}
	
	/**
	 * 
	 * @param b 
	 */
	public void buttonHovered(Button b) {
		ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
		st.setToX(1.6);
		st.setToY(1.6);
		st.play();		
	}
	
	/**
	 * 
	 * @param b 
	 */
	public void buttonHoveredOff(Button b) {
		ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
		st.setToX(1);
		st.setToY(1);
		st.play();				
	}
	
	/**
	 * 
	 * @param b 
	 */
	public void buttonClicked(Button b, KeyCode key) {
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
		
		boolean movable = true;
		Direction direction = new Direction(key);
		if(direction.getKey().equals(KeyCode.UP)) movable = this.upMove(direction);
		if(direction.getKey().equals(KeyCode.RIGHT)) movable = this.rightMove(direction);
		if(direction.getKey().equals(KeyCode.DOWN)) movable = this.downMove(direction);
		if(direction.getKey().equals(KeyCode.LEFT)) movable = this.leftMove(direction);
		if(movable) {	
			int random_value = ((int)(new Random().nextDouble() * 10)) > 8 ? 4 : 2;
			this.addNewTile(String.valueOf(random_value), Duration.seconds(.2));
		}
			
	}
	
	/**
	 * Animation for key pressed.
	 * @param b 
	 */
	private void keyPressedAnimation(Button b) {
		ScaleTransition st = new ScaleTransition(Duration.seconds(.2), b);
		st.setFromX(.8);
		st.setFromY(.8);
		st.setToX(1.6);
		st.setToY(1.6);
		st.play();
		st.setOnFinished(e -> {
			if(!b.isHover()) {
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
	
	/**
	 * 
	 * @param event 
	 */
	public void iconClicked(MouseEvent event) {
		ABOUT= this.getAboutMenu();
		this.ABOUT.show(this.ICON.getScene().getWindow(), this.getNodeMaxX() - 60, this.getNodeMaxY() - 5);	
		
	}
	
	/**
	 * 
	 * @return node's max_y
	 */
	private double getNodeMaxY() {
		return this.ICON.localToScreen(this.ICON.getBoundsInLocal()).getMaxY();
	}
	
	/**
	 * 
	 * @return node's max_x
	 */
	private double getNodeMaxX() {
		return this.ICON.localToScreen(this.ICON.getBoundsInLocal()).getMaxX();
	}
}
