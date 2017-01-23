/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx.center.tile;

import com.shekkar.xpanderfx.Direction;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Shekkar Raee
 */
public class Tile extends Label{
	
	public Tile() {
		super.setMinSize(140.6, 136.8);
		super.setMaxSize(140.6, 136.8);
		super.setTextAlignment(TextAlignment.CENTER);
		super.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Combines the provided adder tile to this tile
	 * @param adder 
	 */
	public void combine(Tile adder) {
		setNewText(adder);
		setNewBackground();
	}
	
	/**
	 * Checks whether the provided tile can be combined with this tile or not.
	 * @param adder
	 * @return combinable
	 */
	public boolean isCombinable(Tile adder) {
		return getText().equals(adder.getText());
	}
	
	/**
	 * Sets text to the tile after summing the value of the tile with the value of provided adder tile.
	 * @param adder
	 */
	public void setNewText(Tile adder) {
		int sum = convertToInt(adder.getText()) + convertToInt(getText());
		setText(String.valueOf(sum));
	}
	
	
	/**
	 * Converts string value to integer value
	 * @param value
	 * @return converted-int
	 */
	private int convertToInt(String value) {
		return Integer.parseInt(value);
	}
	
	/**
	 * sets new styles according to the text value of the tile
	 */
	public void setNewBackground() {
		int value = convertToInt(getText());
		switch(value) {
			case 2 :
				setStyle("-fx-font-size: 75px;" +
					" -fx-text-fill: #776e65;" +
					" -fx-background-color: #ede0c8;" +
					" -fx-background-radius: 8;" +
					" -fx-border-radius: 8;"
				);
				break;
			case 4 :
				setStyle("-fx-font-size: 75px;" +
					" -fx-text-fill: #776e65;" +
					" -fx-background-color: #f0f487; " +
					" -fx-background-radius: 8;" +
					" -fx-border-radius: 8;"
				);
				break;
			case 8 :
				setStyle("-fx-font-size: 75px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #f2b179;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 16 :
				setStyle("-fx-font-size: 65px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #f59564;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 32 :
				setStyle("-fx-font-size: 65px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #f67c5f;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 64 :
				setStyle("-fx-font-size: 65px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #f65e3b;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 128 :
				setStyle("-fx-font-size: 55px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #edcf72;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"+
					"-fx-effect: dropshadow( three-pass-box, rgba(243, 215, 116, 0.2381), 30, 0.5, 0, 0 );" +
					"    -fx-border-color: rgba(255, 255, 255, 0.14286);"+
					"    -fx-border-width: 1;"
				);
				break;
			case 256 :
				setStyle("-fx-font-size: 55px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #edcc61;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"+
					"-fx-effect: dropshadow( three-pass-box, rgba(243, 215, 116, 0.31746), 30, 0.5, 0, 0 );" +
					"    -fx-border-color: rgba(255, 255, 255, 0.19048);" +
					"    -fx-border-width: 1;"
				);
				break;
			case 512 :
				setStyle("-fx-font-size: 55px;" +
					"-fx-text-fill: #f9f6f2;" +
					"-fx-background-color: #edc850;" +
					"-fx-background-radius: 8;" +
					"-fx-border-radius: 8;"+
					"-fx-effect: dropshadow( three-pass-box, rgba(243, 215, 116, 0.39683), 30, 0.5, 0, 0 );" +
					"-fx-border-color: rgba(255, 255, 255, 0.2381);" +
					"-fx-border-width: 1;"
				);
				break;
			case 1024 :
				setStyle("-fx-font-size: 45px;" +
					"-fx-text-fill: #f9f6f2;" +
					"-fx-background-color: #edc53f;" +
					"-fx-background-radius: 8;" +
					"-fx-border-radius: 8;" +
					"-fx-effect: dropshadow( three-pass-box, rgba(243, 215, 116, 0.47619), 30, 0.5, 0, 0 );" +
					"-fx-border-color: rgba(255, 255, 255, 0.28571);\n" +
					"-fx-border-width: 1;"
				);
				break;
			case 2048 :
				setStyle("-fx-font-size: 45px;" +
					"-fx-text-fill: #f9f6f2;" +
					"-fx-background-color: #edc22e; " +
					"-fx-background-radius: 8;" +
					"-fx-border-radius: 8;"+
					"-fx-effect: dropshadow( three-pass-box, rgba(243, 215, 116, 0.55556), 30, 0.5, 0, 0 );" +
					"-fx-border-color: rgba(255, 255, 255, 0.33333);" +
					"-fx-border-width: 1;"
				);
				break;
			case 4096 :
				setStyle("-fx-font-size: 45px;" +
					"fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #b885ac;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 8192 :
				setStyle("-fx-font-size: 45px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #ab61a7;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 16384 :
				setStyle("-fx-font-size: 35px;" +
					"    -fx-text-fill: #776e65;" +
					"    -fx-background-color: #eee4da;" +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			case 32768 :
				setStyle("-fx-font-size: 35px;" +
					"-fx-text-fill: #f9f6f2;" +
					"    -fx-background-color: #a755a6;  " +
					"    -fx-background-radius: 8;" +
					"    -fx-border-radius: 8;"
				);
				break;
			default:
				setStyle("-fx-font-size: 30px;" +
					"-fx-text-fill: #f9f6f2;" +
					"-fx-background-color: #3c3a32;" +
					"-fx-background-radius: 8;" +
					"-fx-border-radius: 8;"
				);
				break;
				
		}
	}
	
	public void remove() {
		this.getParent().getChildrenUnmodifiable().remove(this);
	}
}
