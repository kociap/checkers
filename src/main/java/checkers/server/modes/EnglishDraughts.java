package checkers.server;

import checkers.Dimensions2D;
import checkers.MoveCommand;
import checkers.MoveResult;
import checkers.Piece;
import checkers.PieceIterator;
import checkers.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnglishDraughts implements Engine {
    private List<ServerPiece> pieces = new ArrayList<>();
    private Dimensions2D boardSize;

    public EnglishDraughts(Dimensions2D boardSize) {
        this.boardSize = boardSize;

        int pieceID = 0;
        // Initialize the white pieces on the board.
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < boardSize.width; x += 2) {
                pieces.add(new ServerPiece(pieceID,
                                           new Point(x + (y + 1) % 2, y),
                                           Piece.Color.white, Piece.Kind.pawn));
                pieceID += 1;
            }
        }

        // Initialize the red pieces on the board.
        for(int y = boardSize.height - 1; y >= boardSize.height - 3; --y) {
            for(int x = 0; x < boardSize.width; x += 2) {
                pieces.add(new ServerPiece(pieceID,
                                           new Point(x + (y + 1) % 2, y),
                                           Piece.Color.red, Piece.Kind.pawn));
                pieceID += 1;
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
