package checkers.server;

import checkers.utility.Dimensions2D;
import checkers.Piece;
import checkers.utility.Point;
import java.util.Iterator;
import java.util.List;

public interface Engine {
    Dimensions2D getBoardSize();
    Iterator<Piece> listPieces();
    List<Point> listMoves(Piece piece);
    // MoveResult move(MoveCommand command);
}
