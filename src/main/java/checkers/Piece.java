package checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static checkers.CheckersApp.tileSize;

public class Piece extends StackPane {
    private PieceType type;
    public PieceType getType() {
        return type;
    }
    public Piece(PieceType type, int x, int y) {
        this.type = type;

        Ellipse background = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        background.setFill(type == PieceType.RED ? Color.RED : Color.WHITE);

        background.setStroke(Color.BLACK);
    }
}
