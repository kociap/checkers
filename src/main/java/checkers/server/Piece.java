package checkers.server;

import checkers.Point;

public interface Piece {
    public enum Kind {
        pawn,
        queen,
    }

    public enum Color {
        white,
        red,
    }

    Kind getKind();
    Color getColor();
    Point getPosition();
}