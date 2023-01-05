package checkers.client;

import checkers.Piece;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private ClientPiece piece;

    public boolean hasPiece() {
        return piece != null;
    }

    public ClientPiece getPiece() {
        return piece;
    }

    public void setPiece(ClientPiece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y, int tileSize) {
        setWidth(tileSize);
        setHeight(tileSize);

        relocate(x * tileSize, y * tileSize);

        setFill(light ? Color.GREENYELLOW : Color.GREEN);
    }

    void setColor(Color color) {
        setFill(color);
    }
}
