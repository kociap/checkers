package checkers.server;

import java.util.Iterator;
import java.util.List;
import checkers.Dimensions2D;
import checkers.Point;

public interface Engine {
    Dimensions2D getBoardSize();
    Iterator<Piece> listPieces();
    List<Point> listMoves(Piece piece);
    MoveResult move(MoveCommand command);
}
