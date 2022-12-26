package checkers;

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

    int getID();
    Kind getKind();
    Color getColor();
    Point getPosition();
}
