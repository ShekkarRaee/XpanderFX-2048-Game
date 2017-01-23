/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx.center.tile;

import com.shekkar.xpanderfx.Direction;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Shekkar Raee
 */
public class TileBackground extends Label{
	
	public TileBackground() {
		super.setMinSize(140.6, 136.8);
		super.setMaxSize(140.6, 136.8);
		super.setTextAlignment(TextAlignment.CENTER);
		super.setStyle("-fx-base: grey; " +
			"-fx-background-color:linear-gradient(darkslategrey, transparent,darkslategrey);"+
			"-fx-background-radius: 8;" +
			"-fx-border-radius: 8;"
		);
	}
	public TileBackground getNextTile(TileBackground tiles[][], Direction direction, int row, int column) {
		TileBackground next = null;
		switch (direction.getKey()) {
			case UP:
				next = tiles[row-1][column];
				break;
			case DOWN:
				next = tiles[row + 1][column];
				break;
			case RIGHT:
				next = tiles[row][column + 1];
				break;
			case LEFT:
				next = tiles[row][column - 1];
				break;
			default:
				break;
		}
		return next;
	}
	
	
	/**
	 * 
	 * @return graphic_exist
	 */
	public boolean isTileExist() {
		boolean exist = true;
		if(getGraphic() == null) {
			exist = false;
		}
		return exist;
	}
}
