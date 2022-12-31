package checkers.server;

import checkers.Piece;
import checkers.utility.Dimensions2D;
import checkers.utility.Point;
import java.util.List;

public interface Engine {
    Dimensions2D getBoardSize();
    Iterable<Piece> listPieces();
    List<Point> listMoves(int pieceID);
    // MoveResult move(MoveCommand command);
}
