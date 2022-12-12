package checkers;

public class BoardSize {
    public int width;
    public int height;
}

public class Point {
    public int x;
    public int y;
}

// TODO: Needs to be more specific in order for the client to be able 
//       to provide visual feedback.
public enum MoveResult {
    ok,
    illegal,
}

public class MoveCommand {
    public Piece piece;
    public Point from;
    public Point to;
}

public interface Engine {
    BoardSize getBoardSize();
    List<final Piece> listPieces();
    List<Point> listMoves(Piece piece);
    MoveResult move(MoveCommand command);
}
