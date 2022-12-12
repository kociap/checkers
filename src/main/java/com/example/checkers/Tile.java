package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private Piece piece;

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(CheckersApp.tileSize);
        setHeight(CheckersApp.tileSize);

        relocate(x * CheckersApp.tileSize, y * CheckersApp.tileSize);

        setFill(light ? Color.GREENYELLOW : Color.GREEN);
    }
}
