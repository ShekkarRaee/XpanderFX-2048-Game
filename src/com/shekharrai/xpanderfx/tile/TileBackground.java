package com.shekharrai.xpanderfx.tile;

import com.shekharrai.xpanderfx.control.Direction;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import static java.util.Objects.nonNull;


public class TileBackground extends Label {

    public TileBackground() {
        super.setMinSize(Constants.Size.WIDTH, Constants.Size.HEIGHT);
        super.setMaxSize(Constants.Size.WIDTH, Constants.Size.HEIGHT);
        super.setTextAlignment(TextAlignment.CENTER);
        super.setStyle(Constants.Style.BACKGROUND_RADIUS + Constants.Style.BACKGROUND);
    }

    public TileBackground getNextTile(TileBackground tiles[][], Direction direction, int row, int column) {
        switch (direction.getKey()) {
            case UP:
                return tiles[row - 1][column];
            case DOWN:
                return tiles[row + 1][column];
            case RIGHT:
                return tiles[row][column + 1];
            case LEFT:
                return tiles[row][column - 1];
            default:
                return null;
        }
    }

    public boolean isTileExist() {
        return nonNull(getGraphic());
    }
}
