package checkers.server;

import checkers.Point;

public class LogicalPiece implements Piece {
    private Color color;
    private Kind kind;
    private Point position;

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
        kind = Kind.queen;
    }
}
