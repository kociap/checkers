package checkers.server.modes;

import checkers.Piece;
import checkers.server.Engine;
import checkers.server.ServerPiece;
import checkers.utility.Dimensions2D;
import checkers.utility.PieceIterable;
import checkers.utility.PieceIterator;
import checkers.utility.Point;
import java.util.ArrayList;
import java.util.List;

// White pieces are located towards y = 0.
// Red pieces are located towards y = height - 1.
public class EnglishDraughts implements Engine {
    private List<ServerPiece> pieces = new ArrayList<>();
    private Dimensions2D size;

    public EnglishDraughts(Dimensions2D size) {
        this.size = size;

        int pieceID = 0;
        // Initialize the white pieces on the board.
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < size.width; x += 2) {
                pieces.add(new ServerPiece(pieceID,
                                           new Point(x + (y + 1) % 2, y),
                                           Piece.Color.white, Piece.Kind.pawn));
                pieceID += 1;
            }
        }

        // Initialize the red pieces on the board.
        for(int y = size.height - 1; y >= size.height - 3; --y) {
            for(int x = 0; x < size.width; x += 2) {
                pieces.add(new ServerPiece(pieceID,
                                           new Point(x + (y + 1) % 2, y),
                                           Piece.Color.red, Piece.Kind.pawn));
                pieceID += 1;
            }
        }
    }

    @Override
    public Dimensions2D getBoardSize() {
        return size;
    }

    @Override
    public Iterable<Piece> listPieces() {
        return new PieceIterable(
            new PieceIterator<ServerPiece>(pieces.iterator()));
    }

    @Override
    public List<Point> listMoves(int pieceID) {
        final List<Point> moves = new ArrayList<>();
        final ServerPiece piece = findPieceByID(pieceID);
        if(piece == null) {
            return moves;
        }

        final int yDirection = (piece.getColor() == Piece.Color.white) ? 1 : -1;
        final Point source = piece.getPosition();

        final Point m1 = checkMove(source, 1, yDirection);
        if(m1 != null) {
            moves.add(m1);
        }

        final Point m2 = checkMove(source, -1, yDirection);
        if(m2 != null) {
            moves.add(m2);
        }

        if(piece.getKind() == Piece.Kind.king) {
            // Promoted pieces may also move backwards.
            final Point m3 = checkMove(source, 1, -yDirection);
            if(m3 != null) {
                moves.add(m3);
            }

            final Point m4 = checkMove(source, -1, -yDirection);
            if(m4 != null) {
                moves.add(m4);
            }
        }

        return moves;
    }

    // @Override
    // public MoveResult move(MoveCommand command) {
    //     return MoveResult.illegal;
    // }

    private ServerPiece findPieceByID(int ID) {
        for(ServerPiece p: pieces) {
            if(p.getID() == ID) {
                return p;
            }
        }
        return null;
    }

    private Point checkMove(Point source, int deltaX, int deltaY) {
        Point diagonal = new Point(source.x + deltaX, source.y + deltaY);
        if(!checkInBounds(diagonal)) {
            return null;
        }

        final ServerPiece p = findPieceAt(diagonal);
        if(p == null) {
            return diagonal;
        } else {
            diagonal.x += deltaX;
            diagonal.y += deltaY;
            if(checkInBounds(diagonal)) {
                return diagonal;
            } else {
                return null;
            }
        }
    }

    private boolean checkInBounds(Point p) {
        return p.x >= 0 && p.x < size.width && p.y >= 0 && p.y < size.height;
    }

    private ServerPiece findPieceAt(Point position) {
        for(ServerPiece piece: pieces) {
            final Point p = piece.getPosition();
            if(position.x == p.x && position.y == p.y) {
                return piece;
            }
        }
        return null;
    }
}
