package checkers.modes;

import checkers.Dimensions2D;
import checkers.Point;
import checkers.server.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnglishDraughts12x12 implements Engine {
    private List<LogicalPiece> pieces = new ArrayList<>();
    private Dimensions2D boardSize = new Dimensions2D(12, 12);

    public EnglishDraughts12x12() {
        // Initialize the white pieces on the board.
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < boardSize.width; x += 2) {
                pieces.add(new LogicalPiece(new Point(x + (y + 1) % 2, y),
                                            Piece.Color.white,
                                            checkers.server.Piece.Kind.pawn));
            }
        }

        // Initialize the red pieces on the board.
        for(int y = boardSize.height - 1; y >= boardSize.height - 3; --y) {
            for(int x = 0; x < boardSize.width; x += 2) {
                pieces.add(new LogicalPiece(new Point(x + (y + 1) % 2, y),
                                            Piece.Color.red,
                                            checkers.server.Piece.Kind.pawn));
            }
        }
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
