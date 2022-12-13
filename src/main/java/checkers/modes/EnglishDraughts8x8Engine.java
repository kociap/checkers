package checkers.modes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import checkers.server.*;
import checkers.Dimensions2D;
import checkers.Point;

public class EnglishDraughts8x8Engine implements Engine {
    private List<LogicalPiece> pieces = new ArrayList<>();
    private Dimensions2D boardSize = new Dimensions2D(8, 8);

    public EnglishDraughts8x8Engine() {
        // TODO: Initialize the pieces on the board.
    }

    @Override
    public Dimensions2D getBoardSize() {
        return boardSize;
    }

    @Override
    public Iterator<Piece> listPieces() {
        return new PieceIterator<>(pieces.iterator());
    }

    @Override
    public List<Point> listMoves(Piece piece) {
        return new ArrayList<>();
    }

    @Override
    public MoveResult move(MoveCommand command) {
        return MoveResult.illegal;
    }
}
