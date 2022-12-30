package checkers;

import checkers.utility.Point;

public class MoveCommand {
    public Piece piece;
    public Point from;
    public Point to;

    public MoveCommand(Piece piece, Point from, Point to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }
}
