package com.shekharrai.xpanderfx.tile;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import static java.lang.Integer.parseInt;

public class Tile extends Label {

    public Tile() {
        super.setMinSize(Constants.Size.WIDTH, Constants.Size.HEIGHT);
        super.setMaxSize(Constants.Size.WIDTH, Constants.Size.HEIGHT);
        super.setTextAlignment(TextAlignment.CENTER);
        super.setAlignment(Pos.CENTER);
    }

    public void merge(Tile adder) {
        setMergedText(adder);
        setNewBackground();
    }

    public void remove() {
        this.getParent().getChildrenUnmodifiable().remove(this);
    }

    public boolean canMerge(Tile adder) {
        return getText().equals(adder.getText());
    }

    public boolean is2048Score() {
        return parseInt(this.getText()) == 2048;
    }

    public void setNewBackground() {
        switch (getTextInt()) {
            case 2:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_2);
                break;
            case 4:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_4);
                break;
            case 8:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_8);
                break;
            case 16:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_16);
                break;
            case 32:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_32);
                break;
            case 64:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_64);
                break;
            case 128:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_128);
                break;
            case 256:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_256);
                break;
            case 512:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_512);
                break;
            case 1024:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_1024);
                break;
            case 2048:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_2048);
                break;
            case 4096:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_4096);
                break;
            case 8192:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_8192);
                break;
            case 16384:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_163844);
                break;
            case 32768:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_32768);
                break;
            default:
                setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.TILE_DEFAULT);
                break;
        }
    }

    private void setMergedText(Tile adder) {
        final int sum = adder.getTextInt() + getTextInt();
        setText(String.valueOf(sum));
    }

    private int getTextInt() {
        return parseInt(getText());
    }

}
