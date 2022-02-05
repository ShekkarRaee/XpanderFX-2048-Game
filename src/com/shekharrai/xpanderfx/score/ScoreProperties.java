package com.shekharrai.xpanderfx.score;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;

public class ScoreProperties {

    public static final String HIGH_SCORE_PROPERTY = "best";
    private final Properties scoreProperties;
    private final static String SCORE_PROPERTY_FILE = "src/resources/score/XpanderFX.properties";

    public ScoreProperties() {
        this.scoreProperties = new Properties();
    }

    public void setScore(int currentScore) {
        try {
            int old_best = getBest();
            this.scoreProperties.setProperty(HIGH_SCORE_PROPERTY, String.valueOf((Math.max(currentScore, old_best))));
            this.scoreProperties.store(new FileWriter(SCORE_PROPERTY_FILE), SCORE_PROPERTY_FILE);
        } catch (IOException ex) {
            Logger.getLogger(ScoreProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getBest() throws IOException {
        Reader reader = null;
        try {
            reader = new FileReader(SCORE_PROPERTY_FILE);
            this.scoreProperties.load(reader);
        } finally {
            if (nonNull(reader)) {
                reader.close();
            }
        }
        String best = this.scoreProperties.getProperty(HIGH_SCORE_PROPERTY);
        if (best != null) {
            return parseInt(best);
        }
        return 0;
    }
}	

