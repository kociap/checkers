package checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static checkers.Checkers.tileSize;

public class Piece extends StackPane {

    private PieceType type;
    private double mouseX, mouseY;
    private double oldX, oldY;
    public PieceType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }
    public Piece(PieceType type, int x, int y) {
        this.type = type;

        move(x, y);

        // adds black background
        Ellipse background = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        background.setFill(Color.BLACK);

        background.setStroke(Color.BLACK);
        background.setStrokeWidth(tileSize * 0.03);

        background.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
        background.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2 + tileSize * 0.07);

        // gives pieces a color
        Ellipse ellipse = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        ellipse.setFill(type == PieceType.RED ? Color.RED : Color.WHITE);

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(tileSize * 0.03);

        ellipse.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
        ellipse.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2);

        getChildren().addAll(background, ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y) {
        oldX = x * tileSize;
        oldY = y * tileSize;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }
}
