package checkers.client;

import checkers.Piece;
import checkers.Point;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeType;

public class ClientPiece extends StackPane implements Piece {
    private int ID;
    private Piece.Kind kind;
    private Piece.Color color;
    private Point position = new Point(0, 0);

    private Ellipse foreground;
    private Ellipse background;
    private Ellipse foreground2ndLayer;
    private Ellipse background2ndLayer;

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
        readdChildren();
    }

    public ClientPiece(int ID, Piece.Kind kind, Piece.Color color,
                       Point position) {
        this.ID = ID;
        this.kind = kind;
        this.color = color;
        this.position = position;
    }

    public void resize(final int size) {
        setPrefSize(size, size);
        setAlignment(Pos.CENTER);

        // We render the piece as a colored ellipse with an outline on top of
        // another ellipse in a contrasting color in order to add depth to the
        // pieces and improve their visibility.

        final double width = size * 0.3125;
        final double height = size * 0.26;
        final double outline = size * 0.04;

        final double verticalShift = size * 0.05;

        foreground = new Ellipse(width, height);
        foreground.setStrokeType(StrokeType.INSIDE);
        foreground.setStrokeWidth(outline);

        background = new Ellipse(width, height);
        background.setStrokeType(StrokeType.INSIDE);
        background.setStrokeWidth(outline);
        background.setTranslateY(2 * verticalShift);

        foreground2ndLayer = new Ellipse(width, height);
        foreground2ndLayer.setStrokeType(StrokeType.INSIDE);
        foreground2ndLayer.setStrokeWidth(outline);
        foreground2ndLayer.setTranslateY(2 * -verticalShift);

        background2ndLayer = new Ellipse(width, height);
        background2ndLayer.setStrokeType(StrokeType.INSIDE);
        background2ndLayer.setStrokeWidth(outline);

        final javafx.scene.paint.Color colorFg =
            (color == Piece.Color.black ? ColorPalette.pieceBlackFg
                                        : ColorPalette.pieceWhiteFg);
        final javafx.scene.paint.Color colorBg =
            (color == Piece.Color.black ? ColorPalette.pieceBlackBg
                                        : ColorPalette.pieceWhiteBg);

        foreground.setFill(colorFg);
        foreground.setStroke(colorBg);
        background.setFill(colorBg);
        background.setStroke(colorBg);
        foreground2ndLayer.setFill(colorFg);
        foreground2ndLayer.setStroke(colorBg);
        background2ndLayer.setFill(colorBg);
        background2ndLayer.setStroke(colorFg);

        readdChildren();
    }

    private void readdChildren() {
        getChildren().clear();
        if(kind == Piece.Kind.pawn) {
            getChildren().addAll(background, foreground);
        } else {
            getChildren().addAll(background, foreground, background2ndLayer,
                                 foreground2ndLayer);
        }
    }
}
