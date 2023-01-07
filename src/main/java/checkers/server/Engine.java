package checkers.server;

import checkers.Dimensions2D;
import checkers.Piece;
import checkers.Point;
import java.util.List;

// Engine
// The coordinates start at (0, 0) and end at (width - 1, height - 1).
// White pieces are located towards y = 1.
// Red pieces are located towards y = height.
//
public interface Engine {
    Dimensions2D getBoardSize();
    Iterable<Piece> listPieces();
    List<Point> listMoves(int pieceID);
    MoveResult move(int pieceID, Point position);
    Piece.Color getCurrentColor();
}
