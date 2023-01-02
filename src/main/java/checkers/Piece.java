package checkers;

import checkers.utility.Point;

// Piece
// Piece IDs start at 1.
//
public interface Piece {
    public enum Kind {
        pawn,
        king,
    }

    public enum Color {
        white,
        black,
    }

    public static int noneID = 0;

    int getID();
    Kind getKind();
    Color getColor();
    Point getPosition();
}
