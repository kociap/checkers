package checkers;

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

    public Tile(boolean light, int x, int y, int tileSize) {
        setWidth(tileSize);
        setHeight(tileSize);

        relocate(x * tileSize, y * tileSize);

        setFill(light ? Color.GREENYELLOW : Color.GREEN);
    }
}
