package checkers.server;

import checkers.MoveCommand;
import checkers.Piece;
import checkers.utility.Dimensions2D;
import checkers.utility.Point;
import java.util.List;

// Engine
// White pieces are located towards y = 0.
// Red pieces are located towards y = height - 1.
//
public interface Engine {
    Dimensions2D getBoardSize();
    Iterable<Piece> listPieces();
    List<Point> listMoves(int pieceID);
    MoveResult move(MoveCommand command);
    Piece.Color getCurrentColor();
}
