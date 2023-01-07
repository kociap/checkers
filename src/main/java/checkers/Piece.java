package checkers;

// Piece
// Piece IDs start at 1.
//
public interface Piece {
    enum Kind {
        pawn,
        king,
    }

    enum Color {
        white,
        black,
    }

    int noneID = 0;

    int getID();
    Kind getKind();
    Color getColor();
    Point getPosition();
}
