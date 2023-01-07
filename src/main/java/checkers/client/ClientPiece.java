package checkers.client;

import checkers.Piece;
import checkers.Point;
import javafx.scene.layout.StackPane;
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
        // We render the piece as a colored ellipse with an outline on top of
        // another ellipse in a contrasting color in order to add depth to the
        // pieces and improve their visibility.

        final Ellipse background = new Ellipse(size * 0.3125, size * 0.26);
        background.setStrokeWidth(size * 0.03);
        background.setTranslateX((size - size * 0.3125 * 2) / 2);
        background.setTranslateY((size - size * 0.26 * 2) / 2 + size * 0.07);

        // Indicator is a dot that will appear on a piece after its promotion.
        // Ellipse indicator = new Ellipse(size * 0.15625, size * 0.13);
        // indicator.setStrokeWidth(size * 0.03);
        // indicator.setTranslateX((size - size * 0.15625 * 2) / 2);
        // indicator.setTranslateY((size - size * 0.13 * 2) / 2 + size * 0.07);

        final Ellipse foreground = new Ellipse(size * 0.3125, size * 0.26);
        foreground.setStrokeWidth(size * 0.03);
        foreground.setTranslateX((size - size * 0.3125 * 2) / 2);
        foreground.setTranslateY((size - size * 0.26 * 2) / 2);

        if(color == Piece.Color.black) {
            foreground.setFill(ColorPalette.pieceBlackFg);
            background.setFill(ColorPalette.pieceBlackBg);
            foreground.setStroke(ColorPalette.pieceBlackBg);
            background.setStroke(ColorPalette.pieceBlackBg);
        } else {
            foreground.setFill(ColorPalette.pieceWhiteFg);
            background.setFill(ColorPalette.pieceWhiteBg);
            foreground.setStroke(ColorPalette.pieceWhiteBg);
            background.setStroke(ColorPalette.pieceWhiteBg);
        }

        getChildren().clear();
        getChildren().addAll(background, foreground);
    }
}
