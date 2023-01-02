package checkers.client;

import checkers.Piece;
import checkers.utility.Point;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;

public class ClientPiece extends StackPane implements Piece {
    private double mouseOffsetX;
    private double mouseOffsetY;
    private Point lastPosition = new Point(0, 0);
    private int size;

    private int ID;
    private Piece.Kind kind;
    private Piece.Color color;
    private Point position = new Point(0, 0);

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public Piece.Kind getKind() {
        return kind;
    }

    @Override
    public Piece.Color getColor() {
        return color;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    // public double getOldX() {
    //     return lastPosition.x;
    // }

    // public double getOldY() {
    //     return lastPosition.y;
    // }

    public ClientPiece(int ID, Piece.Kind kind, Piece.Color color,
                       Point position) {
        this.ID = ID;
        this.kind = kind;
        this.color = color;
        this.position = position;

        this.size = 0;
    }

    public void initialise(int size) {
        this.size = size;

        move(position);

        // We render the piece as a colored ellipse on top of a black ellipse
        // in order to create an outline and improve the visibility of our pieces.

        Ellipse background = new Ellipse(size * 0.3125, size * 0.26);
        background.setFill(javafx.scene.paint.Color.BLACK);

        background.setStroke(javafx.scene.paint.Color.BLACK);
        background.setStrokeWidth(size * 0.03);

        // TODO: Add comments what this code does.
        background.setTranslateX((size - size * 0.3125 * 2) / 2);
        background.setTranslateY((size - size * 0.26 * 2) / 2 + size * 0.07);

        // TODO: Consider making black pieces black with white outline.
        Ellipse ellipse = new Ellipse(size * 0.3125, size * 0.26);
        if(color == Piece.Color.black) {
            ellipse.setFill(javafx.scene.paint.Color.RED);
        } else if(color == Piece.Color.white) {
            ellipse.setFill(javafx.scene.paint.Color.WHITE);
        }
        // TODO: else assert

        ellipse.setStroke(javafx.scene.paint.Color.BLACK);
        ellipse.setStrokeWidth(size * 0.03);

        // TODO: Add comments what this code does.
        ellipse.setTranslateX((size - size * 0.3125 * 2) / 2);
        ellipse.setTranslateY((size - size * 0.26 * 2) / 2);

        getChildren().addAll(background, ellipse);

        setOnMousePressed(e -> {
            mouseOffsetX = e.getSceneX();
            mouseOffsetY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseOffsetX + lastPosition.x,
                     e.getSceneY() - mouseOffsetY + lastPosition.y);
        });
    }

    public void move(Point point) {
        lastPosition.x = point.x * size;
        lastPosition.y = point.y * size;
        relocate(lastPosition.x, lastPosition.y);
    }

    public void abortMove() {
        relocate(lastPosition.x, lastPosition.y);
    }
}
