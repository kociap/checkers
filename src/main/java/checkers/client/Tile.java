package checkers.client;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    public Tile(int x, int y, int tileSize) {
        setWidth(tileSize);
        setHeight(tileSize);

        relocate(x * tileSize, y * tileSize);
    }

    void setColor(Color color) {
        setFill(color);
    }
}
