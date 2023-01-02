package checkers.server;

import checkers.Piece;
import checkers.utility.Point;

public class MoveResult {
    public Point position = new Point(0, 0);
    public int takenID = Piece.noneID;
    public boolean promoted = false;
    public boolean endTurn = false;
}
