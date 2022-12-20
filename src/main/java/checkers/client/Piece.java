package checkers.client;

import checkers.Point;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {
    private checkers.server.Piece piece;
    private double mouseX, mouseY;
    private double oldX, oldY;
    private int size;

    public checkers.server.Piece.Kind getKind() {
        return piece.getKind();
    }

    public checkers.server.Piece.Color getColor() {
        return piece.getColor();
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Piece(checkers.server.Piece piece, int size) {
        this.piece = piece;
        this.size = size;

        Point position = piece.getPosition();
        move(position.x, position.y);

        // adds black background
        Ellipse background = new Ellipse(size * 0.3125, size * 0.26);
        background.setFill(Color.BLACK);

        background.setStroke(Color.BLACK);
        background.setStrokeWidth(size * 0.03);

        background.setTranslateX((size - size * 0.3125 * 2) / 2);
        background.setTranslateY((size - size * 0.26 * 2) / 2 + size * 0.07);

        Ellipse ellipse = new Ellipse(size * 0.3125, size * 0.26);
        final checkers.server.Piece.Color color = piece.getColor();
        if(color == checkers.server.Piece.Color.red) {
            ellipse.setFill(Color.RED);
        } else if(color == checkers.server.Piece.Color.white) {
            ellipse.setFill(Color.WHITE);
        }
        // TODO: else assert

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(size * 0.03);

        ellipse.setTranslateX((size - size * 0.3125 * 2) / 2);
        ellipse.setTranslateY((size - size * 0.26 * 2) / 2);

        getChildren().addAll(background, ellipse);

        // setOnMousePressed(e -> {
        //     mouseX = e.getSceneX();
        //     mouseY = e.getSceneY();
        // });

        // setOnMouseDragged(e -> {
        //     relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        // });
    }

    public void move(int x, int y) {
        oldX = x * size;
        oldY = y * size;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        // relocate(oldX, oldY);
    }
}
