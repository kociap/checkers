package checkers.server;

import checkers.Point;

public class LogicalPiece implements Piece {
    private Point position;
    private Color color;
    private Kind kind;

    public LogicalPiece(Point position, Color color, Kind kind) {
        this.position = position;
        this.color = color;
        this.kind = kind;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    public void move(Point point) {
        position = point;
    }

    public void promote() {
        kind = Kind.king;
    }
}
