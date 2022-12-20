package checkers.server;

import checkers.Point;

public interface Piece {
    public enum Kind {
        pawn,
        king,
    }

    public enum Color {
        white,
        red,
    }

    Kind getKind();
    Color getColor();
    Point getPosition();
}
