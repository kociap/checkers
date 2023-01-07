package checkers.client;

import checkers.Piece;
import checkers.Point;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class ClientPiece extends StackPane implements Piece {
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

    public void setPosition(Point position) {
        this.position = position;
    }

    public void promote() {
        this.kind = Piece.Kind.king;
    }

    public ClientPiece(int ID, Piece.Kind kind, Piece.Color color,
                       Point position) {
        this.ID = ID;
        this.kind = kind;
        this.color = color;
        this.position = position;
    }

    public void resize(final int size) {
        // We render the piece as a colored ellipse on top of a black ellipse
        // in order to create an outline and improve the visibility of our pieces.

        Ellipse background = new Ellipse(size * 0.3125, size * 0.26);
        background.setStrokeWidth(size * 0.03);

        // Sets translate witch represent value of relocation of point x and 7
        // and sets this value to values calculated by designer
        background.setTranslateX((size - size * 0.3125 * 2) / 2);
        background.setTranslateY((size - size * 0.26 * 2) / 2 + size * 0.07);

        // Indicator is a red dot that will appear on the piece when it becomes a king
        Ellipse indicator = new Ellipse(size * 0.15625, size * 0.13);
        indicator.setStrokeWidth(size * 0.03);

        // Sets translate witch represent value of relocation of point x and 7
        // and sets this value to values calculated by designer
        indicator.setTranslateX((size - size * 0.15625 * 2) / 2);
        indicator.setTranslateY((size - size * 0.13 * 2) / 2 + size * 0.07);

        // Black pieces are black with withe outline white pieces are white
        // with black outline and kings have red indicator
        Ellipse ellipse = new Ellipse(size * 0.3125, size * 0.26);
        if(kind == Kind.pawn && color == Piece.Color.black) {
            ellipse.setFill(javafx.scene.paint.Color.BLACK);
            background.setFill(javafx.scene.paint.Color.WHITE);
            background.setStroke(javafx.scene.paint.Color.WHITE);
            ellipse.setStroke(javafx.scene.paint.Color.WHITE);
        } else if(kind == Kind.pawn && color == Piece.Color.white) {
            ellipse.setFill(javafx.scene.paint.Color.WHITE);
            background.setFill(javafx.scene.paint.Color.BLACK);
            background.setStroke(javafx.scene.paint.Color.BLACK);
            ellipse.setStroke(javafx.scene.paint.Color.BLACK);
        } else if(kind == Kind.king && color == Piece.Color.black) {
            ellipse.setFill(javafx.scene.paint.Color.BLACK);
            background.setFill(javafx.scene.paint.Color.WHITE);
            background.setStroke(javafx.scene.paint.Color.WHITE);
            ellipse.setStroke(javafx.scene.paint.Color.WHITE);
            indicator.setFill(javafx.scene.paint.Color.RED);
            indicator.setStroke(javafx.scene.paint.Color.RED);
        } else if(kind == Kind.king && color == Color.white) {
            ellipse.setFill(javafx.scene.paint.Color.WHITE);
            background.setFill(javafx.scene.paint.Color.BLACK);
            background.setStroke(javafx.scene.paint.Color.BLACK);
            ellipse.setStroke(javafx.scene.paint.Color.BLACK);
            indicator.setFill(javafx.scene.paint.Color.RED);
            indicator.setStroke(javafx.scene.paint.Color.RED);
        }
        // TODO: else assert

        ellipse.setStrokeWidth(size * 0.03);

        // Sets translate witch represent value of relocation of point x and 7
        // and sets this value to values calculated by designer
        ellipse.setTranslateX((size - size * 0.3125 * 2) / 2);
        ellipse.setTranslateY((size - size * 0.26 * 2) / 2);

        getChildren().clear();
        getChildren().addAll(background, ellipse);
    }
}
