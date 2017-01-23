/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shekkar.xpanderfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shekkar Raee
 */
public class ScoreProperties {
	private Properties SCORE_PROPERTIES;
	private String SCORE_FILE = "XpanderFX.properties";
	
	public ScoreProperties() {
		this.SCORE_PROPERTIES = new Properties();
	}
	
	/**
	 * stores new best score
	 * @param best_score 
	 */
	public void setScore(int best_score)  {
		int old_best = getBest();
		try {
			this.SCORE_PROPERTIES.setProperty("best", String.valueOf((Math.max(best_score, old_best))));
			this.SCORE_PROPERTIES.store(new FileWriter(SCORE_FILE), SCORE_FILE);
		} catch (IOException ex) {
			Logger.getLogger(ScoreProperties.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * 
	 * @return best_score
	 */
	public int getBest() {
		Reader reader = null;
		try {
			reader = new FileReader(this.SCORE_FILE);
			this.SCORE_PROPERTIES.load(reader);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ScoreProperties.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ScoreProperties.class.getName()).log(Level.SEVERE, null, ex);
		}
		String best = this.SCORE_PROPERTIES.getProperty("best");
		if(best != null) {
			return new Integer(best);
		}
		return 0;
	}
}	

