package checkers;

import checkers.utility.Point;

public class MoveCommand {
    public int pieceID;
    public Point position;

    public MoveCommand(int pieceID, Point position) {
        this.pieceID = pieceID;
        this.position = position;
    }
}
