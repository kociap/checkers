package checkers.server;

import checkers.Dimensions2D;
import checkers.Piece;
import checkers.Point;
import java.util.List;

// Engine
// The coordinates start at (1,1) and end at (width,height).
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
