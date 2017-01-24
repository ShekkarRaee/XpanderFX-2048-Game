/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx;

import javafx.scene.input.KeyCode;

/**
 *
 * @author Shekkar Raee
 */
public class Direction {
	private KeyCode key;
	
	private double x_location = 0, y_location = 0;
	private int x_tile = 0, y_tile = 0;
	private boolean positive = true, right_key = false;
	
	public Direction(KeyCode key) {
		this.key = key;
		setKeyCode();
		setLocation();
	}
	
	/**
	 * Sets key-code from the key event.
	 */
	private void setKeyCode() {
		if(key.equals(KeyCode.W) || key.equals(KeyCode.UP)) {
			key = KeyCode.UP;
			right_key = true;
		}
		else if(key.equals(KeyCode.S) || key.equals(KeyCode.DOWN)){
			key = KeyCode.DOWN;
			right_key = true;
		}
		else if(key.equals(KeyCode.A) || key.equals(KeyCode.LEFT)) {
			key = KeyCode.LEFT;
			right_key = true;
		}
		else if(key.equals(KeyCode.D) || key.equals(KeyCode.RIGHT)) {
			key = KeyCode.RIGHT;
			right_key = true;
		}
		else {
			key = KeyCode.PLUS;
		}
	}
	
	/**
	 * sets location according to the key code.
	 */
	private void setLocation() {
		switch (getKey()) {
			case UP:	
				this.y_location = -136.8 - 6;
				this.y_tile = -1;
				this.positive = false;
				break;
			case DOWN:	
				this.y_location = 136.8 + 6;
				this.y_tile = 1;
				break;
			case LEFT:	
				this.x_location = -140.6 - 6;
				this.x_tile = -1;
				this.positive = false;
				break;
			case RIGHT:	
				this.x_location = 140.6 + 6;
				this.x_tile = 1;
				break;
			default:	break;
		}
	}
	
	/**
	 * Returns the key-code.
	 * @return KeyCode
	 */
	public KeyCode getKey() {
		return this.key;
	}

	/**
	 * Returns x-location for movement.
	 * @return 
	 */
	public double getX_location() {
		return x_location;
	}

	/**
	 * Returns y-location for movement.
	 * @return 
	 */
	public double getY_location() {
		return y_location;
	}

	public int getX_tile() {
		return x_tile;
	}

	public int getY_tile() {
		return y_tile;
	}

	public boolean isPositive() {
		return positive;
	}

	public boolean isRight_key() {
		return right_key;
	}
}
