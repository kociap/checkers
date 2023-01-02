package checkers.server.modes;

import checkers.Piece;
import checkers.server.Engine;
import checkers.server.MoveResult;
import checkers.server.ServerPiece;
import checkers.utility.Dimensions2D;
import checkers.utility.PieceIterable;
import checkers.utility.PieceIterator;
import checkers.utility.Point;
import java.util.ArrayList;
import java.util.List;

public class EnglishDraughts implements Engine {
    private class InternalMove {
        public Point position;
        public int takenID;

        public InternalMove(Point position, int takenID) {
            this.position = position;
            this.takenID = takenID;
        }
    }

    private List<ServerPiece> pieces = new ArrayList<>();
    private Dimensions2D size;
    private Piece.Color currentColor = Piece.Color.white;
    private ServerPiece lastMovedPiece = null;

    public EnglishDraughts(Dimensions2D size) {
        this.size = size;

        int pieceID = 1;
        // Initialize the white pieces on the board.
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < size.width; x += 2) {
                pieces.add(new ServerPiece(pieceID,
                                           new Point(x + (y + 1) % 2, y),
                                           Piece.Color.white, Piece.Kind.pawn));
                pieceID += 1;
            }
        }

        // Initialize the black pieces on the board.
        for(int y = size.height - 1; y >= size.height - 3; --y) {
            for(int x = 0; x < size.width; x += 2) {
                pieces.add(new ServerPiece(pieceID,
                                           new Point(x + (y + 1) % 2, y),
                                           Piece.Color.black, Piece.Kind.pawn));
                pieceID += 1;
            }
        }
    }

    @Override
    public Dimensions2D getBoardSize() {
        return size;
    }

    @Override
    public Piece.Color getCurrentColor() {
        return currentColor;
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
        final List<InternalMove> internalMoves =
            internalListMoves(piece, hasMandatoryTake());
        for(InternalMove im: internalMoves) {
            moves.add(im.position);
        }
        return moves;
    }

    @Override
    public MoveResult move(final int pieceID, final Point position) {
        final ServerPiece piece = findPieceByID(pieceID);
        if(piece == null) {
            return null;
        }

        if(piece.getColor() != currentColor) {
            return null;
        }

        if(hasMandatoryTake() && piece.getID() != lastMovedPiece.getID()) {
            return null;
        }

        final MoveResult result = new MoveResult();
        final List<InternalMove> moves =
            internalListMoves(piece, hasMandatoryTake());
        InternalMove validMove = null;
        for(final InternalMove im: moves) {
            if(im.position.equals(position)) {
                validMove = im;
                break;
            }
        }

        if(validMove == null) {
            return null;
        }

        result.position = validMove.position;
        result.takenID = validMove.takenID;

        piece.move(validMove.position);
        final ServerPiece taken = findPieceByID(validMove.takenID);
        if(taken != null) {
            pieces.remove(taken);
        }

        // Check for promotion.
        final boolean promoted =
            isInOpponentKingRank(piece) && piece.getKind() == Piece.Kind.pawn;
        result.promoted = promoted;
        if(promoted) {
            piece.promote();
        }

        // Check for take sequence.
        final List<InternalMove> nextMoves = internalListMoves(piece, true);
        if(nextMoves.size() > 0) {
            lastMovedPiece = piece;
        } else {
            // There are no more takes, hence we end the turn.
            result.endTurn = true;
            endTurn();
        }

        return result;
    }

    private List<InternalMove> internalListMoves(final ServerPiece piece,
                                                 final boolean mustBeTake) {
        final List<InternalMove> moves = new ArrayList<>();
        // If there are mandatory takes to be done, we must not list any moves
        // for pieces other than the last moved.
        if(mustBeTake && lastMovedPiece.getID() != piece.getID()) {
            return moves;
        }

        if(piece.getColor() != currentColor) {
            return moves;
        }

        final int yDirection = (piece.getColor() == Piece.Color.white) ? 1 : -1;
        final Point source = piece.getPosition();

        final InternalMove m1 = checkMove(source, 1, yDirection, mustBeTake);
        if(m1 != null) {
            moves.add(m1);
        }

        final InternalMove m2 = checkMove(source, -1, yDirection, mustBeTake);
        if(m2 != null) {
            moves.add(m2);
        }

        if(piece.getKind() == Piece.Kind.king) {
            // Promoted pieces may also move backwards.
            final InternalMove m3 =
                checkMove(source, 1, -yDirection, mustBeTake);
            if(m3 != null) {
                moves.add(m3);
            }

            final InternalMove m4 =
                checkMove(source, -1, -yDirection, mustBeTake);
            if(m4 != null) {
                moves.add(m4);
            }
        }

        return moves;
    }

    private InternalMove checkMove(final Point source, final int deltaX,
                                   final int deltaY, final boolean mustBeTake) {
        Point diagonal = new Point(source.x + deltaX, source.y + deltaY);
        if(!checkInBounds(diagonal)) {
            return null;
        }

        // Check if the square is occupied by a piece.
        final ServerPiece p1 = findPieceAt(diagonal);
        if(p1 == null) {
            if(!mustBeTake) {
                return new InternalMove(diagonal, Piece.noneID);
            } else {
                return null;
            }
        }

        diagonal.x += deltaX;
        diagonal.y += deltaY;
        if(!checkInBounds(diagonal)) {
            return null;
        }

        // Check if there is a piece behind the first one.
        final ServerPiece p2 = findPieceAt(diagonal);
        if(p2 != null) {
            // There is a piece, we cannot move there.
            return null;
        }

        // There is no piece behind the first one, so we can take.
        return new InternalMove(diagonal, p1.getID());
    }

    private boolean isInOpponentKingRank(final ServerPiece piece) {
        final Point position = piece.getPosition();
        final Piece.Color color = piece.getColor();
        final boolean whiteInOpponent =
            color == Piece.Color.white && (position.y == size.height - 1);
        final boolean blackInOpponent =
            color == Piece.Color.black && (position.y == 0);
        return whiteInOpponent || blackInOpponent;
    }

    private ServerPiece findPieceByID(final int ID) {
        for(ServerPiece p: pieces) {
            if(p.getID() == ID) {
                return p;
            }
        }
        return null;
    }

    private ServerPiece findPieceAt(final Point position) {
        for(ServerPiece piece: pieces) {
            final Point p = piece.getPosition();
            if(position.x == p.x && position.y == p.y) {
                return piece;
            }
        }
        return null;
    }

    private boolean hasMandatoryTake() {
        return lastMovedPiece != null;
    }

    private boolean checkInBounds(final Point p) {
        return p.x >= 0 && p.x < size.width && p.y >= 0 && p.y < size.height;
    }

    private void endTurn() {
        if(currentColor == Piece.Color.white) {
            currentColor = Piece.Color.black;
        } else {
            currentColor = Piece.Color.white;
        }
    }
}
